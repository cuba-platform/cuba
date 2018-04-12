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
import com.haulmont.cuba.gui.events.sys.UiEventsMulticaster;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebWindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebFrame extends WebVBoxLayout implements Frame, WrappedFrame {

    private static final Logger log = LoggerFactory.getLogger(WebFrame.class);

    protected String messagePack;
    protected FrameContext context;
    protected DsContext dsContext;

    protected Frame wrapper;

    protected Map<String, com.haulmont.cuba.gui.components.Component> allComponents = new HashMap<>();

    protected WebFrameActionsHolder actionsHolder = new WebFrameActionsHolder();

    public WebFrame() {
        component.addActionHandler(new com.vaadin.event.Action.Handler() {
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

        setupEventListeners();
    }

    protected void setupEventListeners() {
        component.addAttachListener(event -> enableEventListeners());
        component.addDetachListener(event -> disableEventListeners());
    }

    protected void disableEventListeners() {
        Frame wrapper = getWrapper();
        if (wrapper != null) {
            List<ApplicationListener> uiEventListeners = ((AbstractFrame) wrapper).getUiEventListeners();
            if (uiEventListeners != null) {
                for (ApplicationListener listener : uiEventListeners) {
                    UiEventsMulticaster multicaster = AppUI.getCurrent().getUiEventsMulticaster();
                    multicaster.removeApplicationListener(listener);
                }
            }
        }
    }

    protected void enableEventListeners() {
        Frame wrapper = getWrapper();
        if (wrapper != null) {
            List<ApplicationListener> uiEventListeners = ((AbstractFrame) wrapper).getUiEventListeners();
            if (uiEventListeners != null) {
                for (ApplicationListener listener : uiEventListeners) {
                    UiEventsMulticaster multicaster = AppUI.getCurrent().getUiEventsMulticaster();
                    multicaster.addApplicationListener(listener);
                }
            }
        }
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
                Validatable validatable = (Validatable) component;
                if (validatable.isValidateOnCommit() && !validatable.isValid())
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
                Validatable validatable = (Validatable) component;
                if (validatable.isValidateOnCommit()) {
                    validatable.validate();
                }
            }
        }
    }

    @Override
    public boolean validate(List<Validatable> fields) {
        ValidationErrors errors = new ValidationErrors();

        for (Validatable field : fields) {
            try {
                field.validate();
            } catch (ValidationException e) {
                if (log.isTraceEnabled())
                    log.trace("Validation failed", e);
                else if (log.isDebugEnabled())
                    log.debug("Validation failed: " + e);

                ComponentsHelper.fillErrorMessages(field, e, errors);
            }
        }

        return handleValidationErrors(errors);
    }

    @Override
    public boolean validateAll() {
        ValidationErrors errors = new ValidationErrors();

        Collection<Component> components = ComponentsHelper.getComponents(this);
        for (Component component : components) {
            if (component instanceof Validatable) {
                Validatable validatable = (Validatable) component;
                if (validatable.isValidateOnCommit()) {
                    try {
                        validatable.validate();
                    } catch (ValidationException e) {
                        if (log.isTraceEnabled())
                            log.trace("Validation failed", e);
                        else if (log.isDebugEnabled())
                            log.debug("Validation failed: " + e);

                        ComponentsHelper.fillErrorMessages(validatable, e, errors);
                    }
                }
            }
        }

        return handleValidationErrors(errors);
    }

    protected boolean handleValidationErrors(ValidationErrors errors) {
        if (errors.isEmpty())
            return true;

        Window window = ComponentsHelper.getWindow(wrapper);
        if (window instanceof AbstractFrame) {
            ((AbstractFrame) window).showValidationErrors(errors);
        }

        WebComponentsHelper.focusProblemComponent(errors);

        return false;
    }

    @Override
    public Window openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openWindow(windowInfo, openType, params);
    }

    @Override
    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openEditor(editorScreen, item, openType);
    }

    @Override
    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openEditor(editorScreen, item, openType, params);
    }

    @Override
    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openEditor(editorScreen, item, openType, params, parentDs);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openEditor(windowInfo, item, openType, params, parentDs);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openEditor(windowInfo, item, openType, params);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openEditor(windowInfo, item, openType, parentDs);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openEditor(windowInfo, item, openType);
    }

    @Override
    public Window openWindow(String windowAlias, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openWindow(windowInfo, openType);
    }

    @Override
    public Window.Lookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo lookupScreen = windowConfig.getLookupScreen(entityClass);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openLookup(lookupScreen, handler, openType);
    }

    @Override
    public Window.Lookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo lookupScreen = windowConfig.getLookupScreen(entityClass);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openLookup(lookupScreen, handler, openType, params);
    }

    @Override
    public Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openLookup(windowInfo, handler, openType, params);
    }

    @Override
    public Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        WebWindowManager wm = App.getInstance().getWindowManager();
        return wm.openLookup(windowInfo, handler, openType);
    }

    @Override
    public Frame openFrame(Component parent, String windowAlias) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        WebWindowManager wm = App.getInstance().getWindowManager();
        Frame wrappedFrame = ((Frame.Wrapper) wrapper).getWrappedFrame();
        return wm.openFrame(wrappedFrame, parent, windowInfo);
    }

    @Override
    public Frame openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        WebWindowManager wm = App.getInstance().getWindowManager();
        Frame wrappedFrame = ((Frame.Wrapper) wrapper).getWrappedFrame();
        return wm.openFrame(wrappedFrame, parent, windowInfo, params);
    }

    @Override
    public void showMessageDialog(String title, String message, MessageType messageType) {
        WebWindowManager wm = App.getInstance().getWindowManager();
        wm.showMessageDialog(title, message, messageType);
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        WebWindowManager wm = App.getInstance().getWindowManager();
        wm.showOptionDialog(title, message, messageType, actions);
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, List<Action> actions) {
        WebWindowManager wm = App.getInstance().getWindowManager();
        wm.showOptionDialog(title, message, messageType, actions.toArray(new Action[actions.size()]));
    }

    @Override
    public void showNotification(String caption) {
        WebWindowManager wm = App.getInstance().getWindowManager();
        wm.showNotification(caption);
    }

    @Override
    public void showNotification(String caption, String description, NotificationType type) {
        WebWindowManager wm = App.getInstance().getWindowManager();
        wm.showNotification(caption, description, type);
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        WebWindowManager wm = App.getInstance().getWindowManager();
        wm.showWebPage(url, params);
    }

    @Override
    public void showNotification(String caption, NotificationType type) {
        WebWindowManager wm = App.getInstance().getWindowManager();
        wm.showNotification(caption, type);
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
}