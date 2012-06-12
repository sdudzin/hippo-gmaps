package nl.iprofs.beans.compound;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bean mapping class for the 'gmaps:location' document type
 */
@Node(jcrType = "gmaps:location")
public class Location extends HippoItem implements HippoBean {

    private static final Logger LOG = LoggerFactory.getLogger(Location.class);

    public int getZoom() {
        if (!getValueProvider().hasProperty("gmaps:zoom")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("no zoom property available for location '{}'. Return -1", getValueProvider().getPath());
            }
            return -1;
        }
        return this.getValueProvider().getLong("gmaps:zoom").intValue();
    }

    public Double getLatitude() {
        if (!getValueProvider().hasProperty("gmaps:latitude")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("no latitude property available for location '{}'. Return -1.0", getValueProvider().getPath());
            }
            return -1.0;
        }
        return this.getValueProvider().getDouble("gmaps:latitude");
    }

    public Double getLongitude() {
        if (!getValueProvider().hasProperty("gmaps:longitude")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("no longtitude property available for location '{}'. Return -1.0", getValueProvider().getPath());
                return -1.0;
            }
        }
        return this.getValueProvider().getDouble("gmaps:longitude");
    }


}
