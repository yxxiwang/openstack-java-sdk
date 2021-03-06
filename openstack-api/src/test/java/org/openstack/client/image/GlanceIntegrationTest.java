package org.openstack.client.image;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.openstack.client.AbstractOpenStackTest;
import org.openstack.client.jersey2.OpenStackImagesClient;
import org.openstack.client.utils.RandomDataInputStream;
import org.openstack.model.exceptions.OpenstackException;
import org.openstack.model.exceptions.OpenstackNotFoundException;
import org.openstack.model.image.Image;
import org.openstack.model.image.glance.GlanceImage;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

public class GlanceIntegrationTest extends AbstractOpenStackTest {
	
	@BeforeClass
	public void init() {
		//if (!glanceEnabled) {
			//	throw new SkipException("Skipping because glance not present / accessible");
		//}
		super.init();
		
		//storage = client.target("http://192.168.1.52:8080/v1/AUTH_"+client.getAccess().getToken().getTenant().getId(), AccountResource.class);
	}
	
	@Test
	public void testListImagesAndDetails() throws OpenstackException {
		

		OpenStackImagesClient glance = client.images();
		List<GlanceImage> images = Lists.newArrayList(glance.publicEndpoint().get().getList());

		for (Image image : images) {
			Image details = glance.publicEndpoint().image(image.getId()).head();

			assertImageEquals(details, image);
		}
	}

	private void assertImageEquals(Image actual, Image expected) {
		assertEquals(actual.getId(), expected.getId());
		assertEquals(actual.getChecksum(), expected.getChecksum());
		assertEquals(actual.getContainerFormat(), expected.getContainerFormat());
		assertEquals(actual.getCreatedAt(), expected.getCreatedAt());
		assertEquals(actual.getDeletedAt(), expected.getDeletedAt());
		assertEquals(actual.getDiskFormat(), expected.getDiskFormat());
		assertEquals(actual.getMinDisk(), expected.getMinDisk());
		assertEquals(actual.getMinRam(), expected.getMinRam());
		assertEquals(actual.getName(), expected.getName());
		assertEquals(actual.getOwner(), expected.getOwner());
		assertEquals(actual.getSize(), expected.getSize());
		assertEquals(actual.getStatus(), expected.getStatus());
		assertEquals(actual.getProperties(), expected.getProperties());
	}

	// Heap size problems should be fixed now!
	static final int MAX_LENGTH = 16 * 1024 * 1024;

	@Test
	public void testImageUploadAndDelete() throws Exception {

		OpenStackImagesClient glance = client.images();

		String containerFormat = "bare";
		String diskFormat = "raw";

		int imageLength = random.uniform(1, MAX_LENGTH);
		long seed = random.nextLong();

		RandomDataInputStream stream = new RandomDataInputStream(imageLength, seed);

		Image template = new GlanceImage();
		template.setName(random.randomAlphanumericString(1, 64).trim());
		template.setDiskFormat(diskFormat );
		template.setContainerFormat(containerFormat);

		Image uploaded = glance.publicEndpoint().post(stream, imageLength, template);
		assertEquals(uploaded.getSize(), Long.valueOf(imageLength));
		assertEquals(uploaded.getName(), template.getName());
		assertNull(uploaded.getDeletedAt());
		assertNotNull(uploaded.getCreatedAt());
		assertNotNull(uploaded.getUpdatedAt());
		assertNotNull(uploaded.getId());
		assertEquals(uploaded.isDeleted(), Boolean.FALSE);
		assertEquals(uploaded.getDiskFormat(), diskFormat);
		assertEquals(uploaded.getContainerFormat(), containerFormat);
		assertNotNull(uploaded.getOwner());
		assertEquals(uploaded.getStatus(), "active");

		{
			//Md5Hash md5 = new Md5Hash();
			//byte[] hash = md5.hash(stream.clone());
			//assertEquals(uploaded.getChecksum(), Hex.encodeHexString(hash));
		}

		List<GlanceImage> allImages = Lists.newArrayList(glance.publicEndpoint().get().getList());

		Image foundInAll = findImageById(allImages, uploaded.getId());
		assertNotNull(foundInAll);
		assertImageEquals(foundInAll, uploaded);

		Image details = glance.publicEndpoint().image(uploaded.getId()).head();
		assertImageEquals(details, uploaded);

		{
			InputStream is = glance.publicEndpoint().image(uploaded.getId()).openStream();
			IOUtils.contentEquals(is, stream.clone());
			is.close();
		}
		
		System.out.println("DELETING ....");

		Response response = glance.publicEndpoint().image(uploaded.getId()).delete();

		for (int i = 0; i < 60; i++) {
			System.out.print(".");
			// Wait for up to 60 seconds for the image to be deleted
			allImages = Lists.newArrayList(glance.publicEndpoint().get().getList());
			foundInAll = findImageById(allImages, uploaded.getId());
			if (foundInAll == null)
				break;
			Thread.sleep(1000);
		}
		System.out.println(".");

		assertNull(foundInAll);

		try {
			glance.publicEndpoint().image(uploaded.getId()).head();
			Assert.fail();
		} catch (OpenstackNotFoundException e) {
			// Expected!
		}
	}

	@Test(expectedExceptions= { OpenstackException.class, SkipException.class} )
	public void testNullFormatsFails() throws Exception {

		// https://bugs.launchpad.net/glance/+bug/933702
		// The patch for this landed about Feb 23, 2012
		skipUntilBugFixed(933702);

		OpenStackImagesClient glance = client.images();

		int imageLength = 128;
		long seed = random.nextLong();

		RandomDataInputStream stream = new RandomDataInputStream(imageLength, seed);

		Image template = new GlanceImage();
		template.setName(random.randomAlphanumericString(1, 64).trim());

		Image uploaded = glance.publicEndpoint().post(stream, imageLength, template);
		assertNull(uploaded.getDiskFormat());
		assertNull(uploaded.getContainerFormat());
	}

	@Test
	public void testNullNameFails() throws Exception {

		// It's not clear whether a null name is supposed to fail or not
		// https://bugs.launchpad.net/glance/+bug/934492
		skipUntilBugFixed(934492);

		OpenStackImagesClient glance = client.images();

		int imageLength = 128;
		long seed = random.nextLong();

		RandomDataInputStream stream = new RandomDataInputStream(imageLength, seed);

		Image template = new GlanceImage();

		Image uploaded = glance.publicEndpoint().post(stream, imageLength, template);
		System.out.println(uploaded);
		Assert.fail("Image upload without a name should fail");
	}

	private Image findImageById(List<GlanceImage> images, String id) {
		for (Image image : images) {
			if (id.equals(image.getId()))
				return image;
		}
		return null;
	}

}
