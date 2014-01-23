/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.components.*;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DeclarativeAction extends AbstractAction {

    private IFrame frame;
    private String methodName;

    public DeclarativeAction(String id, String caption, String description, String icon, String enable, String visible,
                             String methodName, @Nullable String shortcut, Component.ActionsHolder holder) {
        super(id, shortcut);
        this.caption = caption;
        this.description = description;
        this.icon = icon;
        this.enabled = enable == null ? true : Boolean.valueOf(enable);
        this.visible = visible == null ? true : Boolean.valueOf(visible);
        this.methodName = methodName;
        if (holder instanceof IFrame) {
            frame = (IFrame) holder;
        } else if (holder instanceof Component.BelongToFrame) {
            frame = ((Component.BelongToFrame) holder).getFrame();
        } else {
            throw new IllegalStateException("Component " + holder + " can't contain DeclarativeAction");
        }
    }

    public DeclarativeAction(String id, String caption, String description, String icon, boolean enabled, boolean visible,
                             String methodName, Component.ActionsHolder holder) {
        super(id);
        this.caption = caption;
        this.description = description;
        this.icon = icon;
        this.enabled = enabled;
        this.visible = visible;
        this.methodName = methodName;
        if (holder instanceof IFrame) {
            frame = (IFrame) holder;
        } else if (holder instanceof Component.BelongToFrame) {
            frame = ((Component.BelongToFrame) holder).getFrame();
        } else {
            throw new IllegalStateException("Component " + holder + " can't contain DeclarativeAction");
        }
    }

    @Override
    public void actionPerform(Component component) {
        if (StringUtils.isEmpty(methodName)) {
            return;
        }

        Object controller;
        if (frame instanceof WrappedFrame) {
            controller = ((WrappedFrame) frame).getWrapper();
        } else if (frame instanceof WrappedWindow) {
            controller = ((WrappedWindow) frame).getWrapper();
        } else {
            controller = frame;
        }
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
                throw new IllegalStateException("No suitable methods named " + methodName + " for action " + id);
            }
        }
    }
}