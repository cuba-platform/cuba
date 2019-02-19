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

import com.haulmont.cuba.web.widgets.client.html.AttributeInfo;
import com.haulmont.cuba.web.widgets.client.html.AttributeType;
import com.haulmont.cuba.web.widgets.client.html.HtmlAttributesClientRpc;
import com.haulmont.cuba.web.widgets.client.html.HtmlAttributesExtensionState;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Extension;
import com.vaadin.ui.Component;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

import static com.haulmont.cuba.web.widgets.client.html.HtmlAttributesExtensionState.DEFAULT_SELECTOR;

public class HtmlAttributesExtension extends AbstractExtension {

    protected Map<String, Set<String>> removeDomAttributes = Collections.emptyMap();
    protected Map<String, Set<String>> removeCssProperties = Collections.emptyMap();

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
        setDomAttribute(DEFAULT_SELECTOR, attributeName, value);
    }

    public void setDomAttribute(String querySelector, String attributeName, String value) {
        if (!removeDomAttributes.isEmpty()) {
            Set<String> attributes = removeDomAttributes.get(querySelector);
            if (CollectionUtils.isNotEmpty(attributes)) {
                attributes.remove(attributeName);
            }
        }

        HtmlAttributesExtensionState state = getState();

        if (state.attributes.isEmpty()) {
            state.attributes = new HashMap<>();
        }
        Set<AttributeInfo> attributes = state.attributes.computeIfAbsent(querySelector, k -> new HashSet<>());

        addOrUpdate(attributes, AttributeInfo.dom(attributeName, value));
    }

    public String getDomAttribute(String attributeName) {
        return getDomAttribute(DEFAULT_SELECTOR, attributeName);
    }

    public String getDomAttribute(String querySelector, String attributeName) {
        return getAttributeValue(querySelector, attributeName, AttributeType.DOM);
    }

    public void removeDomAttribute(String attributeName) {
        removeDomAttribute(DEFAULT_SELECTOR, attributeName);
    }

    public void removeDomAttribute(String querySelector, String attributeName) {
        if (!getState().attributes.isEmpty()) {
            getState().attributes.getOrDefault(querySelector, Collections.emptySet())
                    .removeIf(attributeInfo -> attributeInfo.getType() == AttributeType.DOM
                            && attributeInfo.getName().equals(attributeName));

            if (removeDomAttributes.isEmpty()) {
                removeDomAttributes = new HashMap<>();
            }
            removeDomAttributes
                    .computeIfAbsent(querySelector, k -> new HashSet<>())
                    .add(attributeName);
        }
    }

    public void setCssProperty(String propertyName, String value) {
        setCssProperty(DEFAULT_SELECTOR, propertyName, value);
    }

    public void setCssProperty(String querySelector, String propertyName, String value) {
        if (!removeCssProperties.isEmpty()) {
            Set<String> properties = removeCssProperties.get(querySelector);
            if (CollectionUtils.isNotEmpty(properties)) {
                properties.remove(propertyName);
            }
        }

        HtmlAttributesExtensionState state = getState();

        if (state.attributes.isEmpty()) {
            state.attributes = new HashMap<>();
        }
        Set<AttributeInfo> attributes = state.attributes.computeIfAbsent(querySelector, k -> new HashSet<>());

        addOrUpdate(attributes, AttributeInfo.css(propertyName, value));
    }

    public String getCssProperty(String propertyName) {
        return getCssProperty(DEFAULT_SELECTOR, propertyName);
    }

    public String getCssProperty(String querySelector, String propertyName) {
        return getAttributeValue(querySelector, propertyName, AttributeType.CSS);
    }

    public void removeCssProperty(String propertyName) {
        removeCssProperty(DEFAULT_SELECTOR, propertyName);
    }

    public void removeCssProperty(String querySelector, String propertyName) {
        if (!getState().attributes.isEmpty()) {
            getState().attributes.getOrDefault(querySelector, Collections.emptySet())
                    .removeIf(attributeInfo -> attributeInfo.getType() == AttributeType.CSS
                            && attributeInfo.getName().equals(propertyName));

            if (removeCssProperties.isEmpty()) {
                removeCssProperties = new HashMap<>();
            }
            removeCssProperties
                    .computeIfAbsent(querySelector, k -> new HashSet<>())
                    .add(propertyName);
        }
    }

    protected String getAttributeValue(String querySelector, String attributeName, AttributeType type) {
        Set<AttributeInfo> attributes = getState(false).attributes.get(querySelector);
        if (CollectionUtils.isNotEmpty(attributes)) {
            return attributes.stream().filter(attributeInfo ->
                    attributeInfo.getType() == type
                            && attributeInfo.getName().equals(attributeName))
                    .map(AttributeInfo::getValue)
                    .findFirst().orElse(null);
        }

        return null;
    }

    protected void addOrUpdate(Set<AttributeInfo> attributes, AttributeInfo item) {
        if (!attributes.add(item)) {
            attributes.remove(item);
            attributes.add(item);
        }
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        if (!initial) {
            HtmlAttributesClientRpc clientRpc = getRpcProxy(HtmlAttributesClientRpc.class);

            if (!removeDomAttributes.isEmpty()) {
                clientRpc.removeDomAttributes(removeDomAttributes);

                this.removeDomAttributes = Collections.emptyMap();
            }

            if (!removeCssProperties.isEmpty()) {
                clientRpc.removeCssProperties(removeCssProperties);

                this.removeCssProperties = Collections.emptyMap();
            }
        }
    }
}