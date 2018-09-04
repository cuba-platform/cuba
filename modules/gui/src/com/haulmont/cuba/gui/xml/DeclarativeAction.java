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

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.components.ActionsHolder;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.screen.FrameOwner;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class DeclarativeAction extends BaseAction {

    private Frame frame;
    private String methodName;

    public DeclarativeAction(String id, String caption, String description, String icon, String enable, String visible,
                             String methodName, @Nullable String shortcut, ActionsHolder holder) {
        super(id, shortcut);
        this.caption = caption;
        this.description = description;
        this.icon = icon;

        setEnabled(enable == null || Boolean.parseBoolean(enable));
        setVisible(visible == null || Boolean.parseBoolean(visible));

        this.methodName = methodName;
        checkActionsHolder(holder);
    }

    public DeclarativeAction(String id, String caption, String description, String icon, boolean enabled, boolean visible,
                             String methodName, ActionsHolder holder) {
        super(id);

        this.caption = caption;
        this.description = description;
        this.icon = icon;

        setEnabled(enabled);
        setVisible(visible);

        this.methodName = methodName;
        checkActionsHolder(holder);
    }

    protected void checkActionsHolder(ActionsHolder holder) {
        if (holder instanceof Frame) {
            frame = (Frame) holder;
        } else if (holder instanceof Component.BelongToFrame) {
            frame = ((Component.BelongToFrame) holder).getFrame();
        } else {
            throw new IllegalStateException(String.format("Component %s can't contain DeclarativeAction", holder));
        }
    }

    @Override
    public void actionPerform(Component component) {
        if (StringUtils.isEmpty(methodName)) {
            return;
        }

        FrameOwner controller = frame.getFrameOwner();
        Method method;
        try {
            method = controller.getClass().getMethod(methodName, Component.class);
        } catch (NoSuchMethodException e) {
            try {
                method = controller.getClass().getMethod(methodName);
            } catch (NoSuchMethodException e1) {
                throw new IllegalStateException(String.format("No suitable methods named %s for action %s", methodName, id));
            }
        }

        try {
            if (method.getParameterCount() == 1) {
                method.invoke(controller, component);
            } else {
                method.invoke(controller);
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception on action handling", e);
        }
    }

    @Override
    public String getCaption() {
        return caption;
    }
}