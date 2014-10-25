/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class PickerFieldLoader extends AbstractFieldLoader {

    public PickerFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    protected void initComponent(Element element, Field field, Component parent) {
        super.initComponent(element, field, parent);

        PickerField component = (PickerField) field;

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        final String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            Metadata metadata = AppBeans.get(Metadata.NAME);
            component.setMetaClass(metadata.getClassNN(metaClass));
        }

        loadActions(component, element);
        if (component.getActions().isEmpty()) {
            component.addLookupAction();
            component.addClearAction();
        }

        // CAUTION The code below remains for backward compatibility only!

        final String lookupScreen = element.attributeValue("lookupScreen");
        if (!StringUtils.isEmpty(lookupScreen)) {
            PickerField.LookupAction action = (PickerField.LookupAction) component.getAction(PickerField.LookupAction.NAME);
            if (action != null)
                action.setLookupScreen(lookupScreen);
        }

        String caption = element.attributeValue("lookupCaption");
        if (caption != null) {
            PickerField.LookupAction action = (PickerField.LookupAction) component.getAction(PickerField.LookupAction.NAME);
            if (action != null)
                action.setCaption(loadResourceString(caption));
        }

        caption = element.attributeValue("clearCaption");
        if (caption != null) {
            PickerField.ClearAction action = (PickerField.ClearAction) component.getAction(PickerField.ClearAction.NAME);
            if (action != null)
                action.setCaption(loadResourceString(caption));
        }

        caption = element.attributeValue("lookupIcon");
        if (caption != null) {
            PickerField.LookupAction action = (PickerField.LookupAction) component.getAction(PickerField.LookupAction.NAME);
            if (action != null)
                action.setIcon(loadResourceString(caption));
        }

        caption = element.attributeValue("clearIcon");
        if (caption != null) {
            PickerField.ClearAction action = (PickerField.ClearAction) component.getAction(PickerField.ClearAction.NAME);
            if (action != null)
                action.setIcon(loadResourceString(caption));
        }
    }

    @Override
    protected Action loadDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        String id = element.attributeValue("id");
        if (id == null) {
            Element component = element;
            for (int i = 0; i < 2; i++) {
                if (component.getParent() != null) {
                    component = component.getParent();
                } else {
                    throw new GuiDevelopmentException("No action ID provided for " + element.getName(), context.getFullFrameId());
                }
            }
            throw new GuiDevelopmentException("No action ID provided for " + element.getName(), context.getFullFrameId(),
                    "PickerField ID", component.attributeValue("id"));
        }

        if (StringUtils.isBlank(element.attributeValue("invoke"))) {
            // Try to create a standard picker action
            for (PickerField.ActionType type : PickerField.ActionType.values()) {
                if (type.getId().equals(id)) {
                    Action action = type.createAction((PickerField) actionsHolder);
                    if (type != PickerField.ActionType.LOOKUP && type != PickerField.ActionType.OPEN) {
                        return action;
                    }

                    String openTypeString = element.attributeValue("openType");
                    if (openTypeString == null) {
                        return action;
                    }

                    WindowManager.OpenType openType;
                    try {
                        openType = WindowManager.OpenType.valueOf(openTypeString);
                    } catch (IllegalArgumentException e) {
                        throw new GuiDevelopmentException(
                                "Unknown open type: '" + openTypeString + "' for action: '" + id + "'", context.getFullFrameId());
                    }

                    if (action instanceof PickerField.LookupAction) {
                        ((PickerField.LookupAction) action).setLookupScreenOpenType(openType);
                    } else if (action instanceof PickerField.OpenAction) {
                        ((PickerField.OpenAction) action).setEditScreenOpenType(openType);
                    }
                    return action;
                }
            }
        }

        return super.loadDeclarativeAction(actionsHolder, element);
    }
}