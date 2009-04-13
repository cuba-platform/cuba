/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:51:22
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.core.global.MessageProvider;
import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.ui.Component;
import com.itmill.toolkit.ui.Layout;

import java.util.*;

public class IFrame extends AbstractPanel implements com.haulmont.cuba.gui.components.IFrame, Layout.AlignmentHandler {

    private String messagePack;
    private DsContext dsContext;
    private com.haulmont.cuba.gui.components.IFrame frame;

    protected Collection<com.haulmont.cuba.gui.components.Component> ownComponents = new HashSet<com.haulmont.cuba.gui.components.Component>();
    protected Map<String, com.haulmont.cuba.gui.components.Component> componentByIds = new HashMap<String, com.haulmont.cuba.gui.components.Component>();

    public IFrame() {
        setLayout(new VerticalLayout());
    }

    public void add(com.haulmont.cuba.gui.components.Component component) {
        final VerticalLayout layout = (VerticalLayout) getLayout();

        final com.itmill.toolkit.ui.Component comp = ComponentsHelper.unwrap(component);
        layout.addComponent(comp);
        layout.setComponentAlignment(comp, ComponentsHelper.convertAlignment(component.getAlignment()));

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }
        ownComponents.add(component);
    }

    public void remove(com.haulmont.cuba.gui.components.Component component) {
        final VerticalLayout layout = (VerticalLayout) getLayout();

        layout.removeComponent(ComponentsHelper.unwrap(component));

        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);
    }

    public <T extends com.haulmont.cuba.gui.components.Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    public <T extends com.haulmont.cuba.gui.components.Component> T getComponent(String id) {
        return ComponentsHelper.<T>getComponent(this, id);
    }

    public Collection<com.haulmont.cuba.gui.components.Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    public Collection<com.haulmont.cuba.gui.components.Component> getComponents() {
        return ComponentsHelper.getComponents(this);
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

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openWindow(windowInfo, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Object item, WindowManager.OpenType openType, Map<String, Object> params) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, params);
    }

    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Object item, WindowManager.OpenType openType) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openWindow(windowInfo, openType);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openLookup(windowInfo, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openLookup(windowInfo, handler, openType);
    }

    public void showMessageDialog(String title, String message, MessageType messageType) {
        App.getInstance().getWindowManager().showMessageDialog(title, message, messageType);
    }

    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        App.getInstance().getWindowManager().showOptionDialog(title, message, messageType, actions);
    }

    public void showNotification(String caption, String description, NotificationType type) {
        getWindow().showNotification(caption, description, ComponentsHelper.convertNotificationType(type));
    }

    public void showNotification(String caption, NotificationType type) {
        getWindow().showNotification(caption, ComponentsHelper.convertNotificationType(type));
    }

    public <A extends com.haulmont.cuba.gui.components.IFrame> A getFrame() {
        return (A) frame;
    }

    public void setFrame(com.haulmont.cuba.gui.components.IFrame frame) {
        this.frame = frame;
    }

    public void setComponentAlignment(Component childComponent, int horizontalAlignment, int verticalAlignment) {
        final AbstractOrderedLayout layout = (AbstractOrderedLayout) getLayout();
        layout.setComponentAlignment(childComponent, horizontalAlignment, verticalAlignment);
    }

    public void setComponentAlignment(Component childComponent, com.itmill.toolkit.ui.Alignment alignment) {
        final AbstractOrderedLayout layout = (AbstractOrderedLayout) getLayout();
        layout.setComponentAlignment(childComponent, alignment);
    }

    public com.itmill.toolkit.ui.Alignment getComponentAlignment(Component childComponent) {
        final AbstractOrderedLayout layout = (AbstractOrderedLayout) getLayout();
        return layout.getComponentAlignment(childComponent);
    }
}
