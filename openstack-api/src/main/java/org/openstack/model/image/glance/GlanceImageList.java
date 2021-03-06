package org.openstack.model.image.glance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.openstack.model.image.ImageList;

@XmlRootElement(name = "images")
@XmlAccessorType(XmlAccessType.NONE)
public class GlanceImageList implements Serializable, ImageList {

    @XmlElementWrapper(name = "images")
    @XmlElement(name = "image")
    private List<GlanceImage> images = new ArrayList<GlanceImage>();
    
    /* (non-Javadoc)
	 * @see org.openstack.model.image.glance.ImageList#getList()
	 */
    @Override
	public List<GlanceImage> getList() {
    	return images;
    }

	@Override
	public String toString() {
		return "GlanceImageList [images=" + images + "]";
	}
    
}
