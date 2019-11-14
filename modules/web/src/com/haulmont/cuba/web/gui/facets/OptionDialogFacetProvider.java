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
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.ActionsAwareDialogFacet;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.components.OptionDialogFacet;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.FacetProvider;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.web.gui.components.WebOptionDialogFacet;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.haulmont.cuba.gui.icons.Icons.ICON_NAME_REGEX;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component(OptionDialogFacetProvider.NAME)
public class OptionDialogFacetProvider
        implements FacetProvider<OptionDialogFacet> {

    public static final String NAME = "cuba_OptionDialogFacetProvider";

    @Inject
    protected MessageTools messageTools;
    @Inject
    protected Icons icons;
    @Inject
    protected ThemeConstantsManager themeConstantsManager;

    @Override
    public Class<OptionDialogFacet> getFacetClass() {
        return OptionDialogFacet.class;
    }

    @Override
    public OptionDialogFacet create() {
        return new WebOptionDialogFacet();
    }

    @Override
    public String getFacetTag() {
        return "optionDialog";
    }

    @Override
    public void loadFromXml(OptionDialogFacet facet, Element element,
                            ComponentLoader.ComponentContext context) {
        loadId(facet, element);
        loadCaption(facet, element, context);
        loadMessage(facet, element, context);
        loadType(facet, element);

        loadWidth(facet, element);
        loadHeight(facet, element);

        loadContentMode(facet, element);
        loadMaximized(facet, element);
        loadStyleName(facet, element);

        loadTarget(facet, element, context);

        loadActions(facet, element, context);
    }

    protected void loadId(OptionDialogFacet facet, Element element) {
        String id = element.attributeValue("id");
        if (isNotEmpty(id)) {
            facet.setId(id);
        }
    }

    protected void loadCaption(OptionDialogFacet facet, Element element,
                               ComponentLoader.ComponentContext context) {
        String caption = element.attributeValue("caption");
        if (isNotEmpty(caption)) {
            facet.setCaption(loadResourceString(context, caption));
        }
    }

    protected void loadMessage(OptionDialogFacet facet, Element element,
                               ComponentLoader.ComponentContext context) {
        String message = element.attributeValue("message");
        if (isNotEmpty(message)) {
            facet.setMessage(loadResourceString(context, message));
        }
    }

    protected void loadType(OptionDialogFacet facet, Element element) {
        String type = element.attributeValue("type");
        if (isNotEmpty(type)) {
            facet.setType(Dialogs.MessageType.valueOf(type));
        }
    }

    protected void loadWidth(OptionDialogFacet facet, Element element) {
        String width = element.attributeValue("width");
        if (isNotEmpty(width)) {
            facet.setWidth(width);
        }
    }

    protected void loadHeight(OptionDialogFacet facet, Element element) {
        String height = element.attributeValue("height");
        if (isNotEmpty(height)) {
            facet.setHeight(height);
        }
    }

    protected void loadContentMode(OptionDialogFacet facet, Element element) {
        String contentMode = element.attributeValue("contentMode");
        if (isNotEmpty(contentMode)) {
            facet.setContentMode(ContentMode.valueOf(contentMode));
        }
    }

    protected void loadMaximized(OptionDialogFacet facet, Element element) {
        String maximized = element.attributeValue("maximized");
        if (isNotEmpty(maximized)) {
            facet.setMaximized(Boolean.parseBoolean(maximized));
        }
    }

    protected void loadStyleName(OptionDialogFacet facet, Element element) {
        String styleName = element.attributeValue("styleName");
        if (isNotEmpty(styleName)) {
            facet.setStyleName(styleName);
        }
    }

    protected void loadTarget(OptionDialogFacet facet, Element element,
                              ComponentLoader.ComponentContext context) {
        String actionTarget = element.attributeValue("onAction");
        String buttonTarget = element.attributeValue("onButton");

        if (isNotEmpty(actionTarget) && isNotEmpty(buttonTarget)) {
            throw new GuiDevelopmentException(
                    "Dialog facet should have either action or button target",
                    context);
        }

        if (isNotEmpty(actionTarget)) {
            facet.setActionTarget(actionTarget);
        } else if (isNotEmpty(buttonTarget)) {
            facet.setButtonTarget(buttonTarget);
        }
    }

    protected void loadActions(ActionsAwareDialogFacet facet, Element element,
                               ComponentLoader.ComponentContext context) {
        Element actionsEl = element.element("actions");
        if (actionsEl == null) {
            return;
        }

        List<Element> actionElements = actionsEl.elements("action");

        List<ActionsAwareDialogFacet.DialogAction> actions = new ArrayList<>(actionElements.size());
        for (Element actionElement : actionElements) {
            actions.add(loadAction(actionElement, context));
        }

        facet.setActions(actions);
    }

    protected ActionsAwareDialogFacet.DialogAction loadAction(Element element,
                                                              ComponentLoader.ComponentContext context) {
        String id = element.attributeValue("id");
        String caption = loadResourceString(context, element.attributeValue("caption"));
        String description = loadResourceString(context, element.attributeValue("description"));
        String icon = getIconPath(context, element.attributeValue("icon"));
        boolean primary = Boolean.parseBoolean(element.attributeValue("primary"));

        return new ActionsAwareDialogFacet.DialogAction(id, caption, description, icon, primary);
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

    protected String getIconPath(ComponentLoader.ComponentContext context, String icon) {
        if (icon == null || icon.isEmpty()) {
            return null;
        }

        String iconPath = null;

        if (ICON_NAME_REGEX.matcher(icon).matches()) {
            iconPath = icons.get(icon);
        }

        if (isEmpty(iconPath)) {
            String themeValue = loadThemeString(icon);
            iconPath = loadResourceString(context, themeValue);
        }

        return iconPath;
    }

    protected String loadThemeString(String value) {
        if (value != null
                && value.startsWith(ThemeConstants.PREFIX)) {
            value = themeConstantsManager.getConstants()
                    .get(value.substring(ThemeConstants.PREFIX.length()));
        }
        return value;
    }
}
