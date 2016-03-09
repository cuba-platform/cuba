/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebFrameActionsHolder;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupBox;
import com.haulmont.cuba.web.toolkit.ui.CubaTree;
import com.haulmont.cuba.web.toolkit.ui.CubaVerticalActionsLayout;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.web.gui.components.WebComponentsHelper.convertAlignment;

/**
 * @author krivopustov
 */
public class WebWindow implements Window, Component.Wrapper,
                                  Component.HasXmlDescriptor, WrappedWindow, Component.Disposable,
                                  Component.SecuredActionsHolder {

    private static Logger log = LoggerFactory.getLogger(WebWindow.class);

    protected String id;
    protected String debugId;

    protected Collection<Component> ownComponents = new LinkedHashSet<>();
    protected Map<String, Component> allComponents = new HashMap<>();

    protected List<CloseListener> listeners = null; // lazy initialized listeners list
    protected List<Timer> timers = null; // lazy initialized timers list

    protected String messagePack;

    protected String focusComponentId;

    protected com.vaadin.ui.Component component;

    protected Element element;

    protected DsContext dsContext;
    protected WindowContext context;

    protected String caption;
    protected String description;

    protected boolean forceClose = false;
    protected boolean closing = false;

    protected Runnable doAfterClose;

    protected WebWindowManager windowManager;

    protected WindowDelegate delegate;

    protected WebFrameActionsHolder actionsHolder = new WebFrameActionsHolder();
    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    protected Configuration configuration = AppBeans.get(Configuration.NAME);
    protected Messages messages = AppBeans.get(Messages.NAME);

    protected boolean disposed = false;
    protected DialogOptions dialogOptions = new WebDialogOptions();

    public WebWindow() {
        component = createLayout();
        delegate = createDelegate();
        if (component instanceof com.vaadin.event.Action.Container) {
            ((com.vaadin.event.Action.Container) component).addActionHandler(new com.vaadin.event.Action.Handler() {
                @Override
                public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                    return actionsHolder.getActionImplementations();
                }

                @Override
                public void handleAction(com.vaadin.event.Action actionImpl, Object sender, Object target) {
                    Action action = actionsHolder.getAction(actionImpl);
                    if (action != null && action.isEnabled() && action.isVisible()) {
                        action.actionPerform(WebWindow.this);
                    }
                }
            });
        }
    }

    protected WindowDelegate createDelegate() {
        return new WindowDelegate(this);
    }

    protected ComponentContainer createLayout() {
        CubaVerticalActionsLayout layout = new CubaVerticalActionsLayout();
        layout.setStyleName("cuba-window-layout");
        layout.setSizeFull();
        return layout;
    }

    protected ComponentContainer getContainer() {
        return (ComponentContainer) component;
    }

    @Nullable
    protected com.vaadin.ui.Window asDialogWindow() {
        if (component.isAttached()) {
            com.vaadin.ui.Component parent = component;
            while (parent != null) {
                if (parent instanceof com.vaadin.ui.Window) {
                    return (com.vaadin.ui.Window) parent;
                }

                parent = parent.getParent();
            }
        }
        return null;
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
    public String getStyleName() {
        return component.getStyleName();
    }

    @Override
    public void setStyleName(String name) {
        getContainer().setStyleName(name);

        getContainer().addStyleName("cuba-window-layout");
    }

    @Override
    public void setSpacing(boolean enabled) {
        if (getContainer() instanceof Layout.SpacingHandler) {
            ((Layout.SpacingHandler) getContainer()).setSpacing(true);
        }
    }

    @Override
    public void setMargin(boolean enable) {
        if (getContainer() instanceof Layout.MarginHandler) {
            ((Layout.MarginHandler) getContainer()).setMargin(new MarginInfo(enable));
        }
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        if (getContainer() instanceof Layout.MarginHandler) {
            ((Layout.MarginHandler) getContainer()).setMargin(new MarginInfo(topEnable, rightEnable, bottomEnable, leftEnable));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
    public void removeAction(@Nullable com.haulmont.cuba.gui.components.Action action) {
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

    @Override
    public boolean isValid() {
        return delegate.isValid();
    }

    @Override
    public void validate() throws ValidationException {
        delegate.validate();
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
                try {
                    ((Validatable) component).validate();
                } catch (ValidationException e) {
                    if (log.isTraceEnabled())
                        log.trace("Validation failed", e);
                    else if (log.isDebugEnabled())
                        log.debug("Validation failed: " + e);

                    ComponentsHelper.fillErrorMessages((Validatable) component, e, errors);
                }
            }
        }

        return handleValidationErrors(errors);
    }

    protected boolean handleValidationErrors(ValidationErrors errors) {
        delegate.postValidate(errors);

        if (errors.isEmpty())
            return true;

        showValidationErrors(errors);

        focusProblemComponent(errors);

        return false;
    }

    protected void showValidationErrors(ValidationErrors errors) {
        StringBuilder buffer = new StringBuilder();
        for (ValidationErrors.Item error : errors.getAll()) {
            buffer.append(error.description).append("\n");
        }

        showNotification(messages.getMessage(WebWindow.class, "validationFail.caption"),
                buffer.toString(), NotificationType.TRAY);
    }

    protected void focusProblemComponent(ValidationErrors errors) {
        Component component = null;
        if (!errors.getAll().isEmpty()) {
            component = errors.getAll().iterator().next().component;
        }

        if (component != null) {
            try {
                com.vaadin.ui.Component vComponent = WebComponentsHelper.unwrap(component);
                com.vaadin.ui.Component c = vComponent;
                com.vaadin.ui.Component prevC = null;
                while (c != null) {
                    if (c instanceof TabSheet && !((TabSheet) c).getSelectedTab().equals(prevC)) {
                        ((TabSheet) c).setSelectedTab(prevC);
                        break;
                    }
                    if (c instanceof CubaGroupBox && !((CubaGroupBox) c).isExpanded()) {
                        ((CubaGroupBox) c).setExpanded(true);
                        break;
                    }
                    prevC = c;
                    c = c.getParent();
                }

                // focus first up component
                c = vComponent;
                while (c != null) {
                    if (c instanceof com.vaadin.ui.Component.Focusable) {
                        ((com.vaadin.ui.Component.Focusable) c).focus();
                        break;
                    }
                    c = c.getParent();
                }
            } catch (Exception e) {
                log.warn("Error while validation handling ", e);
            }
        }
    }

    @Override
    public WebWindowManager getWindowManager() {
        return windowManager;
    }

    @Override
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = (WebWindowManager) windowManager;
    }

    @Override
    public DialogOptions getDialogOptions() {
        return dialogOptions;
    }

    @Deprecated
    @Override
    public DialogParams getDialogParams() {
        return getWindowManager().getDialogParams();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
    public Window.Lookup openLookup(Class<? extends Entity>  entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return delegate.openLookup(entityClass, handler, openType);
    }

    @Override
    public Window.Lookup openLookup(Class<? extends Entity>  entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public WindowContext getContext() {
        return context;
    }

    @Override
    public void setContext(FrameContext ctx) {
        this.context = (WindowContext) ctx;
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
    public void setFocusComponent(String componentId) {
        this.focusComponentId = componentId;
        if (componentId != null) {
            Component focusComponent = getComponent(componentId);
            if (focusComponent != null) {
                focusComponent.requestFocus();
            } else {
                log.error("Can't find focus component: " + componentId);
            }
        } else {
            findAndFocusChildComponent();
        }
    }

    protected com.vaadin.ui.Component.Focusable getComponentToFocus(ComponentContainer container) {
        for (com.vaadin.ui.Component child : container) {
            if (child instanceof Panel) {
                child = ((Panel) child).getContent();
            }
            if (child instanceof TabSheet) {
                // #PL-3176
                // we don't know about selected tab after request
                // may be focused component lays on not selected tab
                // it may break component tree
                continue;
            }
            if (child instanceof ComponentContainer) {
                com.vaadin.ui.Component.Focusable result = getComponentToFocus((ComponentContainer) child);
                if (result != null) {
                    return result;
                }
            } else {
                if (child instanceof com.vaadin.ui.Component.Focusable
                        && !child.isReadOnly()
                        && WebComponentsHelper.isComponentVisible(child)
                        && WebComponentsHelper.isComponentEnabled(child)
                        && !(child instanceof Button)) {

                    return (com.vaadin.ui.Component.Focusable) child;
                }
            }
        }
        return null;
    }

    @Override
    public String getFocusComponent() {
        return focusComponentId;
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
        if (listeners == null) {
            listeners = new LinkedList<>();
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeCloseListener(CloseListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
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

    @Override
    public void applySettings(Settings settings) {
        delegate.applySettings(settings);
    }

    @Override
    public void addTimer(Timer timer) {
        AppWindow appWindow = AppUI.getCurrent().getAppWindow();
        appWindow.addTimer(((WebTimer) timer).getTimerImpl());

        if (timers == null) {
            timers = new LinkedList<>();
        }
        timers.add(timer);
    }

    @Override
    public Timer getTimer(final String id) {
        if (timers == null) {
            return null;
        }

        return (Timer) CollectionUtils.find(timers, object -> StringUtils.equals(id, ((Timer) object).getId()));
    }

    public void stopTimers() {
        AppWindow appWindow = AppUI.getCurrent().getAppWindow();
        if (timers != null) {
            for (Timer timer : timers) {
                timer.stop();
                WebTimer webTimer = (WebTimer) timer;
                appWindow.removeTimer(webTimer.getTimerImpl());
            }
        }
    }

    @Override
    public Settings getSettings() {
        return delegate.getSettings();
    }

    @Override
    public Element getXmlDescriptor() {
        return element;
    }

    @Override
    public void setXmlDescriptor(Element element) {
        this.element = element;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void add(Component childComponent) {
        add(childComponent, ownComponents.size());
    }

    @Override
    public void add(Component childComponent, int index) {
        if (childComponent.getParent() != null && childComponent.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        if (ownComponents.contains(childComponent)) {
            com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(childComponent);
            int existingIndex = ((AbstractOrderedLayout)getContainer()).getComponentIndex(composition);
            if (index > existingIndex) {
                index--;
            }

            remove(childComponent);
        }

        ComponentContainer container = getContainer();
        com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);
        ((AbstractOrderedLayout)container).addComponent(vComponent, index);

        com.vaadin.ui.Alignment alignment = convertAlignment(childComponent.getAlignment());
        ((AbstractOrderedLayout) container).setComponentAlignment(vComponent, alignment);

        if (childComponent instanceof BelongToFrame
                && ((BelongToFrame) childComponent).getFrame() == null) {
            ((BelongToFrame) childComponent).setFrame(this);
        } else {
            registerComponent(childComponent);
        }

        if (index == ownComponents.size()) {
            ownComponents.add(childComponent);
        } else {
            List<Component> componentsTempList = new ArrayList<>(ownComponents);
            componentsTempList.add(index, childComponent);

            ownComponents.clear();
            ownComponents.addAll(componentsTempList);
        }

        childComponent.setParent(this);
    }

    @Override
    public int indexOf(Component component) {
        return ComponentsHelper.indexOf(ownComponents, component);
    }

    @Override
    public void remove(Component childComponent) {
        getContainer().removeComponent(WebComponentsHelper.getComposition(childComponent));
        ownComponents.remove(childComponent);

        childComponent.setParent(null);
    }

    @Override
    public void removeAll() {
        getContainer().removeAllComponents();
        for (Component childComponent : ownComponents) {
            if (childComponent.getId() != null) {
                allComponents.remove(childComponent.getId());
            }
        }

        List<Component> childComponents = new ArrayList<>(ownComponents);
        ownComponents.clear();

        for (Component ownComponent : childComponents) {
            ownComponent.setParent(null);
        }
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    protected boolean onClose(String actionId) {
        fireWindowClosed(actionId);
        return true;
    }

    protected void fireWindowClosed(String actionId) {
        if (listeners != null) {
            for (Object listener : listeners) {
                if (listener instanceof CloseListener) {
                    ((CloseListener) listener).windowClosed(actionId);
                }
            }
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;

        if (AppUI.getCurrent().isTestMode() && StringUtils.isEmpty(debugId)) {
            setDebugId(id);
        }
    }

    @Override
    public Component getParent() {
        return null;
    }

    @Override
    public void setParent(Component parent) {
    }

    @Override
    public String getDebugId() {
        return debugId;
    }

    @Override
    public void setDebugId(String debugId) {
        this.debugId = debugId;

        if (debugId != null) {
            component.setId(AppUI.getCurrent().getTestIdManager().getTestId("window_" + debugId));
        }
    }

    @Override
    public boolean isEnabled() {
        return component.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        component.setEnabled(enabled);
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
    public void requestFocus() {
    }

    @Override
    public float getHeight() {
        return component.getHeight();
    }

    @Override
    public int getHeightUnits() {
        return WebAbstractComponent.UNIT_SYMBOLS.indexOf(component.getHeightUnits());
    }

    @Override
    public void setHeight(String height) {
        component.setHeight(height);
    }

    @Override
    public float getWidth() {
        return component.getWidth();
    }

    @Override
    public int getWidthUnits() {
        return WebAbstractComponent.UNIT_SYMBOLS.indexOf(component.getWidthUnits());
    }

    @Override
    public void setWidth(String width) {
        component.setWidth(width);
    }

    @Override
    public Component getOwnComponent(String id) {
        Component nestedComponent = allComponents.get(id);
        if (ownComponents.contains(nestedComponent)) {
            return nestedComponent;
        }

        return null;
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getWindowComponent(this, id);
    }

    @Nonnull
    @Override
    public Component getComponentNN(String id) {
        Component component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
    }

    @Override
    public Alignment getAlignment() {
        return Alignment.MIDDLE_CENTER;
    }

    @Override
    public void setAlignment(Alignment alignment) {
    }

    @Override
    public void expand(Component component, String height, String width) {
        final com.vaadin.ui.Component expandedComponent = WebComponentsHelper.getComposition(component);
        if (getContainer() instanceof AbstractOrderedLayout) {
            WebComponentsHelper.expand((AbstractOrderedLayout) getContainer(), expandedComponent, height, width);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void expand(Component component) {
        expand(component, "", "");
    }

    @Override
    public void resetExpanded() {
        if (getContainer() instanceof AbstractOrderedLayout) {
            AbstractOrderedLayout container = (AbstractOrderedLayout) getContainer();

            for (com.vaadin.ui.Component child : container) {
                container.setExpandRatio(child, 0.0f);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean isExpanded(Component component) {
        return ownComponents.contains(component) && WebComponentsHelper.isComponentExpanded(component);
    }

    @Override
    public ExpandDirection getExpandDirection() {
        return ExpandDirection.VERTICAL;
    }

    @Override
    public com.vaadin.ui.Component getComponent() {
        return component;
    }

    @Override
    public com.vaadin.ui.Component getComposition() {
        return component;
    }

    @Override
    public void closeAndRun(String actionId, Runnable runnable) {
        this.doAfterClose = runnable;
        close(actionId);
    }

    @Override
    public boolean close(final String actionId, boolean force) {
        forceClose = force;
        return close(actionId);
    }

    @Override
    public boolean close(final String actionId) {
        if (!forceClose) {
            if (!delegate.preClose(actionId))
                return false;
        }

        if (closing) {
            return true;
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
                                new DialogAction(DialogAction.Type.OK, Status.PRIMARY) {
                                    @Override
                                    public String getCaption() {
                                        return messages.getMainMessage("closeUnsaved.save");
                                    }
                                    @Override
                                    public void actionPerform(Component component) {
                                        committable.commitAndClose();
                                    }
                                },
                                new AbstractAction("discard") {
                                    {
                                        ThemeConstantsManager thCM = AppBeans.get(ThemeConstantsManager.NAME);
                                        icon = thCM.getThemeValue("actions.dialog.Cancel.icon");
                                    }

                                    @Override
                                    public String getCaption() {
                                        return messages.getMainMessage("closeUnsaved.discard");
                                    }

                                    @Override
                                    public void actionPerform(Component component) {
                                        close(actionId, true);
                                    }
                                },
                                new DialogAction(DialogAction.Type.CANCEL) {
                                    @Override
                                    public String getIcon() {
                                        return null;
                                    }
                                    @Override
                                    public void actionPerform(Component component) {
                                        doAfterClose = null;
                                        // try to move focus back
                                        findAndFocusChildComponent();
                                    }
                                }
                        }
                );
            } else {
                windowManager.showOptionDialog(
                        messages.getMessage(WebWindow.class, "closeUnsaved.caption"),
                        messages.getMessage(WebWindow.class, "closeUnsaved"),
                        MessageType.WARNING,
                        new Action[]{
                                new DialogAction(DialogAction.Type.YES) {
                                    @Override
                                    public void actionPerform(Component component) {
                                        forceClose = true;
                                        close(actionId);
                                    }
                                },
                                new DialogAction(DialogAction.Type.NO, Status.PRIMARY) {
                                    @Override
                                    public void actionPerform(Component component) {
                                        doAfterClose = null;
                                        // try to move focus back
                                        findAndFocusChildComponent();
                                    }
                                }
                        }
                );
            }
            closing = false;
            return false;
        }

        if (!clientConfig.getManualScreenSettingsSaving()) {
            if (getWrapper() != null) {
                getWrapper().saveSettings();
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
        closing = res;
        return res;
    }

    public boolean findAndFocusChildComponent() {
        com.vaadin.ui.Component.Focusable focusComponent = getComponentToFocus(getContainer());
        if (focusComponent != null) {
            focusComponent.focus();
            return true;
        }
        return false;
    }

    protected boolean isModified() {
        return getDsContext() != null && getDsContext().isModified();
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
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;

        if (component.isAttached()) {
            com.vaadin.ui.Window dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                dialogWindow.setCaption(caption);
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

        if (component.isAttached()) {
            com.vaadin.ui.Window dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                dialogWindow.setDescription(caption);
            }
        }
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
    public Window wrapBy(Class<?> wrapperClass) {
        return delegate.wrapBy(wrapperClass);
    }

    @Override
    public Window getWrapper() {
        return delegate.getWrapper();
    }

    @Override
    public void dispose() {
        stopTimers();

        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }

    protected class WebDialogOptions extends DialogOptions {
        @Override
        public Integer getWidth() {
            com.vaadin.ui.Window dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                return (int)dialogWindow.getWidth();
            }

            return super.getWidth();
        }

        @Override
        public DialogOptions setWidth(Integer width) {
            super.setWidth(width);

            if (width != null) {
                com.vaadin.ui.Window dialogWindow = asDialogWindow();
                if (dialogWindow != null) {
                    if (width < 0) {
                        dialogWindow.setWidthUndefined();
                        component.setWidthUndefined();
                        getContainer().setWidthUndefined();
                    } else {
                        dialogWindow.setWidth(width, Unit.PIXELS);
                        component.setWidth(100, Unit.PERCENTAGE);
                    }
                }
            }

            return this;
        }

        @Override
        public Integer getHeight() {
            com.vaadin.ui.Window dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                return (int)dialogWindow.getHeight();
            }

            return super.getHeight();
        }

        @Override
        public DialogOptions setHeight(Integer height) {
            super.setHeight(height);

            if (height != null) {
                com.vaadin.ui.Window dialogWindow = asDialogWindow();
                if (dialogWindow != null) {
                    if (height < 0) {
                        dialogWindow.setHeightUndefined();
                        component.setHeightUndefined();
                        getContainer().setHeightUndefined();
                    } else {
                        dialogWindow.setHeight(height, Unit.PIXELS);
                        component.setHeight(100, Unit.PERCENTAGE);
                    }
                }
            }

            return this;
        }

        @Override
        public Boolean getModal() {
            com.vaadin.ui.Window dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                return dialogWindow.isModal();
            }

            return super.getModal();
        }

        @Override
        public DialogOptions setModal(Boolean modal) {
            super.setModal(modal);

            com.vaadin.ui.Window dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                dialogWindow.setModal(BooleanUtils.isTrue(modal));
            }

            return this;
        }

        @Override
        public Boolean getResizable() {
            com.vaadin.ui.Window dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                return dialogWindow.isResizable();
            }

            return super.getResizable();
        }

        @Override
        public DialogOptions setResizable(Boolean resizable) {
            super.setResizable(resizable);

            com.vaadin.ui.Window dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                dialogWindow.setResizable(BooleanUtils.isTrue(resizable));
            }

            return this;
        }

        @Override
        public Boolean getCloseable() {
            com.vaadin.ui.Window dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                return dialogWindow.isClosable();
            }

            return super.getCloseable();
        }

        @Override
        public DialogOptions setCloseable(Boolean closeable) {
            super.setCloseable(closeable);

            com.vaadin.ui.Window dialogWindow = asDialogWindow();
            if (dialogWindow != null) {
                dialogWindow.setClosable(BooleanUtils.isTrue(closeable));
            }

            return this;
        }
    }

    public static class Editor extends WebWindow implements Window.Editor {

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
        protected boolean onClose(String actionId) {
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

        protected Collection<com.vaadin.ui.Field> getFields() {
            return WebComponentsHelper.getComponents(getContainer(), com.vaadin.ui.Field.class);
        }

        protected MetaClass getMetaClass() {
            return getDatasource().getMetaClass();
        }

        protected Datasource getDatasource() {
            return delegate.getDatasource();
        }

        protected MetaClass getMetaClass(Object item) {
            final MetaClass metaClass;
            if (item instanceof Datasource) {
                metaClass = ((Datasource) item).getMetaClass();
            } else {
                metaClass = ((Instance) item).getMetaClass();
            }
            return metaClass;
        }

        protected Instance getInstance(Object item) {
            if (item instanceof Datasource) {
                return ((Datasource) item).getItem();
            } else {
                return (Instance) item;
            }
        }

        @Override
        public boolean isModified() {
            return ((EditorWindowDelegate) delegate).isModified();
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
    }

    public static class Lookup extends WebWindow implements Window.Lookup {

        private Handler handler;

        private Validator validator;

        private Component lookupComponent;
        private VerticalLayout container;
        private Button selectButton;
        private Button cancelButton;
        private SelectAction selectAction;

        public Lookup() {
            Configuration configuration = AppBeans.get(Configuration.NAME);
            ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

            addAction(new AbstractAction(WindowDelegate.LOOKUP_SELECTED_ACTION_ID, clientConfig.getCommitShortcut()) {
                @Override
                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                    fireSelectAction();
                }
            });
        }

        @Override
        public com.haulmont.cuba.gui.components.Component getLookupComponent() {
            return lookupComponent;
        }

        @Override
        public void setLookupComponent(Component lookupComponent) {
            this.lookupComponent = lookupComponent;

            if (lookupComponent instanceof com.haulmont.cuba.gui.components.Table) {
                com.haulmont.cuba.gui.components.Table table = (com.haulmont.cuba.gui.components.Table) lookupComponent;
                table.setEnterPressAction(
                        new AbstractAction(WindowDelegate.LOOKUP_ENTER_PRESSED_ACTION_ID) {
                            @Override
                            public void actionPerform(Component component) {
                                fireSelectAction();
                            }
                        });
                table.setItemClickAction(new AbstractAction(WindowDelegate.LOOKUP_ITEM_CLICK_ACTION_ID) {
                    @Override
                    public void actionPerform(Component component) {
                        fireSelectAction();
                    }
                });
            } else if (lookupComponent instanceof Tree) {
                final Tree tree = (Tree) lookupComponent;
                final CubaTree treeComponent = (CubaTree) WebComponentsHelper.unwrap(tree);
                treeComponent.setDoubleClickMode(true);
                treeComponent.addItemClickListener(new ItemClickEvent.ItemClickListener() {
                    @Override
                    public void itemClick(ItemClickEvent event) {
                        if (event.isDoubleClick()) {
                            if (event.getItem() != null) {
                                treeComponent.setValue(event.getItemId());
                                fireSelectAction();
                            }
                        }
                    }
                });
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
        protected ComponentContainer getContainer() {
            return container;
        }

        @Override
        public void setLookupValidator(Validator validator) {
            this.validator = validator;
        }

        @Override
        public Validator getLookupValidator() {
            return validator;
        }

        protected void fireSelectAction() {
            if (selectAction != null)
                selectAction.buttonClick(null);
        }

        @Override
        public String getStyleName() {
            return container.getStyleName();
        }

        @Override
        protected ComponentContainer createLayout() {
            final CubaVerticalActionsLayout form = new CubaVerticalActionsLayout();
            form.setStyleName("cuba-lookup-window-wrapper");

            container = new VerticalLayout();
            container.setStyleName("cuba-window-layout");

            boolean isTestMode = AppUI.getCurrent().isTestMode();

            HorizontalLayout okbar = new HorizontalLayout();
            okbar.setHeight(-1, Unit.PIXELS);
            okbar.setStyleName("cuba-window-actions-pane");
            okbar.setMargin(new MarginInfo(true, false, false, false));
            okbar.setSpacing(true);

            selectAction = new SelectAction(this);
            selectButton = WebComponentsHelper.createButton();
            selectButton.setCaption(messages.getMainMessage("actions.Select"));
            selectButton.setIcon(WebComponentsHelper.getIcon("icons/ok.png"));
            selectButton.addClickListener(selectAction);
            selectButton.setStyleName("cuba-window-action-button");
            if (isTestMode) {
                selectButton.setCubaId("selectButton");
            }

            cancelButton = WebComponentsHelper.createButton();
            cancelButton.setCaption(messages.getMainMessage("actions.Cancel"));
            cancelButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    close("cancel");
                }
            });
            cancelButton.setStyleName("cuba-window-action-button");
            cancelButton.setIcon(WebComponentsHelper.getIcon("icons/cancel.png"));
            if (isTestMode) {
                cancelButton.setCubaId("cancelButton");
            }

            okbar.addComponent(selectButton);
            okbar.addComponent(cancelButton);

            form.addComponent(container);
            form.addComponent(okbar);

            container.setSizeFull();
            form.setExpandRatio(container, 1);
            form.setSizeFull();

            return form;
        }

        @Override
        public void setId(String id) {
            super.setId(id);

            if (debugId != null) {
                AppUI ui = AppUI.getCurrent();
                if (ui.isTestMode()) {
                    TestIdManager testIdManager = ui.getTestIdManager();
                    selectButton.setId(testIdManager.getTestId(debugId + "_selectButton"));
                    cancelButton.setId(testIdManager.getTestId(debugId + "_cancelButton"));
                }
            }
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