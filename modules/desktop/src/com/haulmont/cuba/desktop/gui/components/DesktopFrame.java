/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.desktop.DetachedFrame;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;

import javax.annotation.Nullable;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopFrame
        extends DesktopVBox
        implements DetachableFrame, WrappedFrame, Component.HasXmlDescriptor {

    private String messagePack;
    private FrameContext context;
    private DsContext dsContext;
    private IFrame wrapper;
    private Map<String, Component> allComponents = new HashMap<>();

    private boolean detached;
    private DetachedFrame detachedFrame;
    private int componentPosition = -1;
    private HierarchyListener hierarchyListener;

    private List<DetachListener> detachListeners = new ArrayList<>();
    
    private WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

    private DesktopFrameActionsHolder actionsHolder;

    public DesktopFrame() {
        actionsHolder = new DesktopFrameActionsHolder(this, impl);
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
    public void registerComponent(Component component) {
        if (component.getId() != null)
            allComponents.put(component.getId(), component);
    }

    @Nullable
    @Override
    public Component getRegisteredComponent(String id) {
        return allComponents.get(id);
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

    private DesktopWindowManager getWindowManager() {
        return DesktopComponentsHelper.getTopLevelFrame((IFrame) this).getWindowManager();
    }

    @Override
    public DialogParams getDialogParams() {
        return getWindowManager().getDialogParams();
    }

    @Override
    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return getWindowManager().openWindow(windowInfo, openType, params);
    }

    @Override
    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return getWindowManager().openEditor(windowInfo, item, openType, params, parentDs);
    }

    @Override
    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return getWindowManager().openEditor(windowInfo, item, openType, params);
    }

    @Override
    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return getWindowManager().openEditor(windowInfo, item, openType, parentDs);
    }

    @Override
    public <T extends com.haulmont.cuba.gui.components.Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return getWindowManager().openEditor(windowInfo, item, openType);
    }

    @Override
    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return getWindowManager().openWindow(windowInfo, openType);
    }

    @Override
    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return getWindowManager().openLookup(windowInfo, handler, openType, params);
    }

    @Override
    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return getWindowManager().openLookup(windowInfo, handler, openType);
    }

    @Override
    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return getWindowManager().openFrame(wrapper, parent, windowInfo);
    }

    @Override
    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return getWindowManager().openFrame(wrapper, parent, windowInfo, params);
    }

    @Override
    public void detachFrame(String caption) {
        if (isDetached()) {
            throw new RuntimeException("Frame already detached");
        }
        final java.awt.Container parent = impl.getParent();

        detachedFrame = new DetachedFrame(caption, parent);

        for (int i = 0; i < parent.getComponentCount(); i++) {
            if (impl == parent.getComponent(i)) {
                componentPosition = i;
                break;
            }
        }

        hierarchyListener = new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) == HierarchyEvent.DISPLAYABILITY_CHANGED
                        && !parent.isDisplayable()) {
                    parent.removeHierarchyListener(this);
                    attachFrame();
                }
            }
        };

        detachedFrame.setLocationRelativeTo(DesktopComponentsHelper.getTopLevelFrame(impl));
        detachedFrame.setSize(impl.getSize());
        detachedFrame.add(impl);
        detachedFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parent.removeHierarchyListener(hierarchyListener);
                attachFrame();
            }
        });
        parent.revalidate();
        parent.repaint();
        parent.addHierarchyListener(hierarchyListener);
        detachedFrame.setVisible(true);
        detached = true;
        for (DetachListener listener : detachListeners) {
            listener.frameDetached(this);
        }
    }

    @Override
    public void attachFrame() {
        if (!isDetached()) {
            throw new RuntimeException("Frame is already attached");
        }
        java.awt.Container parent = detachedFrame.getParentContainer();
        parent.add(impl, componentPosition);
        detachedFrame.dispose();
        parent.removeHierarchyListener(hierarchyListener);
        detachedFrame = null;
        detached = false;
        parent.revalidate();
        parent.repaint();
        for (DetachListener listener : detachListeners) {
            listener.frameAttached(this);
        }
    }

    @Override
    public void addDetachListener(DetachListener listener) {
        if (!detachListeners.contains(listener)) {
            detachListeners.add(listener);
        }
    }

    @Override
    public void removeDetachListener(DetachListener listener) {
        detachListeners.remove(listener);
    }

    @Override
    public boolean isDetached() {
        return detached;
    }

    @Override
    public void showMessageDialog(String title, String message, MessageType messageType) {
        getWindowManager().showMessageDialog(title, message, messageType);
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        getWindowManager().showOptionDialog(title, message, messageType, actions);
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, java.util.List<Action> actions) {
        getWindowManager().showOptionDialog(title, message, messageType, actions.toArray(new Action[actions.size()]));
    }

    @Override
    public void showNotification(String caption, String description, NotificationType type) {
        getWindowManager().showNotification(caption, description, type);
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        getWindowManager().showWebPage(url, params);
    }

    @Override
    public void showNotification(String caption, NotificationType type) {
        getWindowManager().showNotification(caption, type);
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

    @Override
    public IFrame getWrapper() {
        return wrapper;
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.getFrameComponent(this, id);
    }

    @Override
    public boolean expandsWidth() {
        return true;
    }

    @Override
    public boolean expandsHeight() {
        return false;
    }

    @Override
    public void addAction(final Action action) {
        actionsHolder.addAction(action);
    }

    @Override
    public void removeAction(Action action) {
        actionsHolder.removeAction(action);
    }

    @Override
    public void removeAction(String id) {
        actionsHolder.removeAction(id);
    }

    @Override
    public void removeAllActions() {
        actionsHolder.removeAllActions();
    }

    @Override
    public Collection<Action> getActions() {
        return actionsHolder.getActions();
    }

    @Override
    public Action getAction(String id) {
        return actionsHolder.getAction(id);
    }
}