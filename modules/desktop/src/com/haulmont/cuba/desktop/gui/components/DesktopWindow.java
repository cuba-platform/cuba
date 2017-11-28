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

package com.haulmont.cuba.desktop.gui.components;

import com.google.common.collect.Iterables;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.events.EventRouter;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.desktop.TopLevelFrame;
import com.haulmont.cuba.desktop.gui.data.ComponentSize;
import com.haulmont.cuba.desktop.gui.data.DesktopContainerHelper;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.desktop.sys.DialogWindow;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigLayoutHelper;
import com.haulmont.cuba.desktop.sys.vcl.JTabbedPaneExt;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.LookupComponent.LookupSelectionChangeNotifier;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.events.sys.UiEventsMulticaster;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.logging.UserActionsLogger;
import com.haulmont.cuba.gui.settings.Settings;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class DesktopWindow implements Window, Component.Disposable,
        Component.Wrapper, Component.HasXmlDescriptor, Component.SecuredActionsHolder, WrappedWindow, DesktopContainer {

    protected static final Logger log = LoggerFactory.getLogger(DesktopWindow.class);
    protected Logger userActionsLog = LoggerFactory.getLogger(UserActionsLogger.class);

    protected boolean disposed = false;
    protected boolean responsive = false;

    protected BoxLayoutAdapter layoutAdapter;
    protected JPanel panel;

    protected String id;

    protected Map<String, Component> componentByIds = new HashMap<>();
    protected Collection<Component> ownComponents = new LinkedHashSet<>();

    protected Map<String, Component> allComponents = new HashMap<>();

    protected DsContext dsContext;
    protected WindowContext context;
    protected String messagePack;
    protected String focusComponentId;
    protected Element xmlDescriptor;
    protected String caption;
    protected String description;
    protected Component expandedComponent;
    protected Map<Component, ComponentCaption> captions = new HashMap<>();
    protected Map<Component, Pair<JPanel, BoxLayoutAdapter>> wrappers = new HashMap<>();

    protected WindowDelegate delegate;

    protected DesktopFrameActionsHolder actionsHolder;
    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    protected List<CloseListener> listeners = new ArrayList<>();

    protected boolean forceClose;
    protected Runnable doAfterClose;

    protected List<Timer> timers = new ArrayList<>();

    protected DesktopWindowManager windowManager;

    protected Configuration configuration = AppBeans.get(Configuration.NAME);
    protected Messages messages = AppBeans.get(Messages.NAME);
    protected Icons icons = AppBeans.get(Icons.class);

    protected ComponentSize widthSize;
    protected ComponentSize heightSize;

    protected boolean scheduledRepaint = false;
    protected DialogOptions dialogOptions = new DesktopDialogOptions();
    protected String icon;
    protected List<String> styles;

    private EventRouter eventRouter;
    protected ContentSwitchMode contentSwitchMode = ContentSwitchMode.DEFAULT;

    protected boolean isAttachedToRoot = false;

    public DesktopWindow() {
        initLayout();
        delegate = createDelegate();
        actionsHolder = new DesktopFrameActionsHolder(this, panel);

        setWidth("100%");

        panel.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                SwingUtilities.invokeLater(() -> {
                    if (!isAttachedToRoot) {
                        if (SwingUtilities.getRoot(event.getComponent()) != null) {
                            enableEventListeners();
                            isAttachedToRoot = true;
                        }
                    }
                });
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                SwingUtilities.invokeLater(() -> {
                    if (isAttachedToRoot) {
                        if (SwingUtilities.getRoot(event.getComponent()) == null) {
                            disableEventListeners();
                            isAttachedToRoot = false;
                        }
                    }
                });
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                // do nothing
            }
        });
    }

    protected void disableEventListeners() {
        Frame wrapper = delegate.getWrapper();
        if (wrapper != null) {
            List<ApplicationListener> uiEventListeners = ((AbstractFrame) wrapper).getUiEventListeners();
            if (uiEventListeners != null) {
                for (ApplicationListener listener : uiEventListeners) {
                    UiEventsMulticaster multicaster = App.getInstance().getUiEventsMulticaster();
                    multicaster.removeApplicationListener(listener);
                }
            }
        }
    }

    protected void enableEventListeners() {
        Frame wrapper = delegate.getWrapper();
        if (wrapper != null) {
            List<ApplicationListener> uiEventListeners = ((AbstractFrame) wrapper).getUiEventListeners();
            if (uiEventListeners != null) {
                for (ApplicationListener listener : uiEventListeners) {
                    UiEventsMulticaster multicaster = App.getInstance().getUiEventsMulticaster();
                    multicaster.addApplicationListener(listener);
                }
            }
        }
    }

    protected void initLayout() {
        panel = new JPanel();
        layoutAdapter = BoxLayoutAdapter.create(panel);
        layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.Y);
        layoutAdapter.setMargin(true);
    }

    protected WindowDelegate createDelegate() {
        return new WindowDelegate(this);
    }

    @Nullable
    protected DialogWindow asDialogWindow() {
        java.awt.Component parent = getContainer();
        while (parent != null) {
            if (parent instanceof DialogWindow) {
                return (DialogWindow) parent;
            }

            parent = parent.getParent();
        }
        return null;
    }

    @Override
    public Element getXmlDescriptor() {
        return xmlDescriptor;
    }

    @Override
    public void setXmlDescriptor(Element element) {
        xmlDescriptor = element;
    }

    @Override
    public void addListener(CloseListener listener) {
        addCloseListener(listener);
    }

    @Override
    public void removeListener(CloseListener listener) {
        removeCloseListener(listener);
    }

    @Override
    public void addCloseListener(CloseListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeCloseListener(CloseListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void addCloseWithCommitListener(CloseWithCommitListener listener) {
        if (listeners == null) {
            listeners = new LinkedList<>();
        }

        CloseListenerAdapter adapter = new CloseListenerAdapter(listener);
        if (!listeners.contains(adapter)) {
            listeners.add(adapter);
        }
    }

    @Override
    public void removeCloseWithCommitListener(CloseWithCommitListener listener) {
        if (listeners != null) {
            listeners.remove(new CloseListenerAdapter(listener));
        }
    }

    /**
     * Use EventRouter for listeners instead of fields with listeners List.
     *
     * @return lazily initialized {@link EventRouter} instance.
     * @see EventRouter
     */
    protected EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }

    @Override
    public void addBeforeCloseWithShortcutListener(BeforeCloseWithShortcutListener listener) {
        getEventRouter().addListener(BeforeCloseWithShortcutListener.class, listener);
    }

    @Override
    public void removeBeforeCloseWithShortcutListener(BeforeCloseWithShortcutListener listener) {
        getEventRouter().removeListener(BeforeCloseWithShortcutListener.class, listener);
    }

    public void fireBeforeCloseWithShortcut(BeforeCloseWithShortcutEvent event) {
        getEventRouter().fireEvent(BeforeCloseWithShortcutListener.class,
                BeforeCloseWithShortcutListener::beforeCloseWithShortcut, event);
    }

    @Override
    public void addBeforeCloseWithCloseButtonListener(BeforeCloseWithCloseButtonListener listener) {
        getEventRouter().addListener(BeforeCloseWithCloseButtonListener.class, listener);
    }

    @Override
    public void removeBeforeCloseWithCloseButtonListener(BeforeCloseWithCloseButtonListener listener) {
        getEventRouter().removeListener(BeforeCloseWithCloseButtonListener.class, listener);
    }

    public void fireBeforeCloseWithCloseButton(BeforeCloseWithCloseButtonEvent event) {
        getEventRouter().fireEvent(BeforeCloseWithCloseButtonListener.class,
                BeforeCloseWithCloseButtonListener::beforeCloseWithCloseButton, event);
    }

    @Override
    public void applySettings(Settings settings) {
        delegate.applySettings(settings);
    }

    @Override
    public void saveSettings() {
        delegate.saveSettings();
    }

    @Override
    public void deleteSettings() {
        delegate.deleteSettings();
    }

    @Override
    public void setFocusComponent(String componentId) {
        this.focusComponentId = componentId;
        if (componentId != null) {
            Component component = getComponent(componentId);
            if (component != null) {
                component.requestFocus();
            } else {
                log.error("Can't find focus component: " + componentId);
            }
        } else {
            findAndFocusChildComponent();
        }
    }

    public boolean findAndFocusChildComponent() {
        final java.awt.Component focusComponent = getComponentToFocus(getContainer());
        if (focusComponent != null) {
            SwingUtilities.invokeLater(() -> {
                focusComponent.requestFocus();
            });

            return true;
        }
        return false;
    }

    protected java.awt.Component getComponentToFocus(java.awt.Container component) {
        if (component.isFocusable() && component.isEnabled()
                && DesktopComponentsHelper.isRecursivelyVisible(component)) {
            if (component instanceof JComboBox
                    || component instanceof JCheckBox
                    || component instanceof JTable
                    || component instanceof JTree) {
                return component;
            } else if (component instanceof JTextComponent && ((JTextComponent) component).isEditable()) {
                return component;
            }
        }
        for (java.awt.Component child : component.getComponents()) {
            if (child instanceof JTabbedPane) {
                // #PL-3176
                // we don't know about selected tab after request
                // may be focused component lays on not selected tab
                continue;
            }
            if (child instanceof java.awt.Container) {
                java.awt.Component result = getComponentToFocus((java.awt.Container) child);
                if (result != null) {
                    return result;
                }
            } else {
                return child;
            }
        }
        return null;
    }

    @Override
    public String getFocusComponent() {
        return focusComponentId;
    }

    @Override
    public Settings getSettings() {
        return delegate.getSettings();
    }

    @Override
    public boolean isResponsive() {
        return responsive;
    }

    @Override
    public void setResponsive(boolean responsive) {
        this.responsive = responsive;
    }

    @Override
    public boolean close(final String actionId) {
        if (!forceClose) {
            if (!delegate.preClose(actionId))
                return false;
        }

        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

        if (!forceClose && isModified()) {
            final Committable committable = (getWrapper() instanceof Committable) ? (Committable) getWrapper() :
                        (this instanceof Committable) ? (Committable) this : null;
            if ((committable != null) && clientConfig.getUseSaveConfirmation()) {
                windowManager.showOptionDialog(
                        messages.getMainMessage("closeUnsaved.caption"),
                        messages.getMainMessage("saveUnsaved"),
                        MessageType.WARNING,
                        new Action[]{
                                new DialogAction(Type.OK, Status.PRIMARY)
                                    .withCaption(messages.getMainMessage("closeUnsaved.save"))
                                    .withHandler(event -> {

                                    committable.commitAndClose();
                                }),
                                new BaseAction("discard")
                                    .withIcon("icons/cancel.png")
                                    .withCaption(messages.getMainMessage("closeUnsaved.discard"))
                                    .withHandler(event -> {

                                    committable.close(actionId, true);
                                }),
                                new DialogAction(Type.CANCEL)
                                    .withIcon(null)
                                    .withHandler(event -> {

                                    doAfterClose = null;
                                })
                        }
                );
            } else {
                windowManager.showOptionDialog(
                        messages.getMainMessage("closeUnsaved.caption"),
                        messages.getMainMessage("closeUnsaved"),
                        MessageType.WARNING,
                        new Action[]{
                                new DialogAction(Type.YES).withHandler(event -> {
                                    getWrapper().close(actionId, true);
                                }),
                                new DialogAction(Type.NO, Status.PRIMARY).withHandler(event -> {
                                    doAfterClose = null;
                                })
                        }
                );
            }
            return false;
        }

        if (!clientConfig.getManualScreenSettingsSaving()) {
            if (delegate.getWrapper() != null) {
                delegate.getWrapper().saveSettings();
            } else {
                saveSettings();
            }
        }

        delegate.disposeComponents();

        windowManager.close(this);
        boolean res = onClose(actionId);
        if (res && doAfterClose != null) {
            doAfterClose.run();
        }

        stopTimers();

        userActionsLog.trace("Window {} was closed", getId());

        return res;
    }

    public boolean isModified() {
        return delegate.isModified();
    }

    private void stopTimers() {
        // hard stop timers
        for (Timer timer : timers) {
            ((DesktopTimer)timer).disposeTimer();
        }
    }

    @Override
    public boolean close(String actionId, boolean force) {
        forceClose = force;
        return close(actionId);
    }

    @Override
    public void closeAndRun(String actionId, Runnable runnable) {
        this.doAfterClose = runnable;
        close(actionId);
    }

    @Override
    public void addTimer(Timer timer) {
        if (timer instanceof DesktopTimer) {
            timers.add(timer);
        }
    }

    @Override
    public Timer getTimer(String id) {
        if (id == null)
            throw new IllegalArgumentException("id is null");

        for (Timer timer : timers) {
            if (id.equals(timer.getId()))
                return timer;
        }
        return null;
    }

    @Override
    public void addAction(Action action) {
        checkNotNullArgument(action, "action must be non null");

        actionsHolder.addAction(action);
        actionsPermissions.apply(action);
    }

    @Override
    public void addAction(Action action, int index) {
        checkNotNullArgument(action, "action must be non null");

        actionsHolder.addAction(action, index);
        actionsPermissions.apply(action);
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
    public Collection<Action> getActions() {
        return actionsHolder.getActions();
    }

    @Override
    @Nullable
    public Action getAction(String id) {
        return actionsHolder.getAction(id);
    }

    @Nullable
    protected JPanel asTabWindow() {
        java.awt.Component parent = getContainer();
        while (parent != null) {
            if (parent.getParent() instanceof JTabbedPaneExt) {
                return (JPanel) parent;
            }
            parent = parent.getParent();
        }

        return null;
    }

    @Nullable
    protected JPanel asDetachedWindow() {
        java.awt.Component parent = getContainer();
        while (parent != null) {
            if (parent.getParent() != null && parent.getParent().getParent() != null) {
                if (parent.getParent().getParent() instanceof JLayeredPane) {
                    return (JPanel) parent;
                }
            }
            parent = parent.getParent();
        }

        return null;
    }

    protected JTabbedPaneExt getJTabbedPaneExt() {
        java.awt.Component parent = getContainer();
        while (parent != null) {
            if (parent instanceof JTabbedPaneExt) {
                return (JTabbedPaneExt) parent;
            }
            parent = parent.getParent();
        }

        return null;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;

        DialogWindow dialogWindow = asDialogWindow();
        if (dialogWindow != null) {
            dialogWindow.setTitle(caption);
        } else {
            JPanel tabWindow = asTabWindow();
            if (tabWindow != null) {
                setTabCaptionAndDescription(tabWindow);
            } else {
                JPanel singleWindow = asDetachedWindow();
                if (singleWindow != null) {
                    ((TopLevelFrame) singleWindow.getParent().getParent().getParent().getParent())
                            .setTitle(formatTabCaption(caption, description));
                    windowManager.getBreadCrumbs(singleWindow).update();
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;

        JPanel tabWindow = asTabWindow();
        if (tabWindow != null) {
            setTabCaptionAndDescription(tabWindow);
        } else {
            JPanel singleWindow = asDetachedWindow();
            if (singleWindow != null) {
                ((TopLevelFrame) singleWindow.getParent().getParent().getParent().getParent())
                        .setTitle(formatTabCaption(caption, description));
            }
        }
    }

    protected void setTabCaptionAndDescription(JComponent tabWindow) {
        String formattedCaption = formatTabCaption(caption, description);

        JTabbedPaneExt jTabbedPaneExt = getJTabbedPaneExt();
        jTabbedPaneExt.setTitleAt(jTabbedPaneExt.getSelectedIndex(), formattedCaption);
        windowManager.getBreadCrumbs(tabWindow).update();
    }

    protected String formatTabCaption(final String caption, final String description) {
        DesktopConfig desktopConfig = configuration.getConfig(DesktopConfig.class);
        String tabCaption = formatTabDescription(caption, description);

        int maxLength = desktopConfig.getMainTabCaptionLength();
        if (tabCaption.length() > maxLength) {
            return tabCaption.substring(0, maxLength) + "...";
        } else {
            return tabCaption;
        }
    }

    protected String formatTabDescription(final String caption, final String description) {
        if (StringUtils.isNotEmpty(description)) {
            return String.format("%s: %s", caption, description);
        } else {
            return caption;
        }
    }

    @Override
    public WindowContext getContext() {
        return context;
    }

    @Override
    public void setContext(FrameContext ctx) {
        context = (WindowContext) ctx;
    }

    @Override
    public DsContext getDsContext() {
        return dsContext;
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

    @Override
    public boolean isValid() {
        return delegate.isValid();
    }

    @Override
    public void validate() throws ValidationException {
        delegate.validate();
    }

    @Deprecated
    @Override
    public DialogParams getDialogParams() {
        return getWindowManager().getDialogParams();
    }

    @Override
    public Window openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.openWindow(windowAlias, openType, params);
    }

    @Override
    public Window openWindow(String windowAlias, WindowManager.OpenType openType) {
        return delegate.openWindow(windowAlias, openType);
    }

    @Override
    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType) {
        return delegate.openEditor(item, openType);
    }

    @Override
    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.openEditor(item, openType, params);
    }

    @Override
    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        return delegate.openEditor(item, openType, params, parentDs);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        return delegate.openEditor(windowAlias, item, openType, params, parentDs);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.openEditor(windowAlias, item, openType, params);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        return delegate.openEditor(windowAlias, item, openType, parentDs);
    }

    @Override
    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        return delegate.openEditor(windowAlias, item, openType);
    }

    @Override
    public Window.Lookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return delegate.openLookup(entityClass, handler, openType);
    }

    @Override
    public Window.Lookup openLookup(Class<? extends Entity> entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.openLookup(entityClass, handler, openType, params);
    }

    @Override
    public Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.openLookup(windowAlias, handler, openType, params);
    }

    @Override
    public Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return delegate.openLookup(windowAlias, handler, openType);
    }

    @Override
    public Frame openFrame(Component parent, String windowAlias) {
        return delegate.openFrame(parent, windowAlias);
    }

    @Override
    public Frame openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        return delegate.openFrame(parent, windowAlias, params);
    }

    @Override
    public DesktopWindowManager getWindowManager() {
        return windowManager;
    }

    @Override
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = (DesktopWindowManager) windowManager;
    }

    @Override
    public DialogOptions getDialogOptions() {
        return dialogOptions;
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
    public void showNotification(String caption) {
        getWindowManager().showNotification(caption);
    }

    @Override
    public void showNotification(String caption, NotificationType type) {
        getWindowManager().showNotification(caption, type);
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
    public Frame getFrame() {
        return this;
    }

    @Override
    public void setFrame(Frame frame) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Component getParent() {
        return null;
    }

    @Override
    public void setParent(Component parent) {
    }

    @Override
    public void expand(Component component, String height, String width) {
        if (expandedComponent != null && expandedComponent instanceof DesktopComponent) {
            ((DesktopComponent) expandedComponent).setExpanded(false);
        }

        // only Y direction
        if (StringUtils.isEmpty(height) || AUTO_SIZE.equals(height) || height.endsWith("%")) {
            component.setHeight("100%");
        }

        JComponent expandingChild = DesktopComponentsHelper.getComposition(component);

        Pair<JPanel, BoxLayoutAdapter> wrapperInfo = wrappers.get(component);
        if (wrapperInfo != null) {
            expandingChild = wrapperInfo.getFirst();
        }

        layoutAdapter.expand(expandingChild, height, width);

        if (component instanceof DesktopComponent) {
            ((DesktopComponent) component).setExpanded(true);
        }

        expandedComponent = component;
    }

    @Override
    public void expand(Component component) {
        expand(component, "", "");
    }

    @Override
    public void resetExpanded() {
        layoutAdapter.resetExpanded();
        expandedComponent = null;
    }

    @Override
    public boolean isExpanded(Component component) {
        return expandedComponent == component;
    }

    @Override
    public ExpandDirection getExpandDirection() {
        return ExpandDirection.VERTICAL;
    }

    protected void requestRepaint() {
        if (!scheduledRepaint) {
            SwingUtilities.invokeLater(() -> {
                getContainer().revalidate();
                getContainer().repaint();

                java.awt.Container container = getContainer().getTopLevelAncestor();
                if (container instanceof DialogWindow) {
                    DialogWindow dialog = (DialogWindow) container;
                    if (!dialog.isResizable() && (getHeight() <= 0 || getWidth() <= 0)) {
                        dialog.pack();
                    }
                }

                scheduledRepaint = false;
            });

            scheduledRepaint = true;
        }
    }

    @Override
    public void add(Component component) {
        add(component, ownComponents.size());
    }

    @Override
    public void add(Component component, int index) {
        if (component.getParent() != null && component.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        if (ownComponents.contains(component)) {
            int existingIndex = new ArrayList<>(ownComponents).indexOf(component);
            if (index > existingIndex) {
                index--;
            }

            remove(component);
        }

        int implIndex = getActualIndex(index);

        ComponentCaption caption = null;
        boolean haveDescription = false;
        if (DesktopContainerHelper.hasExternalCaption(component)) {
            caption = new ComponentCaption(component);
            captions.put(component, caption);
            getContainer().add(caption, layoutAdapter.getCaptionConstraints(component), implIndex);  // CAUTION this dramatically wrong
            implIndex++;
        } else if (DesktopContainerHelper.hasExternalDescription(component)) {
            caption = new ComponentCaption(component);
            captions.put(component, caption);
            haveDescription = true;
        }

        JComponent composition = DesktopComponentsHelper.getComposition(component);
        // if component have description without caption, we need to wrap
        // component to view Description button horizontally after component
        if (haveDescription) {
            JPanel wrapper = new JPanel();
            BoxLayoutAdapter adapter = BoxLayoutAdapter.create(wrapper);
            adapter.setExpandLayout(true);
            adapter.setSpacing(false);
            adapter.setMargin(false);
            wrapper.add(composition);
            wrapper.add(caption, new CC().alignY("top"));
            getContainer().add(wrapper, layoutAdapter.getConstraints(component), implIndex);
            wrappers.put(component, new Pair<>(wrapper, adapter));
        } else {
            getContainer().add(composition, layoutAdapter.getConstraints(component), implIndex);
        }
        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }

        if (component instanceof BelongToFrame
                && ((BelongToFrame) component).getFrame() == null) {
            ((BelongToFrame) component).setFrame(this);
        } else {
            registerComponent(component);
        }

        if (index == ownComponents.size()) {
            ownComponents.add(component);
        } else {
            List<Component> componentsTempList = new ArrayList<>(ownComponents);
            componentsTempList.add(index, component);

            ownComponents.clear();
            ownComponents.addAll(componentsTempList);
        }

        DesktopContainerHelper.assignContainer(component, this);

        if (component instanceof DesktopAbstractComponent && !isEnabled()) {
            ((DesktopAbstractComponent) component).setParentEnabled(false);
        }

        component.setParent(this);

        requestRepaint();
    }

    @Override
    public int indexOf(Component component) {
        return Iterables.indexOf(ownComponents, c -> c == component);
    }

    @Nullable
    @Override
    public Component getComponent(int index) {
        return Iterables.get(ownComponents, index);
    }

    @Override
    public void remove(Component component) {
        if (wrappers.containsKey(component)) {
            getContainer().remove(wrappers.get(component).getFirst());
            wrappers.remove(component);
        } else {
            getContainer().remove(DesktopComponentsHelper.getComposition(component));
        }
        getContainer().validate();
        if (captions.containsKey(component)) {
            getContainer().remove(captions.get(component));
            captions.remove(component);
        }
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);

        DesktopContainerHelper.assignContainer(component, null);

        if (component instanceof DesktopAbstractComponent && !isEnabled()) {
            ((DesktopAbstractComponent) component).setParentEnabled(true);
        }

        if (expandedComponent == component) {
            expandedComponent = null;
        }

        component.setParent(null);

        requestRepaint();
    }

    @Override
    public void removeAll() {
        wrappers.clear();
        getContainer().removeAll();
        componentByIds.clear();
        captions.clear();

        List<Component> components = new ArrayList<>(ownComponents);
        ownComponents.clear();

        for (Component component : components) {
            if (component instanceof DesktopAbstractComponent && !isEnabled()) {
                ((DesktopAbstractComponent) component).setParentEnabled(true);
            }

            if (expandedComponent == component) {
                expandedComponent = null;
            }

            component.setParent(null);

            DesktopContainerHelper.assignContainer(component, null);
        }

        requestRepaint();
    }

    protected int getActualIndex(int originalIndex) {
        int index = originalIndex;
        Object[] components = ownComponents.toArray();
        for (int i = 0; i < originalIndex; i++) {
            if (DesktopContainerHelper.hasExternalCaption((Component) components[i])) {
                index++;
            }
        }
        return index;
    }

    @Override
    public Component getOwnComponent(String id) {
        return componentByIds.get(id);
    }

    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getWindowComponent(this, id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public JComponent getComposition() {
        return panel;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDebugId() {
        return null;
    }

    @Override
    public void setDebugId(String id) {
    }

    @Override
    public boolean isEnabled() {
        return panel.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() != enabled) {
            panel.setEnabled(enabled);

            updateEnabled();
        }
    }

    public void updateEnabled() {
        for (Component component : ownComponents) {
            if (component instanceof DesktopAbstractComponent) {
                ((DesktopAbstractComponent) component).setParentEnabled(isEnabled());
            }
        }
    }
    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isVisibleItself() {
        return true;
    }

    @Override
    public boolean isEnabledItself() {
        return panel.isEnabled();
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public float getHeight() {
        return heightSize != null ? heightSize.value : -1;
    }

    @Override
    public int getHeightUnits() {
        return heightSize != null ? heightSize.unit : 0;
    }

    @Override
    public void setHeight(String height) {
        heightSize = ComponentSize.parse(height);
    }

    @Override
    public float getWidth() {
        return widthSize != null ? widthSize.value : -1;
    }

    @Override
    public int getWidthUnits() {
        return widthSize != null ? widthSize.unit : 0;
    }

    @Override
    public void setWidth(String width) {
        widthSize = ComponentSize.parse(width);
    }

    @Override
    public Alignment getAlignment() {
        return null;
    }

    @Override
    public void setAlignment(Alignment alignment) {
    }

    @Override
    public String getStyleName() {
        return String.join(" ", styles);
    }

    @Override
    public void setStyleName(String styleName) {
        if (styles == null)
            styles = new LinkedList<>();

        styles.clear();

        parseAndApplyTheme(styleName);
    }

    @Override
    public void addStyleName(String styleName) {
        if (styles == null)
            styles = new LinkedList<>();

        if (StringUtils.isEmpty(styleName) || styles.contains(styleName))
            return;

        parseAndApplyTheme(styleName);
    }

    protected void parseAndApplyTheme(String styleName) {
        if (StringUtils.isNotEmpty(styleName)) {
            StringTokenizer tokenizer = new StringTokenizer(styleName, " ");
            while (tokenizer.hasMoreTokens()) {
                String style = tokenizer.nextToken();
                if (!styles.contains(style))
                    styles.add(style);
            }
        }
    }

    @Override
    public void removeStyleName(String styleName) {
        if (StringUtils.isEmpty(styleName) || CollectionUtils.isEmpty(styles) || !styles.contains(styleName))
            return;

        StringTokenizer tokenizer = new StringTokenizer(styleName, " ");
        while (tokenizer.hasMoreTokens()) {
            styles.remove(tokenizer.nextToken());
        }
    }

    @Override
    public <X> X unwrap(Class<X> internalComponentClass) {
        return (X) getComponent();
    }

    @Override
    public <X> X unwrapComposition(Class<X> internalCompositionClass) {
        return (X) getComposition();
    }

    @Override
    public void setMargin(MarginInfo marginInfo) {
        layoutAdapter.setMargin(marginInfo);
    }

    @Override
    public MarginInfo getMargin() {
        return layoutAdapter.getMargin();
    }

    @Override
    public void setSpacing(boolean enabled) {
        layoutAdapter.setSpacing(enabled);
    }

    @Override
    public boolean getSpacing() {
        return layoutAdapter.getSpacing();
    }

    @Override
    public Window wrapBy(Class<?> wrapperClass) {
        return delegate.wrapBy(wrapperClass);
    }

    @Override
    public Window getWrapper() {
        return delegate.getWrapper();
    }

    protected boolean onClose(String actionId) {
        fireWindowClosed(actionId);
        return true;
    }

    protected void fireWindowClosed(String actionId) {
        for (Object listener : listeners) {
            if (listener instanceof CloseListener) {
                ((CloseListener) listener).windowClosed(actionId);
            }
        }
    }

    protected JComponent getContainer() {
        return panel;
    }

    @Override
    public void updateComponent(Component child) {
        boolean componentReAdded = false;
        if (DesktopContainerHelper.mayHaveExternalCaption(child)) {
            if (captions.containsKey(child)
                    && !DesktopContainerHelper.hasExternalCaption(child)
                    && !DesktopContainerHelper.hasExternalDescription(child)) {
                reAddChild(child);
                componentReAdded = true;
            } else if (!captions.containsKey(child)
                    && (DesktopContainerHelper.hasExternalCaption(child)
                    || DesktopContainerHelper.hasExternalDescription(child))) {
                reAddChild(child);
                componentReAdded = true;
            } else if (captions.containsKey(child)) {
                ComponentCaption caption = captions.get(child);
                caption.update();
                BoxLayoutAdapter adapterForCaption = layoutAdapter;
                if (wrappers.containsKey(child)) {
                    adapterForCaption = wrappers.get(child).getSecond();
                }
                adapterForCaption.updateConstraints(caption, adapterForCaption.getCaptionConstraints(child));
            }
        }

        if (!componentReAdded) {
            JComponent composition;
            if (wrappers.containsKey(child)) {
                composition = wrappers.get(child).getFirst();
            } else {
                composition = DesktopComponentsHelper.getComposition(child);
            }
            layoutAdapter.updateConstraints(composition, layoutAdapter.getConstraints(child));
        }

        requestRepaint();
    }

    protected void reAddChild(Component child) {
        int index = indexOf(child);

        boolean expanded = expandedComponent == child;
        remove(child);
        add(child, index);

        if (expanded) {
            expand(child);
        }
    }

    @Override
    public void dispose() {
        // hard stop timers
        stopTimers();
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
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

        validateAdditionalRules(errors);

        return handleValidationErrors(errors);
    }

    protected void validateAdditionalRules(ValidationErrors errors) {
    }

    protected boolean handleValidationErrors(ValidationErrors errors) {
        delegate.postValidate(errors);

        if (errors.isEmpty())
            return true;

        DesktopComponentsHelper.focusProblemComponent(errors);

        SwingUtilities.invokeLater(() -> {
            delegate.showValidationErrors(errors);
        });

        return false;
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void setIconByName(Icons.Icon icon) {
        this.icon = icons.get(icon);
    }

    @Override
    public ContentSwitchMode getContentSwitchMode() {
        return contentSwitchMode;
    }

    @Override
    public void setContentSwitchMode(ContentSwitchMode mode) {
        Preconditions.checkNotNullArgument(mode, "Content switch mode can't be null. " +
                "Use ContentSwitchMode.DEFAULT option instead");

        this.contentSwitchMode = mode;
    }

    protected class DesktopDialogOptions extends DialogOptions {
        @Override
        protected DialogOptions setWidth(Float width, SizeUnit unit) {
            if (unit != null && unit != SizeUnit.PIXELS) {
                throw new UnsupportedOperationException("Dialog size can be set only in pixels");
            }

            super.setWidth(width, unit);

            if (width != null) {
                DialogWindow dialogWindow = asDialogWindow();
                if (dialogWindow != null) {
                    int intWidth = width.intValue();
                    dialogWindow.setFixedWidth(intWidth >= 0 ? intWidth : null);

                    Dimension dim = new Dimension();
                    if (dialogWindow.getFixedWidth() != null) {
                        dim.width = dialogWindow.getFixedWidth();
                    }
                    if (dialogWindow.getFixedHeight() != null) {
                        dim.height = dialogWindow.getFixedHeight();
                    }
                    dialogWindow.setMinimumSize(dim);

                    dialogWindow.pack();
                }
            }

            return this;
        }

        @Override
        public Boolean getCloseable() {
            DialogWindow dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                return BooleanUtils.isNotFalse(super.getCloseable());
            }

            return super.getCloseable();
        }

        @Override
        public Float getWidth() {
            DialogWindow dialogWindow = asDialogWindow();

            if (dialogWindow == null) {
                return super.getWidth();
            } else {
                return dialogWindow.getFixedWidth() != null ? dialogWindow.getFixedWidth() : -1.0f;
            }
        }

        @Override
        protected DialogOptions setHeight(Float height, SizeUnit unit) {
            if (unit != null && unit != SizeUnit.PIXELS) {
                throw new UnsupportedOperationException("In the desktop module only pixels are allowed");
            }

            super.setHeight(height, unit);

            if (height != null) {
                DialogWindow dialogWindow = asDialogWindow();
                if (dialogWindow != null) {
                    int intHeight = height.intValue();
                    dialogWindow.setFixedHeight(intHeight >= 0 ? intHeight : null);

                    Dimension dim = new Dimension();
                    if (dialogWindow.getFixedWidth() != null) {
                        dim.width = dialogWindow.getFixedWidth();
                    }
                    if (dialogWindow.getFixedHeight() != null) {
                        dim.height = dialogWindow.getFixedHeight();
                    }
                    dialogWindow.setMinimumSize(dim);

                    dialogWindow.pack();
                }
            }

            return this;
        }

        @Override
        public Float getHeight() {
            DialogWindow dialogWindow = asDialogWindow();

            if (dialogWindow == null) {
                return super.getHeight();
            } else {
                return dialogWindow.getFixedHeight() != null ? dialogWindow.getFixedHeight() : -1.0f;
            }
        }

        @Override
        public Boolean getModal() {
            DialogWindow dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                return dialogWindow.isSoftModal();
            }

            return super.getModal();
        }

        @Override
        public DialogOptions setModal(Boolean modal) {
            super.setModal(modal);

            if (asDialogWindow() != null) {
                log.warn("Changing DesktopWindow modal at run time is not supported");
            }

            return this;
        }

        @Override
        public DialogOptions setResizable(Boolean resizable) {
            super.setResizable(resizable);

            DialogWindow dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                dialogWindow.setResizable(BooleanUtils.isTrue(resizable));
            }

            return this;
        }

        @Override
        public Boolean getResizable() {
            DialogWindow dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                return dialogWindow.isResizable();
            }

            return super.getResizable();
        }

        @Override
        public Integer getPositionX() {
            DialogWindow dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                return dialogWindow.getLocation().x;
            }

            return super.getPositionX();
        }

        @Override
        public DialogOptions setPositionX(Integer positionX) {
            super.setPositionX(positionX);

            DialogWindow dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                Integer positionY = getPositionY();
                dialogWindow.setLocation(positionX != null ? positionX : 0,
                        positionY != null ? positionY : 0);
            }

            return this;
        }

        @Override
        public Integer getPositionY() {
            DialogWindow dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                return dialogWindow.getLocation().y;
            }

            return super.getPositionY();
        }

        @Override
        public DialogOptions setPositionY(Integer positionY) {
            super.setPositionY(positionY);

            DialogWindow dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                Integer positionX = getPositionX();
                dialogWindow.setLocation(positionX != null ? positionX : 0,
                        positionY != null ? positionY : 0);
            }

            return this;
        }
    }

    public static class Editor extends DesktopWindow implements Window.Editor {

        @Override
        protected WindowDelegate createDelegate() {
            return new EditorWindowDelegate(this);
        }

        @Override
        public Entity getItem() {
            return ((EditorWindowDelegate) delegate).getItem();
        }

        @Override
        public void setItem(Entity item) {
            ((EditorWindowDelegate) delegate).setItem(item);
        }

        @Override
        public boolean onClose(String actionId) {
            releaseLock();
            return super.onClose(actionId);
        }

        public void releaseLock() {
            ((EditorWindowDelegate) delegate).releaseLock();
        }

        @Nullable
        @Override
        public Datasource getParentDs() {
            return ((EditorWindowDelegate) delegate).getParentDs();
        }

        @Override
        public void setParentDs(Datasource parentDs) {
            ((EditorWindowDelegate) delegate).setParentDs(parentDs);
        }

        protected Datasource getDatasource() {
            return delegate.getDatasource();
        }

        @Override
        public boolean commit() {
            return commit(true);
        }

        @Override
        public boolean commit(boolean validate) {
            if (validate && !getWrapper().validateAll())
                return false;

            return ((EditorWindowDelegate) delegate).commit(false);
        }

        @Override
        public void commitAndClose() {
            if (!getWrapper().validateAll())
                return;

            if (((EditorWindowDelegate) delegate).commit(true))
                close(COMMIT_ACTION_ID);
        }

        @Override
        public boolean isLocked() {
            return ((EditorWindowDelegate) delegate).isLocked();
        }

        @Override
        public boolean isCrossFieldValidate() {
            return ((EditorWindowDelegate) delegate).isCrossFieldValidate();
        }

        @Override
        public void setCrossFieldValidate(boolean crossFieldValidate) {
            ((EditorWindowDelegate) delegate).setCrossFieldValidate(crossFieldValidate);
        }

        @Override
        protected void validateAdditionalRules(ValidationErrors errors) {
            ((EditorWindowDelegate) delegate).validateAdditionalRules(errors);
        }
    }

    public static class Lookup extends DesktopWindow implements Window.Lookup, LookupComponent.LookupSelectionChangeListener {

        protected Component lookupComponent;
        protected Handler handler;
        protected Validator validator;

        protected JPanel container;

        public Lookup() {
            addAction(new SelectAction(this));

            addAction(new AbstractAction(WindowDelegate.LOOKUP_CANCEL_ACTION_ID) {
                @Override
                public void actionPerform(Component component) {
                    close("cancel");
                }
            });
        }

        @Nullable
        protected Action getSelectAction() {
            return getAction(WindowDelegate.LOOKUP_SELECT_ACTION_ID);
        }

        @Override
        public Component getLookupComponent() {
            return lookupComponent;
        }

        @Override
        public void setLookupComponent(Component lookupComponent) {
            Action selectAction = getSelectAction();
            if (this.lookupComponent instanceof LookupSelectionChangeNotifier) {
                LookupSelectionChangeNotifier selectionChangeNotifier = (LookupSelectionChangeNotifier) this.lookupComponent;
                selectionChangeNotifier.removeLookupValueChangeListener(this);

                if (selectAction != null)
                    selectAction.setEnabled(true);
            }

            this.lookupComponent = lookupComponent;

            if (lookupComponent instanceof LookupComponent) {
                ((LookupComponent) lookupComponent).setLookupSelectHandler(() -> {
                    if (selectAction != null)
                        selectAction.actionPerform(null);
                });

                if (lookupComponent instanceof LookupSelectionChangeNotifier) {
                    LookupSelectionChangeNotifier selectionChangeNotifier =
                            (LookupSelectionChangeNotifier) lookupComponent;
                    selectionChangeNotifier.addLookupValueChangeListener(this);

                    if (selectAction != null)
                        selectAction.setEnabled(!selectionChangeNotifier.getLookupSelectedItems().isEmpty());
                }
            }
        }

        @Override
        public Handler getLookupHandler() {
            return handler;
        }

        @Override
        public void setLookupHandler(Handler handler) {
            this.handler = handler;
        }

        @Override
        public Validator getLookupValidator() {
            return validator;
        }

        @Override
        public void setLookupValidator(Validator validator) {
            this.validator = validator;
        }

        @Override
        public void initLookupLayout() {
            Action selectAction = getAction(WindowDelegate.LOOKUP_SELECT_ACTION_ID);
            if (selectAction == null || selectAction.getOwner() == null) {
                Frame frame = openFrame(null, "lookupWindowActions");
                JPanel jPanel = frame.unwrapComposition(JPanel.class);
                BoxLayoutAdapter layoutAdapter = BoxLayoutAdapter.create(jPanel);
                layoutAdapter.setMargin(true);
                panel.add(jPanel);

                if (App.getInstance().isTestMode()) {
                    Component selectButton = frame.getComponent("selectButton");
                    if (selectButton instanceof com.haulmont.cuba.gui.components.Button) {
                        selectButton.unwrap(JButton.class).setName("selectButton");
                    }
                    Component cancelButton = frame.getComponent("cancelButton");
                    if (cancelButton instanceof com.haulmont.cuba.gui.components.Button) {
                        cancelButton.unwrap(JButton.class).setName("cancelButton");
                    }
                }
            }
        }

        @Override
        protected void initLayout() {
            panel = new JPanel();
            panel.setLayout(
                    new MigLayout(
                            "flowy, fillx, ins 0" + (LayoutAdapter.isDebug() ? ", debug" : ""),
                            "",
                            "[]0[]")
            );

            container = new JPanel();
            layoutAdapter = BoxLayoutAdapter.create(container);
            layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.Y);
            layoutAdapter.setMargin(true);

            panel.add(container, "grow, height 100%, width 100%");
        }

        @Override
        public void setWidth(String width) {
            super.setWidth(width);

            updateContainerConstraints();
        }

        @Override
        public void setHeight(String height) {
            super.setHeight(height);

            updateContainerConstraints();
        }

        protected void updateContainerConstraints() {
            CC cc = new CC();

            if (widthSize != null) {
                MigLayoutHelper.applyWidth(cc, (int) widthSize.value, widthSize.unit, true);
            } else {
                MigLayoutHelper.applyWidth(cc, 100, UNITS_PERCENTAGE, true);
            }

            if (heightSize != null) {
                MigLayoutHelper.applyHeight(cc, (int) heightSize.value, heightSize.unit, true);
            } else {
                MigLayoutHelper.applyHeight(cc, 100, UNITS_PERCENTAGE, true);
            }

            MigLayout migLayout = (MigLayout) panel.getLayout();
            migLayout.setComponentConstraints(container, cc);
        }

        @Override
        protected JComponent getContainer() {
            return container;
        }

        @Override
        public void lookupValueChanged(LookupComponent.LookupSelectionChangeEvent event) {
            Action selectAction = getSelectAction();
            if (selectAction != null)
                selectAction.setEnabled(!event.getSource().getLookupSelectedItems().isEmpty());
        }
    }

    protected static class CloseListenerAdapter implements CloseListener {

        protected CloseWithCommitListener closeWithCommitListener;

        public CloseListenerAdapter(CloseWithCommitListener closeWithCommitListener) {
            this.closeWithCommitListener = closeWithCommitListener;
        }

        @Override
        public int hashCode() {
            return closeWithCommitListener.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            CloseListenerAdapter wrapper = (CloseListenerAdapter) obj;

            return this.closeWithCommitListener.equals(wrapper.closeWithCommitListener);
        }

        @Override
        public void windowClosed(String actionId) {
            if (COMMIT_ACTION_ID.equals(actionId)) {
                closeWithCommitListener.windowClosedWithCommitAction();
            }
        }
    }
}