/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 19:02:39
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.WindowContext;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebFrameActionsHolder;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.*;

public class WebWindow
        implements
        Window,
        Component.Wrapper,
        Component.HasXmlDescriptor,
        WrappedWindow {
    private static final long serialVersionUID = -686695761338837334L;

    private boolean closing = false;

    private String id;
    private String debugId;

    protected Map<String, Component> componentByIds = new HashMap<String, Component>();
    protected Collection<Component> ownComponents = new HashSet<Component>();

    protected Map<String, Component> allComponents = new HashMap<String, Component>();

    private String messagePack;

    protected com.vaadin.ui.Component component;
    private Element element;

    private DsContext dsContext;
    private WindowContext context;

    private String caption;
    private String description;

    private List<CloseListener> listeners = new ArrayList<CloseListener>();

    private boolean forceClose;

    private static Log log = LogFactory.getLog(WebWindow.class);

    private Runnable doAfterClose;

    protected WindowDelegate delegate;

    protected WebFrameActionsHolder actionsHolder = new WebFrameActionsHolder();

    public WebWindow() {
        component = createLayout();
        delegate = createDelegate();
        ((com.vaadin.event.Action.Container) component).addActionHandler(new com.vaadin.event.Action.Handler() {
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                return actionsHolder.getActionImplementations();
            }

            public void handleAction(com.vaadin.event.Action actionImpl, Object sender, Object target) {
                Action action = actionsHolder.getAction(actionImpl);
                if (action != null && action.isEnabled() && action.isVisible()) {
                    action.actionPerform(WebWindow.this);
                }
            }
        });
    }

    protected WindowDelegate createDelegate() {
        return new WindowDelegate(this, App.getInstance().getWindowManager());
    }

    protected com.vaadin.ui.Component createLayout() {
        VerticalLayout layout = new VerticalActionsLayout();
        layout.setSizeFull();
        return layout;
    }

    protected ComponentContainer getContainer() {
        return (ComponentContainer) component;
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

    public void registerComponent(Component component) {
        if (component.getId() != null)
            allComponents.put(component.getId(), component);
    }

    public String getStyleName() {
        return component.getStyleName();
    }

    public void setStyleName(String name) {
        component.setStyleName(name);
    }

    public void setSpacing(boolean enabled) {
        if (component instanceof Layout.SpacingHandler) {
            ((Layout.SpacingHandler) component).setSpacing(true);
        }
    }

    public void setMargin(boolean enable) {
        if (component instanceof Layout.MarginHandler) {
            ((Layout.MarginHandler) component).setMargin(new Layout.MarginInfo(enable));
        }
    }

    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        if (component instanceof Layout.MarginHandler) {
            ((Layout.MarginHandler) component).setMargin(new Layout.MarginInfo(topEnable, rightEnable, bottomEnable, leftEnable));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addAction(final com.haulmont.cuba.gui.components.Action action) {
        actionsHolder.addAction(action);
    }

    public void removeAction(com.haulmont.cuba.gui.components.Action action) {
        actionsHolder.removeAction(action);
    }

    public Collection<com.haulmont.cuba.gui.components.Action> getActions() {
        return actionsHolder.getActions();
    }

    public com.haulmont.cuba.gui.components.Action getAction(String id) {
        return actionsHolder.getAction(id);
    }

    public boolean isValid() {
        return delegate.isValid();
    }

    public void validate() throws ValidationException {
        delegate.validate();
    }

    public DialogParams getDialogParams() {
        return App.getInstance().getWindowManager().getDialogParams();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.<T>openWindow(windowAlias, openType, params);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        return delegate.<T>openWindow(windowAlias, openType);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        return delegate.<T>openEditor(windowAlias, item, openType, params, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.<T>openEditor(windowAlias, item, openType, params);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        return delegate.<T>openEditor(windowAlias, item, openType, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        return delegate.<T>openEditor(windowAlias, item, openType);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.<T>openLookup(windowAlias, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return delegate.<T>openLookup(windowAlias, handler, openType);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        return delegate.<T>openFrame(parent, windowAlias);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        return delegate.<T>openFrame(parent, windowAlias, params);
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

    public void showNotification(String caption, NotificationType type) {
        com.vaadin.ui.Window.Notification notification =
                new com.vaadin.ui.Window.Notification(caption, WebComponentsHelper.convertNotificationType(type));
        if (type.equals(IFrame.NotificationType.HUMANIZED))
            notification.setDelayMsec(3000);

        com.vaadin.ui.Window window = component.getWindow();
        if (window != null)
            window.showNotification(notification);
        else
            App.getInstance().getAppWindow().showNotification(notification);
    }

    public void showNotification(String caption, String description, NotificationType type) {
        com.vaadin.ui.Window.Notification notification =
                new com.vaadin.ui.Window.Notification(caption, description, WebComponentsHelper.convertNotificationType(type));
        if (type.equals(IFrame.NotificationType.HUMANIZED))
            notification.setDelayMsec(3000);

        com.vaadin.ui.Window window = component.getWindow();
        if (window != null)
            window.showNotification(notification);
        else
            App.getInstance().getAppWindow().showNotification(notification);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public WindowContext getContext() {
        return context;
    }

    public void setContext(WindowContext ctx) {
        this.context = ctx;
    }

    public DsContext getDsContext() {
        return dsContext;
    }

    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    public void setFocusComponent(String componentId) {
        Component component = getComponent(componentId);
        if (component != null) {
            component.requestFocus();
        } else {
            log.error("Can't find focus component: " + componentId);
        }
    }

    public void addListener(CloseListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(CloseListener listener) {
        listeners.remove(listener);
    }

    public void applySettings(Settings settings) {
        delegate.applySettings(settings);
    }

    public void addTimer(Timer timer) {
        App.getInstance().addTimer((WebTimer) timer, this);
    }

    public Timer getTimer(String id) {
        return (Timer) App.getInstance().getTimers().getTimer(id);
    }

    public Settings getSettings() {
        return delegate.getSettings();
    }

    public Element getXmlDescriptor() {
        return element;
    }

    public void setXmlDescriptor(Element element) {
        this.element = element;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void add(Component component) {
        getContainer().addComponent(WebComponentsHelper.getComposition(component));
        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
            registerComponent(component);
        }
        ownComponents.add(component);
    }

    public void remove(Component component) {
        getContainer().removeComponent(WebComponentsHelper.getComposition(component));
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);
    }

    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebugId() {
        return debugId;
    }

    public void setDebugId(String debugId) {
        this.debugId = debugId;
    }

    public boolean isEnabled() {
        return component.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        component.setEnabled(enabled);
    }

    public boolean isVisible() {
        return true;
    }

    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException();
    }

    public void requestFocus() {
    }

    public float getHeight() {
        return component.getHeight();
    }

    public int getHeightUnits() {
        return component.getHeightUnits();
    }

    public void setHeight(String height) {
        component.setHeight(height);
    }

    public float getWidth() {
        return component.getWidth();
    }

    public int getWidthUnits() {
        return component.getWidthUnits();
    }

    public void setWidth(String width) {
        component.setWidth(width);
    }

    public <T extends Component> T getOwnComponent(String id) {
        //noinspection unchecked
        return (T) componentByIds.get(id);
    }

    public <T extends Component> T getComponent(String id) {
        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            return (T) allComponents.get(id);
        } else {
            Component frame = allComponents.get(elements[0]);
            if (frame != null && frame instanceof Container) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return (T) ((Container) frame).getComponent(subPath);
            } else
                return null;
        }
//        return WebComponentsHelper.<T>getComponent(this, id);
    }

    public Alignment getAlignment() {
        return Alignment.MIDDLE_CENTER;
    }

    public void setAlignment(Alignment alignment) {
    }

    public void expand(Component component, String height, String width) {
        final com.vaadin.ui.Component expandedComponent = WebComponentsHelper.getComposition(component);
        if (getContainer() instanceof AbstractOrderedLayout) {
            WebComponentsHelper.expand((AbstractOrderedLayout) getContainer(), expandedComponent, height, width);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void expand(Component component) {
        expand(component, "", "");
    }

    public <T> T getComponent() {
        //noinspection unchecked
        return (T) component;
    }

    public com.vaadin.ui.Component getComposition() {
        return component;
    }

    public void closeAndRun(String actionId, Runnable runnable) {
        this.doAfterClose = runnable;
        close(actionId);
    }

    public boolean close(final String actionId, boolean force) {
        forceClose = force;
        return close(actionId);
    }

    public boolean close(final String actionId) {
        if (closing)
            return true;
        closing = true;
        WebWindowManager windowManager = App.getInstance().getWindowManager();

        if (!forceClose && getDsContext() != null && getDsContext().isModified()) {
            windowManager.showOptionDialog(
                    MessageProvider.getMessage(WebWindow.class, "closeUnsaved.caption"),
                    MessageProvider.getMessage(WebWindow.class, "closeUnsaved"),
                    MessageType.WARNING,
                    new Action[]{
                            new DialogAction(DialogAction.Type.YES) {
                                public void actionPerform(Component component) {
                                    forceClose = true;
                                    close(actionId);
                                }
                            },
                            new DialogAction(DialogAction.Type.NO) {
                                public void actionPerform(Component component) {
                                    doAfterClose = null;
                                }
                            }
                    }
            );
            closing = false;
            return false;
        }

        if (delegate.getWrapper() != null)
            delegate.getWrapper().saveSettings();
        else
            saveSettings();

        windowManager.close(this);
        boolean res = onClose(actionId);
        if (res && doAfterClose != null) {
            doAfterClose.run();
        }
        closing = res;
        return res;
    }

    public void saveSettings() {
        delegate.saveSettings();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public <A extends IFrame> A getFrame() {
        //noinspection unchecked
        return (A) this;
    }

    public void setFrame(IFrame frame) {
        throw new UnsupportedOperationException();
    }

    public void expandLayout(boolean expandLayout) {
        if (expandLayout) {
            getContainer().setSizeFull();
        } else {
            getContainer().setWidth("100%");
            getContainer().setHeight("-1px");
        }
    }

    public Window wrapBy(Class<Window> wrapperClass) {
        return delegate.wrapBy(wrapperClass);
    }

    public Window getWrapper() {
        return delegate.getWrapper();
    }

    public static class Editor extends WebWindow implements Window.Editor {

        public Editor() {
            super();
            addAction(new AbstractShortcutAction("commitAndCloseAction",
                    new ShortcutAction.KeyCombination(ShortcutAction.Key.ENTER, ShortcutAction.Modifier.CTRL)) {
                @Override
                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                    commitAndClose();
                }
            });
        }

        @Override
        protected WindowDelegate createDelegate() {
            return new EditorWindowDelegate(this, App.getInstance().getWindowManager());
        }

        public Entity getItem() {
            return ((EditorWindowDelegate) delegate).getItem();
        }

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

        public boolean commit() {
            return commit(true);
        }

        public boolean commit(boolean validate) {
            if (validate && !((Window.Editor) getWrapper()).validateOnCommit())
                return false;

            ((EditorWindowDelegate) delegate).commit();
            return true;
        }

        public void commitAndClose() {
            if (commit()) {
                close(COMMIT_ACTION_ID);
            }
        }

        public boolean isLocked() {
            return ((EditorWindowDelegate) delegate).isLocked();
        }

        public boolean validateOnCommit() {
            Map<Exception, Component> problems = new HashMap<Exception, Component>();

            Collection<Component> components = ComponentsHelper.getComponents(this);
            for (Component component : components) {
                if (component instanceof Validatable) {
                    try {
                        ((Validatable) component).validate();
                    } catch (ValidationException e) {
                        log.warn("Validation failed", e);
                        problems.put(e, component);
                    }
                }
            }

//                    // TODO validate table columns - smthng like this:
//                    if (impl instanceof com.vaadin.ui.Table) {
//                        Set visibleComponents = ((Table) impl).getVisibleComponents();
//                        for (Object visibleComponent : visibleComponents) {
//                            if (visibleComponent instanceof com.vaadin.ui.Field
//                                    && ((com.vaadin.ui.Field) visibleComponent).isEnabled() &&
//                                    !((com.vaadin.ui.Field) visibleComponent).isReadOnly()) {
//                                try {
//                                    ((com.vaadin.ui.Field) visibleComponent).validate();
//                                } catch (Validator.InvalidValueException e) {
//                                    problems.put(e, ((com.vaadin.ui.Field) visibleComponent));
//                                }
//                            }
//                        }
//                    }
//
//                }
//            });

            if (problems.isEmpty())
                return true;

            Component component = null;
            StringBuilder buffer = new StringBuilder(MessageProvider.getMessage(WebWindow.class, "validationFail") + "<br>");
            for (Exception exception : problems.keySet()) {
                if (component == null)
                    component = problems.get(exception);
                buffer.append(exception.getMessage()).append("<br>");
            }

            showNotification(MessageProvider.getMessage(WebWindow.class, "validationFail.caption"),
                    buffer.toString(), NotificationType.TRAY);
            if (component != null) {
                focusProblemComponent(component);
            }

            return false;
        }

        protected void focusProblemComponent(Component component) {
            try {
                com.vaadin.ui.Component vComponent = WebComponentsHelper.unwrap(component);
                com.vaadin.ui.Component c = vComponent;
                com.vaadin.ui.Component prevC = null;
                while (c != null) {
                    if (c instanceof com.vaadin.ui.Component.Focusable) {
                        ((com.vaadin.ui.Component.Focusable) c).focus();
                    } else if (c instanceof TabSheet && !((TabSheet) c).getSelectedTab().equals(prevC)) {
                        ((TabSheet) c).setSelectedTab(prevC);
                        break;
                    }
                    prevC = c;
                    c = c.getParent();
                }
                if (vComponent instanceof com.vaadin.ui.Component.Focusable)
                    ((com.vaadin.ui.Component.Focusable) vComponent).focus();
            } catch (Exception e) {
                //
            }
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
            super();
            addAction(new AbstractShortcutAction(LOOKUP_SELECTED_ACTION_ID,
                    new ShortcutAction.KeyCombination(ShortcutAction.Key.ENTER, ShortcutAction.Modifier.CTRL)) {
                @Override
                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                    fireSelectAction();
                }
            });
        }

        public com.haulmont.cuba.gui.components.Component getLookupComponent() {
            return lookupComponent;
        }

        public void setLookupComponent(Component lookupComponent) {
            this.lookupComponent = lookupComponent;

            if (lookupComponent instanceof com.haulmont.cuba.gui.components.Table) {
                com.haulmont.cuba.gui.components.Table table = (com.haulmont.cuba.gui.components.Table) lookupComponent;
                table.setEnterPressAction(
                        new AbstractAction(LOOKUP_ENTER_PRESSED_ACTION_ID) {
                            @Override
                            public void actionPerform(Component component) {
                                fireSelectAction();
                            }
                        });
                table.setItemClickAction(new AbstractAction(LOOKUP_ITEM_CLICK_ACTION_ID) {
                    @Override
                    public void actionPerform(Component component) {
                        fireSelectAction();
                    }
                });
            }
        }

        public Handler getLookupHandler() {
            return handler;
        }

        public void setLookupHandler(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected ComponentContainer getContainer() {
            return container;
        }

        public void setLookupValidator(Validator validator) {
            this.validator = validator;
        }

        public Validator getLookupValidator() {
            return validator;
        }

        @Override
        public void expandLayout(boolean expandLayout) {
            if (expandLayout) {
                component.setSizeFull();
            } else {
                component.setWidth("100%");
                component.setHeight("-1px");
            }
        }

        protected void fireSelectAction() {
            if (selectAction != null)
                selectAction.buttonClick(null);
        }

        @Override
        protected com.vaadin.ui.Component createLayout() {
            final VerticalLayout form = new VerticalActionsLayout();

            container = new VerticalLayout();

            HorizontalLayout okbar = new HorizontalLayout();
            okbar.setHeight(-1, Sizeable.UNITS_PIXELS);
            okbar.setStyleName("Window-actionsPane");
            okbar.setMargin(true, false, false, false);
            okbar.setSpacing(true);

            final String messagesPackage = AppConfig.getMessagesPack();
            selectAction = new SelectAction(this);
            selectButton = WebComponentsHelper.createButton();
            selectButton.setCaption(MessageProvider.getMessage(messagesPackage, "actions.Select"));
            selectButton.setIcon(new ThemeResource("icons/ok.png"));
            selectButton.addListener(selectAction);
            selectButton.setStyleName("Window-actionButton");

            cancelButton = WebComponentsHelper.createButton();
            cancelButton.setCaption(MessageProvider.getMessage(messagesPackage, "actions.Cancel"));
            cancelButton.addListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    close("cancel");
                }
            });
            cancelButton.setStyleName("Window-actionButton");
            cancelButton.setIcon(new ThemeResource("icons/cancel.png"));

            okbar.addComponent(selectButton);
            okbar.addComponent(cancelButton);

            form.addComponent(container);
            form.addComponent(okbar);

            container.setSizeFull();
            form.setExpandRatio(container, 1);
            form.setComponentAlignment(okbar, com.vaadin.ui.Alignment.MIDDLE_LEFT);
            form.setSizeFull();

            return form;
        }

        @Override
        public void setId(String id) {
            super.setId(id);

            if (ConfigProvider.getConfig(GlobalConfig.class).getTestMode()) {
                WebWindowManager windowManager = App.getInstance().getWindowManager();
                windowManager.setDebugId(selectButton, id + ".selectButton");
                windowManager.setDebugId(cancelButton, id + ".cancelButton");
            }
        }
    }
}
