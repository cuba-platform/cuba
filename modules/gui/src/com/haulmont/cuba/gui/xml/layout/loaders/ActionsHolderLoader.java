/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.actions.ListActionType;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class ActionsHolderLoader extends ComponentLoader {

    protected ActionsHolderLoader(Context context) {
        super(context);
    }

    @Override
    protected Action loadDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        String id = element.attributeValue("id");
        if (StringUtils.isEmpty(id)) {
            throw new GuiDevelopmentException("No action id provided", context.getFullFrameId(),
                    "ActionsHolder ID", actionsHolder.getId());
        }

        if (StringUtils.isBlank(element.attributeValue("invoke"))) {
            // Try to create a standard list action
            for (ListActionType type : ListActionType.values()) {
                if (type.getId().equals(id)) {
                    Action instance = type.createAction((ListComponent) actionsHolder);

                    loadStandardActionProperties(element, instance);

                    loadActionOpenType(instance, element);

                    return instance;
                }
            }
        }

        return super.loadDeclarativeAction(actionsHolder, element);
    }

    protected void loadStandardActionProperties(Element element, Action instance) {
        String enable = element.attributeValue("enable");
        if (StringUtils.isNotEmpty(enable)) {
            instance.setEnabled(Boolean.valueOf(enable));
        }

        String visible = element.attributeValue("visible");
        if (StringUtils.isNotEmpty(visible)) {
            instance.setVisible(Boolean.valueOf(visible));
        }

        String caption = element.attributeValue("caption");
        if (StringUtils.isNotEmpty(caption)) {
            instance.setCaption(loadResourceString(caption));
        }

        String description = element.attributeValue("description");
        if (StringUtils.isNotEmpty(description)) {
            instance.setDescription(loadResourceString(description));
        }

        String icon = element.attributeValue("icon");
        if (StringUtils.isNotEmpty(icon)) {
            instance.setIcon(loadResourceString(icon));
        }
    }

    protected void loadActionOpenType(Action action, Element element) {
        if (action instanceof Action.HasOpenType) {
            String openTypeString = element.attributeValue("openType");
            if (StringUtils.isNotEmpty(openTypeString)) {
                WindowManager.OpenType openType;
                try {
                    openType = WindowManager.OpenType.valueOf(openTypeString);
                } catch (IllegalArgumentException e) {
                    throw new GuiDevelopmentException(
                            "Unknown open type: '" + openTypeString + "' for action: '" + action.getId() + "'",
                            context.getFullFrameId());
                }

                ((Action.HasOpenType) action).setOpenType(openType);
            }
        }
    }
}