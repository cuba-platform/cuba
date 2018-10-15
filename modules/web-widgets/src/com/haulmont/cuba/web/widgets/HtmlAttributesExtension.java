/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.html.HtmlAttributesClientRpc;
import com.haulmont.cuba.web.widgets.client.html.HtmlAttributesExtensionState;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Extension;
import com.vaadin.ui.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HtmlAttributesExtension extends AbstractExtension {

    protected Set<String> removeDomAttributes = Collections.emptySet();
    protected Set<String> removeCssProperties = Collections.emptySet();

    public HtmlAttributesExtension(AbstractClientConnector target) {
        super(target);
    }

    public static HtmlAttributesExtension get(Component component) {
        for (Extension e : component.getExtensions()) {
            if (e instanceof HtmlAttributesExtension) {
                return (HtmlAttributesExtension) e;
            }
        }
        return new HtmlAttributesExtension((AbstractClientConnector) component);
    }

    @Override
    protected HtmlAttributesExtensionState getState() {
        return (HtmlAttributesExtensionState) super.getState();
    }

    @Override
    protected HtmlAttributesExtensionState getState(boolean markAsDirty) {
        return (HtmlAttributesExtensionState) super.getState(markAsDirty);
    }

    public void setDomAttribute(String attributeName, String value) {
        if (!removeDomAttributes.isEmpty()) {
            removeDomAttributes.remove(attributeName);
        }

        HtmlAttributesExtensionState state = getState();

        if (state.dom.isEmpty()) {
            state.dom = new HashMap<>();
        }
        state.dom.put(attributeName, value);
    }

    public String getDomAttribute( String attributeName) {
        return getState(false).dom.get(attributeName);
    }

    public void removeDomAttribute(String attributeName) {
        if (!getState().dom.isEmpty()) {
            getState().dom.remove(attributeName);

            if (removeDomAttributes.isEmpty()) {
                removeDomAttributes = new HashSet<>();
            }
            removeDomAttributes.add(attributeName);
        }
    }

    public void setCssProperty(String propertyName, String value) {
        if (!removeCssProperties.isEmpty()) {
            removeCssProperties.remove(propertyName);
        }

        HtmlAttributesExtensionState state = getState();

        if (state.css.isEmpty()) {
            state.css = new HashMap<>();
        }
        state.css.put(propertyName, value);
    }

    public String getCssProperty(String propertyName) {
        return getState(false).css.get(propertyName);
    }

    public void removeCssProperty(String propertyName) {
        if (!getState().css.isEmpty()) {
            getState().css.remove(propertyName);

            if (removeCssProperties.isEmpty()) {
                removeCssProperties = new HashSet<>();
            }
            removeCssProperties.add(propertyName);
        }
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        if (!initial) {
            HtmlAttributesClientRpc clientRpc = getRpcProxy(HtmlAttributesClientRpc.class);

            if (!removeDomAttributes.isEmpty()) {
                clientRpc.removeDomAttributes(removeDomAttributes);

                this.removeDomAttributes = Collections.emptySet();
            }

            if (!removeCssProperties.isEmpty()) {
                clientRpc.removeCssProperties(removeCssProperties);

                this.removeCssProperties = Collections.emptySet();
            }
        }
    }
}