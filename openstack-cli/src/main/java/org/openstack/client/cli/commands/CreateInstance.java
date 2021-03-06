package org.openstack.client.cli.commands;

import java.util.HashMap;

import javax.ws.rs.client.Entity;

import org.kohsuke.args4j.Argument;
import org.openstack.client.cli.OpenstackCliContext;
import org.openstack.client.cli.model.FlavorName;
import org.openstack.client.cli.model.ImageName;
import org.openstack.client.jersey2.OpenStackComputeClient;
import org.openstack.model.compute.NovaImage;
import org.openstack.model.compute.NovaServerForCreate;

public class CreateInstance extends OpenstackCliCommandRunnerBase {
	@Argument(index = 0)
	public String instanceName;

	@Argument(index = 1)
	public ImageName imageName;

	@Argument(index = 2)
	public FlavorName flavorName;

	public CreateInstance() {
		super("create", "instance");
	}

	@Override
	public Object runCommand() throws Exception {
		OpenstackCliContext context = getContext();

		NovaImage image = imageName.findImage(context);
		if (image == null) {
			throw new IllegalArgumentException("Cannot find image: " + imageName.getKey());
		}

		String flavorId = flavorName.findImageId(context);
		if (flavorId == null) {
			throw new IllegalArgumentException("Cannot find flavor: " + flavorName.getKey());
		}

		OpenStackComputeClient tenant = context.getComputeClient();
		NovaServerForCreate serverForCreate = new NovaServerForCreate();
		serverForCreate.setName(instanceName);

		serverForCreate.setImageRef(image.getId());
		serverForCreate.setFlavorRef(flavorId);

		return tenant.publicEndpoint().servers().post(new HashMap<String, Object>(), Entity.json(serverForCreate));
	}

}
