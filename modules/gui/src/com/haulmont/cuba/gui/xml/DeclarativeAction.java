/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DeclarativeAction extends BaseAction {

    private Frame frame;
    private String methodName;

    public DeclarativeAction(String id, String caption, String description, String icon, String enable, String visible,
                             String methodName, @Nullable String shortcut, Component.ActionsHolder holder) {
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
                             String methodName, Component.ActionsHolder holder) {
        super(id);

        this.caption = caption;
        this.description = description;
        this.icon = icon;

        setEnabled(enabled);
        setVisible(visible);

        this.methodName = methodName;
        checkActionsHolder(holder);
    }

    protected void checkActionsHolder(Component.ActionsHolder holder) {
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

        Object controller = ComponentsHelper.getFrameController(frame);
        Method method;
        try {
            method = controller.getClass().getMethod(methodName, Component.class);
            try {
                method.invoke(controller, component);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchMethodException e) {
            try {
                method = controller.getClass().getMethod(methodName);
                try {
                    method.invoke(controller);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            } catch (NoSuchMethodException e1) {
                throw new IllegalStateException(String.format("No suitable methods named %s for action %s", methodName, id));
            }
        }
    }

    @Override
    public String getCaption() {
        return caption;
    }
}