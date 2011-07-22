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

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;
import org.hippoecm.frontend.editor.plugins.field.PropertyFieldPlugin;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;

public class LatitudeFieldPlugin extends PropertyFieldPlugin {

    private static final String CLASS = "gmaps-lat";

    public LatitudeFieldPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

        add(new AttributeAppender("class", new Model<String>(CLASS), " "));
    }

}
