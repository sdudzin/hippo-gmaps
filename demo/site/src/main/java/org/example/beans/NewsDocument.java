
package org.example.beans;

import java.util.Calendar;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean;

@Node(jcrType="myhippoproject:newsdocument")
public class NewsDocument extends TextDocument{

    public Calendar getDate() {
        return getProperty("myhippoproject:date");
    }

    /**
     * Get the imageset of the newspage
     *
     * @return the imageset of the newspage
     */
    public HippoGalleryImageSetBean getImage() {
        return getLinkedBean("myhippoproject:image", HippoGalleryImageSetBean.class);
    }
    
}
