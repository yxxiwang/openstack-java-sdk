package org.openstack.api.storage;

import java.util.List;

import javax.ws.rs.client.Target;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openstack.api.common.Resource;
import org.openstack.model.storage.SwiftContainer;

public class AccountResource extends Resource {
	
	// GET /account List containers
	// HEAD account Retrieve account metadata

	
	public AccountResource(Target target) {
		super(target);
	}

	public List<SwiftContainer> get() {
		return target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<SwiftContainer>>() {});
	}
	
	public Response head() {
		return target.request(MediaType.APPLICATION_JSON).head();
	}

	public ContainerResource container(String id) {
		return new ContainerResource(target.path("/{id}").pathParam("id", id));
	}

}
