/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
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
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final PickerField component = (PickerField) super.loadComponent(factory, element, parent);

        assignFrame(component);

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        final String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            component.setMetaClass(AppBeans.get(Metadata.class).getClassNN(metaClass));
        }

        loadActions(component, element);
        if (component.getActions().isEmpty()) {
            component.addLookupAction();
            component.addClearAction();
        }

        // The code below remains for backward compatibility only!

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

        return component;
    }

    @Override
    protected Action loadDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        String id = element.attributeValue("id");
        if (id == null) {
            Element component = element;
            for (int i = 0; i < 2; i++) {
                if (component.getParent() != null)
                    component = component.getParent();
                else
                    throw new GuiDevelopmentException("No action ID provided", context.getFullFrameId());
            }
            throw new GuiDevelopmentException("No action ID provided", context.getFullFrameId(),
                    "PickerField ID", component.attributeValue("id"));
        }

        if (StringUtils.isBlank(element.attributeValue("invoke"))) {
            // Try to create a standard picker action
            for (PickerField.ActionType type : PickerField.ActionType.values()) {
                if (type.getId().equals(id)) {
                    return type.createAction((PickerField) actionsHolder);
                }
            }
        }

        return super.loadDeclarativeAction(actionsHolder, element);
    }
}
