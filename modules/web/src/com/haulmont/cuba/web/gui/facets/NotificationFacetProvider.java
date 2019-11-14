/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.gui.facets;

import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.Notifications.Position;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.components.NotificationFacet;
import com.haulmont.cuba.gui.xml.FacetProvider;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.web.gui.components.WebNotificationFacet;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component(NotificationFacetProvider.NAME)
public class NotificationFacetProvider implements FacetProvider<NotificationFacet> {

    public static final String NAME = "cuba_NotificationFacetProvider";

    @Inject
    protected MessageTools messageTools;

    @Override
    public Class<NotificationFacet> getFacetClass() {
        return NotificationFacet.class;
    }

    @Override
    public NotificationFacet create() {
        return new WebNotificationFacet();
    }

    @Override
    public String getFacetTag() {
        return "notification";
    }

    @Override
    public void loadFromXml(NotificationFacet facet, Element element,
                            ComponentLoader.ComponentContext context) {
        loadId(facet, element);
        loadCaption(facet, element, context);
        loadDescription(facet, element, context);
        loadType(facet, element);
        loadDelay(facet, element);
        loadContentMode(facet, element);
        loadStyleName(facet, element);
        loadPosition(facet, element);
        loadTarget(facet, element, context);
    }

    protected void loadId(NotificationFacet facet, Element element) {
        String id = element.attributeValue("id");
        if (isNotEmpty(id)) {
            facet.setId(id);
        }
    }

    protected void loadCaption(NotificationFacet facet, Element element,
                               ComponentLoader.ComponentContext context) {
        String caption = element.attributeValue("caption");
        if (isNotEmpty(caption)) {
            facet.setCaption(loadResourceString(context, caption));
        }
    }

    protected void loadDescription(NotificationFacet facet, Element element,
                                   ComponentLoader.ComponentContext context) {
        String description = element.attributeValue("description");
        if (isNotEmpty(description)) {
            facet.setDescription(loadResourceString(context, description));
        }
    }

    protected void loadType(NotificationFacet facet, Element element) {
        String type = element.attributeValue("type");
        if (isNotEmpty(type)) {
            facet.setType(NotificationType.valueOf(type));
        }
    }

    protected void loadDelay(NotificationFacet facet, Element element) {
        String delay = element.attributeValue("delay");
        if (isNotEmpty(delay)) {
            facet.setDelay(Integer.parseInt(delay));
        }
    }

    protected void loadContentMode(NotificationFacet facet, Element element) {
        String contentMode = element.attributeValue("contentMode");
        if (isNotEmpty(contentMode)) {
            facet.setContentMode(ContentMode.valueOf(contentMode));
        }
    }

    protected void loadStyleName(NotificationFacet facet, Element element) {
        String styleName = element.attributeValue("styleName");
        if (isNotEmpty(styleName)) {
            facet.setStyleName(styleName);
        }
    }

    protected void loadPosition(NotificationFacet facet, Element element) {
        String position = element.attributeValue("position");
        if (isNotEmpty(position)) {
            facet.setPosition(Position.valueOf(position));
        }
    }

    protected void loadTarget(NotificationFacet facet, Element element,
                              ComponentLoader.ComponentContext context) {
        String actionTarget = element.attributeValue("onAction");
        String buttonTarget = element.attributeValue("onButton");

        if (isNotEmpty(actionTarget)
                && isNotEmpty(buttonTarget)) {
            throw new GuiDevelopmentException(
                    "Notification Facet should have either action or button subscription", context);
        }

        if (isNotEmpty(actionTarget)) {
            facet.setActionTarget(actionTarget);
        } else if (isNotEmpty(buttonTarget)) {
            facet.setButtonTarget(buttonTarget);
        }
    }

    protected String loadResourceString(ComponentLoader.ComponentContext context, String caption) {
        if (isEmpty(caption)) {
            return caption;
        }

        Class screenClass = context.getFrame()
                .getFrameOwner()
                .getClass();

        return messageTools.loadString(screenClass.getPackage().getName(), caption);
    }
}
