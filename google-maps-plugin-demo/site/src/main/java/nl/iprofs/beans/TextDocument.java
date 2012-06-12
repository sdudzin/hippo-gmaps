package nl.iprofs.beans;

import nl.iprofs.beans.compound.Location;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType = "googlemapsplugindemo:textdocument")
public class TextDocument extends BaseDocument {

    public HippoHtml getHtml() {
        return getHippoHtml("googlemapsplugindemo:body");
    }

    public String getSummary() {
        return getProperty("googlemapsplugindemo:summary");
    }

    public String getTitle() {
        return getProperty("googlemapsplugindemo:title");
    }

    public Location getLocation() {
        return getBean("googlemapsplugindemo:gmaps_location");
    }

}
