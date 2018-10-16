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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HtmlAttributes;
import com.haulmont.cuba.web.widgets.HtmlAttributesExtension;
import org.apache.commons.lang3.StringUtils;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

@org.springframework.stereotype.Component(HtmlAttributes.NAME)
public class HtmlAttributesImpl implements HtmlAttributes {
    @Override
    public void setDomAttribute(Component component, String attributeName, String value) {
        checkNotNullArgument(component);
        checkNotNullArgument(attributeName);

        com.vaadin.ui.Component vComponent = component.unwrapComposition(com.vaadin.ui.Component.class);
        HtmlAttributesExtension.get(vComponent)
                .setDomAttribute(attributeName, value);
    }

    @Override
    public String getDomAttribute(Component component, String attributeName) {
        checkNotNullArgument(component);
        checkNotNullArgument(attributeName);

        com.vaadin.ui.Component vComponent = component.unwrapComposition(com.vaadin.ui.Component.class);
        return HtmlAttributesExtension.get(vComponent)
                .getDomAttribute(attributeName);
    }

    @Override
    public void removeDomAttribute(Component component, String attributeName) {
        checkNotNullArgument(component);
        checkNotNullArgument(attributeName);

        com.vaadin.ui.Component vComponent = component.unwrapComposition(com.vaadin.ui.Component.class);
        HtmlAttributesExtension.get(vComponent)
                .removeDomAttribute(attributeName);
    }

    @Override
    public void setCssProperty(Component component, String propertyName, String value) {
        checkNotNullArgument(component);
        checkNotNullArgument(propertyName);

        com.vaadin.ui.Component vComponent = component.unwrapComposition(com.vaadin.ui.Component.class);
        HtmlAttributesExtension.get(vComponent)
                .setCssProperty(propertyName, value);
    }

    @Override
    public String getCssProperty(Component component, String propertyName) {
        checkNotNullArgument(component);
        checkNotNullArgument(propertyName);

        com.vaadin.ui.Component vComponent = component.unwrapComposition(com.vaadin.ui.Component.class);
        return HtmlAttributesExtension.get(vComponent)
                .getCssProperty(propertyName);
    }

    @Override
    public void removeCssProperty(Component component, String propertyName) {
        checkNotNullArgument(component);
        checkNotNullArgument(propertyName);

        com.vaadin.ui.Component vComponent = component.unwrapComposition(com.vaadin.ui.Component.class);
        HtmlAttributesExtension.get(vComponent)
                .removeCssProperty(propertyName);
    }

    @Override
    public void applyCss(Component component, String css) {
        String[] propertyStatements = StringUtils.split(css, ';');
        for (String propertyStatement : propertyStatements) {
            if (StringUtils.isBlank(propertyStatement)) {
                continue;
            }

            int separatorIndex = propertyStatement.indexOf(':');
            if (separatorIndex < 0) {
                throw new IllegalArgumentException("Incorrect CSS string: " + css);
            }

            String propertyName = trimToEmpty(propertyStatement.substring(0, separatorIndex));
            String propertyValue = trimToEmpty(propertyStatement.substring(separatorIndex + 1));

            if (StringUtils.isBlank(propertyName)) {
                throw new IllegalArgumentException("Incorrect CSS string, empty property name: " + css);
            }

            setCssProperty(component, propertyName, propertyValue);
        }
    }
}