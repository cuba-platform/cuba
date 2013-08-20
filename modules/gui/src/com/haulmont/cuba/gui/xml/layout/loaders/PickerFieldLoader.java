/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 14:10:16
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.xml.layout.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.core.global.MetadataProvider;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;

public class PickerFieldLoader extends AbstractFieldLoader {
    public PickerFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final PickerField component = (PickerField) super.loadComponent(factory, element, parent);

        assignFrame(component);

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        final String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            component.setMetaClass(MetadataProvider.getSession().getClass(metaClass));
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
                    throw new DevelopmentException("No action id provided", context.getFullFrameId());
            }
            throw new DevelopmentException("No action id provided", context.getFullFrameId(),
                    Collections.<String, Object>singletonMap("PickerField Id", component.attributeValue("id")));
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
