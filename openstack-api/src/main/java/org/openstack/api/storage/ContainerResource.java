package org.openstack.api.storage;

import java.util.List;

import javax.ws.rs.client.Target;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openstack.api.common.Resource;
import org.openstack.model.storage.SwiftStorageObject;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;


public class ContainerResource extends Resource {
	
	// Verb URI Description
	// GET /account/container List objects
	// PUT /account/container Create container
	// DELETE /account/container Delete container
	// HEAD /account/container Retrieve container metadata
	
	public ContainerResource(Target target) {
		super(target);
	}
	
	public List<SwiftStorageObject> get() {
		return target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<SwiftStorageObject>>() {});
	}
	
	public List<SwiftStorageObject> get(String prefix, String delimiter) {
		
		Builder b = target.request();
		

		if (prefix != null) {
			//this.target = b.addQueryParameter("prefix", prefix);
		}
		if (!Strings.isNullOrEmpty(delimiter)) {
			//b.addQueryParameter("delimiter", delimiter);
		}

		//b.clearAcceptTypes();
		//b.addAcceptType(MediaType.TEXT_PLAIN_TYPE);

		//String listing = requestBuilder.get(String.class);
		String listing = "";
		List<SwiftStorageObject> list = Lists.newArrayList();
		for (String line : Splitter.on("\n").split(listing)) {
			if (line.isEmpty())
				continue;

			SwiftStorageObject storageObject = new SwiftStorageObject();
			storageObject.setName(line);
			list.add(storageObject);
		}

		return list;
	}
	
	public Response put() {
		return target.request().method("PUT");
	}
	
	public Response head() {
		return target.request(MediaType.APPLICATION_JSON).method("HEAD");
	}
	
	public Response delete() {
		return target.request().method("DELETE");
	}

	public ObjectResource object(String name) {
		return new ObjectResource(target.path("/{name}").pathParam("name", name));
	}
	
}
