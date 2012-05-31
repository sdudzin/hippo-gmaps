/*
 * Copyright 2011 Siarhei Dudzin.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.onehippo.plugins.gmaps;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.IEditor;
import org.hippoecm.frontend.service.render.ListViewPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class GmapsPlugin extends ListViewPlugin implements IHeaderContributor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GmapsPlugin.class);
    //External Resources
    private static final String JQUERY_JS = "http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js";
    private static final String JQUERY_UI_JS = "http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js";
    private static final String JQUERY_UI_CSS = "http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/themes/base/jquery-ui.css";
    //Custom and internal Resources
    private static final String AUTOCOMPLETE_CSS = "ui.geo_autocomplete.css";
    private static final String AUTOCOMPLETE_JS = "ui.geo_autocomplete.js";
    private static final String MAPS_JS = "hippo-gmaps-plugin.js";

    private static final String DEFAULT_ZOOM_LEVEL = "10";

    private String autocompleteId;
    private String mapId;
    private IEditor.Mode mode;

    public GmapsPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

        WebMarkupContainer mapDiv = createMapComponent("map");
        mapId = mapDiv.getMarkupId();
        add(mapDiv);

        WebMarkupContainer autocompleteLabel = createAutocompleteComponent("autocompleteLabel");
        add(autocompleteLabel);

        WebMarkupContainer autocompleteContainer = createAutocompleteComponent("autocomplete");
        autocompleteId = autocompleteContainer.getMarkupId();
        add(autocompleteContainer);

        setOutputMarkupId(true);

        Image mapImage = createStaticMapImage();
        add(mapImage);

        mode = IEditor.Mode.fromString(config.getString("mode", "view"));
        if (IEditor.Mode.EDIT.equals(mode)) {
            mapDiv.setVisible(true);
            autocompleteContainer.setVisible(true);
            autocompleteLabel.setVisible(true);
            mapImage.setVisible(false);
        } else {
            mapDiv.setVisible(false);
            autocompleteContainer.setVisible(false);
            autocompleteLabel.setVisible(false);
            mapImage.setVisible(true);
        }

    }

    private WebMarkupContainer createAutocompleteComponent(String id) {
        WebMarkupContainer container = new WebMarkupContainer(id);
        container.setOutputMarkupId(true);
        return container;
    }

    private WebMarkupContainer createMapComponent(String id) {
        WebMarkupContainer mapDiv = new WebMarkupContainer(id);
        mapDiv.setOutputMarkupId(true);
        mapDiv.add(new AttributeModifier("style", true, new Model<String>("width:400px;height:400px;margin: 0 0 3px 8px;")));
        return mapDiv;
    }

    private Image createStaticMapImage() {
        final String mapImgSrc = "http://maps.google.com/maps/api/staticmap?" +
                "&zoom=" + getZoom() + "&" +
                "size=300x300" +
                "&maptype=roadmap" +
                "&markers=color:green|" +
                getLocation()[0] + "," +
                getLocation()[1] +
                "&sensor=true";

        Image mapImage = new Image("mapImage");

        mapImage.add(new AttributeModifier("style", true, new Model<String>("margin: 0 0 3px 8px;")));
        mapImage.add(new AttributeModifier("src", true, new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 1L;

            /**
             * {@inheritDoc}
             */
            @Override
            public final String getObject() {
                return mapImgSrc;
            }
        }));
        mapImage.setOutputMarkupId(true);
        return mapImage;
    }

    public void renderHead(IHeaderResponse response) {
        if (IEditor.Mode.EDIT.equals(mode)) {
            // load jquery js + css
            response.renderJavascriptReference(JQUERY_JS);
            response.renderCSSReference(JQUERY_UI_CSS);
            response.renderJavascriptReference(JQUERY_UI_JS);
            // load autocomplete js + css
            ResourceReference autocompleteCss =
                    new CompressedResourceReference(GmapsPlugin.class, AUTOCOMPLETE_CSS);
            response.renderCSSReference(autocompleteCss);
            JavascriptResourceReference autocompleteJs = new JavascriptResourceReference(GmapsPlugin.class, AUTOCOMPLETE_JS);
            response.renderJavascriptReference(autocompleteJs);

            // custom functions
            JavascriptResourceReference jsResourceReference = new JavascriptResourceReference(GmapsPlugin.class, MAPS_JS);
            response.renderJavascriptReference(jsResourceReference);

            // load gmaps js
            String[] location = getLocation();
            String zoom = getZoom();
            response.renderJavascript(
                    "function initMap" + mapId + "(){" +
                            "var map = document.getElementById('" + mapId + "');" +
                            "initializeMap(map, " + location[0] + ", " + location[1] + ", " + zoom + ", " + autocompleteId + ");" +
                            "};"
                    , mapId + "Js");

            response.renderOnDomReadyJavascript(
                    "$.getScript('http://maps.google.com/maps/api/js?sensor=true&callback=initMap" + mapId + "');"
            );
        }
    }

    private String[] getLocation() {
        String[] location = new String[2];
        Node locationNode = (Node) getDefaultModelObject();
        try {
            if (locationNode != null) {
                location[0] = String.valueOf(locationNode.getProperty("gmaps:latitude").getDouble());
                location[1] = String.valueOf(locationNode.getProperty("gmaps:longitude").getDouble());
            } else {
                location[0] = "0.0";
                location[1] = "0.0";
            }
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return location;
    }

    private String getZoom() {
        String zoom;
        Node locationNode = (Node) getDefaultModelObject();
        try {
            if (locationNode != null) {
                zoom = String.valueOf(locationNode.getProperty("gmaps:zoom").getLong());
            } else {
                zoom = DEFAULT_ZOOM_LEVEL;
            }
        } catch (RepositoryException e) {
            zoom = DEFAULT_ZOOM_LEVEL;
            LOGGER.error(e.getMessage(), e);
        }
        return zoom;
    }

}
