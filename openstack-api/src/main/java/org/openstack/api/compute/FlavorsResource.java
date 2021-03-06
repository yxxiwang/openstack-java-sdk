package org.openstack.api.compute;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.compute.NovaFlavorList;

public class FlavorsResource extends Resource {
	
	public FlavorsResource(Target target) {
		super(target);
	}
	
	public NovaFlavorList get() {
		return get(new HashMap<String, Object>());
	}

    public NovaFlavorList get(Map<String, Object> properties) {
    	if(properties.get("detail") != null) {
			target =  target.path("/detail");
		} 
		return target.request(MediaType.APPLICATION_JSON).get(NovaFlavorList.class);
    }

    public FlavorResource flavor(String id) {
    	return new FlavorResource(target.path("/{id}").pathParam("id", id));
    }

	

}
