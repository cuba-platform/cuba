/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:51:22
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.WindowContext;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import java.util.*;
import java.lang.reflect.Constructor;
import java.util.List;

import org.dom4j.Element;

public class WebFrame extends WebAbstractPanel
        implements
            IFrame,
            WrappedFrame,
            com.haulmont.cuba.gui.components.Component.HasXmlDescriptor,
            Layout.AlignmentHandler
{
    private String messagePack;
    private WindowContext context;
    private DsContext dsContext;
    private IFrame frame;
    private Element element;

    private IFrame wrapper;

    protected Collection<com.haulmont.cuba.gui.components.Component> ownComponents = new HashSet<com.haulmont.cuba.gui.components.Component>();
    protected Map<String, com.haulmont.cuba.gui.components.Component> componentByIds = new HashMap<String, com.haulmont.cuba.gui.components.Component>();
    
    protected Map<String, com.haulmont.cuba.gui.components.Component> allComponents = 
            new WeakHashMap<String, com.haulmont.cuba.gui.components.Component>();

    public WebFrame() {
        setContent(new VerticalLayout());
    }

    public IFrame wrapBy(Class<? extends IFrame> aClass) {
        try {
            Constructor<?> constructor = aClass.getConstructor(IFrame.class);

            wrapper = (IFrame) constructor.newInstance(this);
            return wrapper;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public IFrame getWrapper() {
        return wrapper;
    }

    public void add(com.haulmont.cuba.gui.components.Component component) {
        final VerticalLayout layout = (VerticalLayout) getContent();

        final com.vaadin.ui.Component comp = WebComponentsHelper.getComposition(component);
        layout.addComponent(comp);
        layout.setComponentAlignment(comp, WebComponentsHelper.convertAlignment(component.getAlignment()));

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }
        ownComponents.add(component);
    }

    public void remove(com.haulmont.cuba.gui.components.Component component) {
        final VerticalLayout layout = (VerticalLayout) getContent();

        layout.removeComponent(WebComponentsHelper.getComposition(component));

        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);
    }

    public <T extends com.haulmont.cuba.gui.components.Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    public <T extends com.haulmont.cuba.gui.components.Component> T getComponent(String id) {
        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            T result = (T) allComponents.get(id);
            if (result == null && frame != null) {
                result = frame.<T>getComponent(id);
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

    public Collection<com.haulmont.cuba.gui.components.Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    public Collection<com.haulmont.cuba.gui.components.Component> getComponents() {
        return WebComponentsHelper.getComponents(this);
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

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openWindow(windowInfo, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, params, parentDs);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, parentDs);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openWindow(windowInfo, openType);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openLookup(windowInfo, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openLookup(windowInfo, handler, openType);
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

    public <A extends com.haulmont.cuba.gui.components.IFrame> A getFrame() {
        return (A) frame;
    }

    public void setFrame(com.haulmont.cuba.gui.components.IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
    }

    public void setComponentAlignment(Component childComponent, int horizontalAlignment, int verticalAlignment) {
        final AbstractOrderedLayout layout = (AbstractOrderedLayout) getContent();
        layout.setComponentAlignment(childComponent, horizontalAlignment, verticalAlignment);
    }

    public void setComponentAlignment(Component childComponent, com.vaadin.ui.Alignment alignment) {
        final AbstractOrderedLayout layout = (AbstractOrderedLayout) getContent();
        layout.setComponentAlignment(childComponent, alignment);
    }

    public com.vaadin.ui.Alignment getComponentAlignment(Component childComponent) {
        final AbstractOrderedLayout layout = (AbstractOrderedLayout) getContent();
        return layout.getComponentAlignment(childComponent);
    }

    public void expandLayout(boolean expandLayout) {
        if (expandLayout) {
            getContent().setSizeFull();
        } else {
            getContent().setWidth("100%");
            getContent().setHeight("-1px");
        }
    }

    public Element getXmlDescriptor() {
        return element;
    }

    public void setXmlDescriptor(Element element) {
        this.element = element;
    }
}
