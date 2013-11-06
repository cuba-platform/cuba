/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.ui.JavaScriptHost;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class WebFrame extends WebAbstractBox implements IFrame, WrappedFrame {

    protected String messagePack;
    protected WindowContext context;
    protected DsContext dsContext;
    protected Element element;

    protected IFrame wrapper;

    protected Map<String, com.haulmont.cuba.gui.components.Component> allComponents = new HashMap<>();

    protected WindowConfig windowConfig = AppBeans.get(WindowConfig.class);

    protected WebFrameActionsHolder actionsHolder = new WebFrameActionsHolder();

    public WebFrame() {
        VerticalActionsLayout vComponent = new VerticalActionsLayout();
        vComponent.setWidth(100, UNITS_PERCENTAGE);

        vComponent.addActionHandler(new com.vaadin.event.Action.Handler() {
            @Override
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                return actionsHolder.getActionImplementations();
            }

            @Override
            public void handleAction(com.vaadin.event.Action actionImpl, Object sender, Object target) {
                Action action = actionsHolder.getAction(actionImpl);
                if (action != null && action.isEnabled() && action.isVisible()) {
                    action.actionPerform(WebFrame.this);
                }
            }
        });

        component = vComponent;
    }

    @Override
    public IFrame wrapBy(Class<? extends IFrame> aClass) {
        try {
            // First try to find an old-style constructor with IFrame parameter
            Constructor<?> constructor = null;
            try {
                constructor = aClass.getConstructor(IFrame.class);
            } catch (NoSuchMethodException e) {
                //
            }
            if (constructor != null) {
                wrapper = (IFrame) constructor.newInstance(this);
            } else {
                // If not found, get the default constructor
                constructor = aClass.getConstructor();
                wrapper = (IFrame) constructor.newInstance();
                ((AbstractFrame) wrapper).setWrappedFrame(this);
            }
            return wrapper;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IFrame getWrapper() {
        return wrapper;
    }

    @Override
    public <T extends com.haulmont.cuba.gui.components.Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    @Override
    public <T extends com.haulmont.cuba.gui.components.Component> T getComponent(String id) {
        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            T result = (T) allComponents.get(id);
            if (result == null && getFrame() != null) {
                result = getFrame().getComponent(id);
            }
            return result;
        } else {
            com.haulmont.cuba.gui.components.Component frame = allComponents.get(elements[0]);
            if (frame != null && frame instanceof Container) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[]{}));
                return (T) ((Container) frame).getComponent(subPath);
            } else {
                return null;
            }
        }
    }

    @Override
    public WindowContext getContext() {
        return context == null ? getFrame().getContext() : context;
    }

    @Override
    public void setContext(WindowContext ctx) {
        this.context = ctx;
    }

    @Override
    public DsContext getDsContext() {
        return dsContext == null ? getFrame().getDsContext() : dsContext;
    }

    @Override
    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    @Override
    public String getMessagesPack() {
        return messagePack;
    }

    @Override
    public void setMessagesPack(String name) {
        messagePack = name;
    }

    @Override
    public void registerComponent(com.haulmont.cuba.gui.components.Component component) {
        if (component.getId() != null) {
            allComponents.put(component.getId(), component);
        }
    }

    @Override
    public DialogParams getDialogParams() {
        return App.getInstance().getWindowManager().getDialogParams();
    }

    @Override
    public boolean isValid() {
        Collection<Component> components = ComponentsHelper.getComponents(this);
        for (Component component : components) {
            if (component instanceof Validatable) {
                if (!((Validatable) component).isValid()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void validate() throws ValidationException {
        Collection<Component> components = ComponentsHelper.getComponents(this);
        for (Component component : components) {
            if (component instanceof Validatable) {
                ((Validatable) component).validate();
            }
        }
    }

    @Override
    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openWindow(windowInfo, openType, params);
    }

    @Override
    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openEditor(windowInfo, item, openType, params, parentDs);
    }

    @Override
    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openEditor(windowInfo, item, openType, params);
    }

    @Override
    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openEditor(windowInfo, item, openType, parentDs);
    }

    @Override
    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openEditor(windowInfo, item, openType);
    }

    @Override
    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openWindow(windowInfo, openType);
    }

    @Override
    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openLookup(windowInfo, handler, openType, params);
    }

    @Override
    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openLookup(windowInfo, handler, openType);
    }

    @Override
    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openFrame((Window) wrapper, parent, windowInfo);
    }

    @Override
    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openFrame((Window) wrapper, parent, windowInfo, params);
    }

    @Override
    public void showMessageDialog(String title, String message, MessageType messageType) {
        App.getInstance().getWindowManager().showMessageDialog(title, message, messageType);
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        App.getInstance().getWindowManager().showOptionDialog(title, message, messageType, actions);
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, java.util.List<Action> actions) {
        App.getInstance().getWindowManager().showOptionDialog(title, message, messageType, actions.toArray(new Action[actions.size()]));
    }

    @Override
    public void showNotification(String caption, String description, NotificationType type) {
        App.getInstance().getWindowManager().showNotification(caption, description, type);
    }
    @Override
    public void showNotification(String caption, NotificationType type) {
        App.getInstance().getWindowManager().showNotification(caption, type);
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        App.getInstance().getWindowManager().showWebPage(url, params);
    }

    @Override
    public Element getXmlDescriptor() {
        return element;
    }

    @Override
    public void setXmlDescriptor(Element element) {
        this.element = element;
    }

    @Override
    public void addAction(Action action) {
        actionsHolder.addAction(action);
    }

    @Override
    public void removeAction(Action action) {
        actionsHolder.removeAction(action);
    }

    @Override
    public Collection<com.haulmont.cuba.gui.components.Action> getActions() {
        return actionsHolder.getActions();
    }

    @Override
    public com.haulmont.cuba.gui.components.Action getAction(String id) {
        return actionsHolder.getAction(id);
    }
}