package org.openstack.api.compute;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;

import org.openstack.api.common.Resource;
import org.openstack.model.compute.NovaImageList;

public class ImagesResource extends Resource {
	
	public ImagesResource(Target target) {
		super(target);
	}
	
	public NovaImageList get() {
		return get(new HashMap<String, Object>());
	}

    public NovaImageList get(Map<String,Object> properties) {
		if(properties.get("detail") != null) {
			target =  target.path("/detail");
		} 
		return target.request(MediaType.APPLICATION_JSON).get(NovaImageList.class);
	}

    public ImageResource image(String id) {
    	return new ImageResource(target.path("/{id}").pathParam("id", id));
    }

	

}
