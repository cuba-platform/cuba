/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:51:22
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.WindowContext;
import com.haulmont.cuba.toolkit.gwt.client.ui.VVerticalActionsLayout;
import com.haulmont.cuba.web.App;
import com.vaadin.event.ActionManager;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Layout;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.*;

@SuppressWarnings("serial")
@ClientWidget(VVerticalActionsLayout.class)
public class WebFrame extends WebVBoxLayout
        implements
            IFrame,
            WrappedFrame,
            com.haulmont.cuba.gui.components.Component.HasXmlDescriptor,
            Layout.AlignmentHandler,
            com.vaadin.event.Action.Container
{
    private String messagePack;
    private WindowContext context;
    private DsContext dsContext;
    private Element element;

    private IFrame wrapper;

    protected Map<String, com.haulmont.cuba.gui.components.Component> allComponents = new HashMap<String, Component>();

    private ActionManager actionManager;

    private WindowConfig windowConfig = AppContext.getBean(WindowConfig.class);

    protected WebFrameActionsHolder actionsHolder = new WebFrameActionsHolder();

    public WebFrame() {
        super();
        addActionHandler(new com.vaadin.event.Action.Handler() {
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                return actionsHolder.getActionImplementations();
            }

            public void handleAction(com.vaadin.event.Action actionImpl, Object sender, Object target) {
                Action action = actionsHolder.getAction(actionImpl);
                if (action != null && action.isEnabled() && action.isVisible()) {
                    action.actionPerform(WebFrame.this);
                }
            }
        });
    }

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

    public IFrame getWrapper() {
        return wrapper;
    }

    public <T extends com.haulmont.cuba.gui.components.Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    public <T extends com.haulmont.cuba.gui.components.Component> T getComponent(String id) {
        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            T result = (T) allComponents.get(id);
            if (result == null && getFrame() != null) {
                result = getFrame().<T>getComponent(id);
            }
            return result;
        } else {
            com.haulmont.cuba.gui.components.Component frame = allComponents.get(elements[0]);
            if (frame != null && frame instanceof Container) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[]{}));
                return (T) ((Container) frame).getComponent(subPath);
            } else
                return null;
        }
    }

    public WindowContext getContext() {
        return context == null ? getFrame().getContext() : context;
    }

    public void setContext(WindowContext ctx) {
        this.context = ctx;
    }

    public DsContext getDsContext() {
        return dsContext == null ? getFrame().getDsContext() : dsContext;
    }

    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    public String getMessagesPack() {
        return messagePack;
    }

    public void setMessagesPack(String name) {
        messagePack = name;
    }

    public String getMessage(String key) {
        if (messagePack == null)
            throw new IllegalStateException("MessagePack is not set");
        return MessageProvider.getMessage(messagePack, key);
    }

    public void registerComponent(com.haulmont.cuba.gui.components.Component component) {
        if (component.getId() != null)
            allComponents.put(component.getId(), component);
    }

    public DialogParams getDialogParams() {
        return App.getInstance().getWindowManager().getDialogParams();
    }

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

    public void validate() throws ValidationException {
        Collection<Component> components = ComponentsHelper.getComponents(this);
        for (Component component : components) {
            if (component instanceof Validatable) {
                ((Validatable) component).validate();
            }
        }
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openWindow(windowInfo, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, params, parentDs);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, parentDs);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openWindow(windowInfo, openType);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openLookup(windowInfo, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openLookup(windowInfo, handler, openType);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openFrame((Window) wrapper, parent, windowInfo);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openFrame((Window) wrapper, parent, windowInfo, params);
    }

    public void showMessageDialog(String title, String message, MessageType messageType) {
        App.getInstance().getWindowManager().showMessageDialog(title, message, messageType);
    }

    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        App.getInstance().getWindowManager().showOptionDialog(title, message, messageType, actions);
    }

    public void showOptionDialog(String title, String message, MessageType messageType, java.util.List<Action> actions) {
        App.getInstance().getWindowManager().showOptionDialog(title, message, messageType, actions.toArray(new Action[actions.size()]));
    }

    public void showNotification(String caption, String description, NotificationType type) {
        getWindow().showNotification(caption, description, WebComponentsHelper.convertNotificationType(type));
    }

    public void showNotification(String caption, NotificationType type) {
        getWindow().showNotification(caption, WebComponentsHelper.convertNotificationType(type));
    }

    public void expandLayout(boolean expandLayout) {
        if (expandLayout) {
            setSizeFull();
        } else {
            setWidth("100%");
            setHeight("-1px");
        }
    }

    public Element getXmlDescriptor() {
        return element;
    }

    public void setXmlDescriptor(Element element) {
        this.element = element;
    }

    public void addActionHandler(com.vaadin.event.Action.Handler actionHandler) {
        getActionManager().addActionHandler(actionHandler);
    }

    public void removeActionHandler(com.vaadin.event.Action.Handler actionHandler) {
        if (actionManager != null) {
            actionManager.removeActionHandler(actionHandler);
        }
    }

    protected ActionManager getActionManager() {
        if (actionManager == null) {
            actionManager = new ActionManager(this);
        }
        return actionManager;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (actionManager != null) {
            actionManager.paintActions(null, target);
        }
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        if (actionManager != null) {
            actionManager.handleActions(variables, this);
        }
    }

    public void addAction(Action action) {
        actionsHolder.addAction(action);
    }

    public void removeAction(Action action) {
        actionsHolder.removeAction(action);
    }

    public Collection<com.haulmont.cuba.gui.components.Action> getActions() {
        return actionsHolder.getActions();
    }

    public com.haulmont.cuba.gui.components.Action getAction(String id) {
        return actionsHolder.getAction(id);
    }
}
