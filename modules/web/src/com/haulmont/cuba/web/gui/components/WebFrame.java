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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.ui.CubaOrderedActionsLayout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebFrame extends WebVBoxLayout implements Frame, WrappedFrame {

    protected String messagePack;
    protected FrameContext context;
    protected DsContext dsContext;

    protected Frame wrapper;

    protected Map<String, com.haulmont.cuba.gui.components.Component> allComponents = new HashMap<>();

    protected WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

    protected WebFrameActionsHolder actionsHolder = new WebFrameActionsHolder();

    public WebFrame() {
        ((CubaOrderedActionsLayout)component).addActionHandler(new com.vaadin.event.Action.Handler() {
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
    }

    @Override
    public Frame wrapBy(Class<?> aClass) {
        try {
            // First try to find an old-style constructor with Frame parameter
            Constructor<?> constructor = null;
            try {
                constructor = aClass.getConstructor(Frame.class);
            } catch (NoSuchMethodException e) {
                //
            }
            if (constructor != null) {
                wrapper = (Frame) constructor.newInstance(this);
            } else {
                // If not found, get the default constructor
                constructor = aClass.getConstructor();
                wrapper = (Frame) constructor.newInstance();
                ((AbstractFrame) wrapper).setWrappedFrame(this);
            }
            return wrapper;
        } catch (Throwable e) {
            throw new RuntimeException("Unable to init frame controller", e);
        }
    }

    @Override
    public Frame getWrapper() {
        return wrapper;
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getFrameComponent(this, id);
    }

    @Override
    public FrameContext getContext() {
        return context == null ? getFrame().getContext() : context;
    }

    @Override
    public void setContext(FrameContext ctx) {
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
    protected void attachToFrame(Component childComponent) {
        registerComponent(childComponent);
    }

    @Override
    public void registerComponent(Component component) {
        if (component.getId() != null) {
            allComponents.put(component.getId(), component);
        }
    }

    @Override
    public void unregisterComponent(Component component) {
        if (component.getId() != null) {
            allComponents.remove(component.getId());
        }
    }

    @Nullable
    @Override
    public Component getRegisteredComponent(String id) {
        return allComponents.get(id);
    }

    @Deprecated
    @Override
    public DialogParams getDialogParams() {
        return App.getInstance().getWindowManager().getDialogParams();
    }

    @Override
    public boolean isValid() {
        Collection<Component> components = ComponentsHelper.getComponents(this);
        for (Component component : components) {
            if (component instanceof Validatable) {
                if (!((Validatable) component).isValid())
                    return false;
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
    public Window openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openWindow(windowInfo, openType, params);
    }

    @Override
    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType) {
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        return App.getInstance().getWindowManager().openEditor(editorScreen, item, openType);
    }

    @Override
    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        return App.getInstance().getWindowManager().openEditor(editorScreen, item, openType, params);
    }

    @Override
    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        return App.getInstance().getWindowManager().openEditor(editorScreen, item, openType, params, parentDs);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openEditor(windowInfo, item, openType, params, parentDs);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openEditor(windowInfo, item, openType, params);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openEditor(windowInfo, item, openType, parentDs);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openEditor(windowInfo, item, openType);
    }

    @Override
    public Window openWindow(String windowAlias, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openWindow(windowInfo, openType);
    }

    @Override
    public Window.Lookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowInfo lookupScreen = windowConfig.getLookupScreen(entityClass);
        return App.getInstance().getWindowManager().openLookup(lookupScreen, handler, openType);
    }

    @Override
    public Window.Lookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo lookupScreen = windowConfig.getLookupScreen(entityClass);
        return App.getInstance().getWindowManager().openLookup(lookupScreen, handler, openType, params);
    }

    @Override
    public Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openLookup(windowInfo, handler, openType, params);
    }

    @Override
    public Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openLookup(windowInfo, handler, openType);
    }

    @Override
    public Frame openFrame(Component parent, String windowAlias) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openFrame(wrapper, parent, windowInfo);
    }

    @Override
    public Frame openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().openFrame(wrapper, parent, windowInfo, params);
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
    public void showOptionDialog(String title, String message, MessageType messageType, List<Action> actions) {
        App.getInstance().getWindowManager().showOptionDialog(title, message, messageType, actions.toArray(new Action[actions.size()]));
    }

    @Override
    public void showNotification(String caption, String description, NotificationType type) {
        App.getInstance().getWindowManager().showNotification(caption, description, type);
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        App.getInstance().getWindowManager().showWebPage(url, params);
    }

    @Override
    public void showNotification(String caption, NotificationType type) {
        App.getInstance().getWindowManager().showNotification(caption, type);
    }

    @Override
    public void addAction(Action action) {
        checkNotNullArgument(action, "action must be non null");

        actionsHolder.addAction(action);
    }

    @Override
    public void addAction(Action action, int index) {
        checkNotNullArgument(action, "action must be non null");

        actionsHolder.addAction(action, index);
    }

    @Override
    public void removeAction(@Nullable Action action) {
        actionsHolder.removeAction(action);
    }

    @Override
    public void removeAction(@Nullable String id) {
        actionsHolder.removeAction(id);
    }

    @Override
    public void removeAllActions() {
        actionsHolder.removeAllActions();
    }

    @Override
    public Collection<com.haulmont.cuba.gui.components.Action> getActions() {
        return actionsHolder.getActions();
    }

    @Override
    @Nullable
    public com.haulmont.cuba.gui.components.Action getAction(String id) {
        return actionsHolder.getAction(id);
    }

    @Nonnull
    @Override
    public Action getActionNN(String id) {
        Action action = getAction(id);
        if (action == null) {
            throw new IllegalStateException("Unable to find action with id " + id);
        }
        return action;
    }
}