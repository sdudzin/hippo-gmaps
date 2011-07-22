
package org.example.beans;

import org.example.beans.compound.Location;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType="myhippoproject:textdocument")
public class TextDocument extends BaseDocument{

    public HippoHtml getHtml(){
        return getHippoHtml("myhippoproject:body");    
    }

    public Location getLocation() {
        return getBean("myhippoproject:gmaps_location");
    }
}
