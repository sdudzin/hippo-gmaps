package nl.iprofs.beans;

import java.util.Calendar;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean;

@Node(jcrType="googlemapsplugindemo:newsdocument")
public class NewsDocument extends BaseDocument{

    public Calendar getDate() {
        return getProperty("googlemapsplugindemo:date");
    }

    public HippoHtml getHtml(){
        return getHippoHtml("googlemapsplugindemo:body");    
    }

    /**
     * Get the imageset of the newspage
     *
     * @return the imageset of the newspage
     */
    public HippoGalleryImageSetBean getImage() {
        return getLinkedBean("googlemapsplugindemo:image", HippoGalleryImageSetBean.class);
    }

    public String getSummary() {
        return getProperty("googlemapsplugindemo:summary");
    }

    public String getTitle() {
        return getProperty("googlemapsplugindemo:title");
    }

}
