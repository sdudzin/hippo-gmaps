
package org.example.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType="myhippoproject:basedocument")
public class BaseDocument extends HippoDocument {

    public String getTitle() {
        return getProperty("myhippoproject:title");
    }
    
    public String getSummary() {
        return getProperty("myhippoproject:summary");
    }
}
