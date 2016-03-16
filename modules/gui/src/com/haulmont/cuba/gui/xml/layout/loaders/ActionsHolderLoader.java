/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.ListActionType;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 */
public abstract class ActionsHolderLoader<T extends Component.ActionsHolder> extends AbstractComponentLoader<T> {

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

                    loadStandardActionProperties(instance, element);

                    loadActionOpenType(instance, element);

                    loadConstraint(instance, element);

                    return instance;
                }
            }
        }

        return super.loadDeclarativeAction(actionsHolder, element);
    }

    protected void loadStandardActionProperties(Action instance, Element element) {
        String enable = element.attributeValue("enable");
        if (StringUtils.isNotEmpty(enable)) {
            instance.setEnabled(Boolean.parseBoolean(enable));
        }

        String visible = element.attributeValue("visible");
        if (StringUtils.isNotEmpty(visible)) {
            instance.setVisible(Boolean.parseBoolean(visible));
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

    protected void loadConstraint(Action action, Element element) {
        if (action instanceof ItemTrackingAction) {
            ItemTrackingAction itemTrackingAction = (ItemTrackingAction) action;
            ConstraintOperationType operationType
                    = ConstraintOperationType.fromId(element.attributeValue("constraintOperationType"));
            String constraintCode = element.attributeValue("constraintCode");
            itemTrackingAction.setConstraintOperationType(operationType);
            itemTrackingAction.setConstraintCode(constraintCode);
        }
    }
}