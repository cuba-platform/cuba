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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.*;
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
import com.haulmont.cuba.web.gui.components.WebAbstractTable;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.FieldGroup;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.data.Validator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.*;
import java.util.List;

public class WebWindow
        implements 
            Window,
            Component.Wrapper,
            Component.HasXmlDescriptor,
            WrappedWindow
{
    private static final long serialVersionUID = -686695761338837334L;

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

    private Settings settings;

    private boolean forceClose;

    private static Log log = LogFactory.getLog(WebWindow.class);

    private Runnable doAfterClose;

    protected WindowDelegate delegate;

    protected List<com.haulmont.cuba.gui.components.Action> actionsOrder = new LinkedList<com.haulmont.cuba.gui.components.Action>();
    protected BiMap<com.vaadin.event.Action, Action> actions = HashBiMap.create();

    public WebWindow() {
        component = createLayout();
        delegate = createDelegate();
        ((com.vaadin.event.Action.Container) component).addActionHandler(new com.vaadin.event.Action.Handler() {
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                final Set<com.vaadin.event.Action> keys = actions.keySet();
                return keys.toArray(new com.vaadin.event.Action[keys.size()]);
            }

            public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
                Action act = actions.get(action);
                if (act != null && act.isEnabled()) {
                    act.actionPerform(WebWindow.this);
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
        if (action instanceof ShortcutAction) {
            actions.put(WebComponentsHelper.createShortcutAction((ShortcutAction) action), action);
        }
        actionsOrder.add(action);
    }

    public void removeAction(com.haulmont.cuba.gui.components.Action action) {
        actionsOrder.remove(action);
        actions.inverse().remove(action);
    }

    public Collection<com.haulmont.cuba.gui.components.Action> getActions() {
        return Collections.unmodifiableCollection(actionsOrder);
    }

    public com.haulmont.cuba.gui.components.Action getAction(String id) {
        for (com.haulmont.cuba.gui.components.Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }

        return null;
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
        com.vaadin.ui.Window.Notification notify =
                new com.vaadin.ui.Window.Notification(caption, WebComponentsHelper.convertNotificationType(type));
        if(type.equals(IFrame.NotificationType.HUMANIZED))
            notify.setDelayMsec(3000);
        component.getWindow().showNotification(notify);
    }

    public void showNotification(String caption, String description, NotificationType type) {
        com.vaadin.ui.Window.Notification notify =
                new com.vaadin.ui.Window.Notification(caption, description, WebComponentsHelper.convertNotificationType(type));
        if(type.equals(IFrame.NotificationType.HUMANIZED))
            notify.setDelayMsec(3000);
        component.getWindow().showNotification(notify);
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
        if (component != null)
            component.requestFocus();
    }

    public void addListener(CloseListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(CloseListener listener) {
        listeners.remove(listener);
    }

    public void applySettings(Settings settings) {
        this.settings = settings;
        ComponentsHelper.walkComponents(
                this,
                new ComponentVisitor() {
                    public void visit(Component component, String name) {
                        if (component instanceof HasSettings) {
                            log.trace("Applying settings for : " + name + " : " + component);
                            Element e = WebWindow.this.settings.get(name);
                            ((HasSettings) component).applySettings(e);
                            if (component instanceof HasPresentations && e.attributeValue("presentation") != null) {
                                final String def = e.attributeValue("presentation");
                                if (!StringUtils.isEmpty(def)) {
                                    UUID defaultId = UUID.fromString(def);
                                    ((HasPresentations) component).applyPresentationAsDefault(defaultId);
                                }
                            }
                        }
                    }
                }
        );
    }

    public void addTimer(Timer timer) {
        App.getInstance().addTimer((WebTimer) timer, this);
    }

    public Timer getTimer(String id) {
        return (Timer) App.getInstance().getTimer(id);
    }

    public Settings getSettings() {
        return settings;
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
        WebWindowManager windowManager = App.getInstance().getWindowManager();

        if (!forceClose && getDsContext() != null && getDsContext().isModified()) {
            windowManager.showOptionDialog(
                    MessageProvider.getMessage(WebWindow.class, "closeUnsaved.caption"),
                    MessageProvider.getMessage(WebWindow.class, "closeUnsaved"),
                    MessageType.WARNING,
                    new Action[]{
                            new AbstractAction(MessageProvider.getMessage(WebWindow.class, "actions.Yes")) {
                                public void actionPerform(Component component) {
                                    forceClose = true;
                                    close(actionId);
                                }

                                @Override
                                public String getIcon() {
                                    return "icons/ok.png";
                                }
                            },
                            new AbstractAction(MessageProvider.getMessage(WebWindow.class, "actions.No")) {
                                public void actionPerform(Component component) {
                                    doAfterClose = null;
                                }

                                @Override
                                public String getIcon() {
                                    return "icons/cancel.png";
                                }
                            }
                    }
            );
            return false;
        }

        saveSettings();

        windowManager.close(this);
        boolean res = onClose(actionId);
        if (res && doAfterClose != null) {
            doAfterClose.run();
        }
        return res;
    }

    public void saveSettings() {
        ComponentsHelper.walkComponents(
                this,
                new ComponentVisitor() {
                    public void visit(Component component, String name) {
                        if (component instanceof HasSettings && WebWindow.this.settings != null) {
                            log.trace("Saving settings for : " + name + " : " + component);
                            Element e = WebWindow.this.settings.get(name);
                            boolean modified = ((HasSettings) component).saveSettings(e);
                            if (component instanceof HasPresentations && ((HasPresentations) component).isUsePresentations()) {
                                Object def = ((HasPresentations) component).getDefaultPresentationId();
                                if (def != null) {
                                    e.addAttribute("presentation", def.toString());
                                }
                                ((HasPresentations) component).getPresentations().commit();
                            }
                            WebWindow.this.settings.setModified(modified);
                        }
                        if (component instanceof Disposable) {
                            ((Disposable) component).dispose();
                        }
                    }
                }
        );
        if (settings != null) {
            settings.commit();
        }
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

        public boolean isValid() {
            for (com.vaadin.ui.Field field : getFields()) {
                if (!field.isValid()) return false;
            }
            return true;
        }

        public void validate() throws ValidationException {
            for (com.vaadin.ui.Field field : getFields()) {
                try {
                    field.validate();
                } catch (Validator.InvalidValueException e) {
                    throw new ValidationException(e.getMessage());
                }
            }
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
                return (Instance) ((Datasource) item).getItem();
            } else {
                return (Instance) item;
            }
        }

        public boolean commit() {
            return commit(true);
        }

        public boolean commit(boolean validate) {
            if (validate && !__validate())
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

        protected boolean __validate() {
            final Map<Exception, com.vaadin.ui.Field> problems =
                    new HashMap<Exception, com.vaadin.ui.Field>();

            ComponentsHelper.walkComponents(this, new ComponentVisitor() {
                public void visit(Component component, String name) {

                    com.vaadin.ui.Component impl = WebComponentsHelper.unwrap(component);

                    // validate component
                    if (component instanceof WebAbstractTable) {
                        try {
                            ((WebAbstractTable) component).validate();
                        } catch (ValidationException e) {
                            problems.put(e, ((com.vaadin.ui.Field) impl));
                        }
                    } else if (impl instanceof com.vaadin.ui.Field
                            && impl.isVisible() && impl.isEnabled() && !impl.isReadOnly()) {
                        if (impl instanceof FieldGroup) {
                            final FieldGroup fieldGroup = (FieldGroup) impl;
                            for (final Object propId : fieldGroup.getItemPropertyIds()) {
                                final Field f = fieldGroup.getField(propId);
                                if (f.isVisible() && f.isEnabled() && !f.isReadOnly())
                                    validateField(f, problems);
                            }
                        } else {
                            validateField(impl, problems);
                        }
                    }

                    // validate table columns
                    if (impl instanceof com.vaadin.ui.Table) {
                        Set visibleComponents = ((Table) impl).getVisibleComponents();
                        for (Object visibleComponent : visibleComponents) {
                            if (visibleComponent instanceof com.vaadin.ui.Field
                                    && ((com.vaadin.ui.Field) visibleComponent).isEnabled() &&
                                    !((com.vaadin.ui.Field) visibleComponent).isReadOnly()) {
                                try {
                                    ((com.vaadin.ui.Field) visibleComponent).validate();
                                } catch (Validator.InvalidValueException e) {
                                    problems.put(e, ((com.vaadin.ui.Field) visibleComponent));
                                }
                            }
                        }
                    }

                }
            });

            if (problems.isEmpty()) return true;

            com.vaadin.ui.Field field = null;
            StringBuffer buffer = new StringBuffer(
                    MessageProvider.getMessage(WebWindow.class, "validationFail") + "<br>");
            for (Exception exception : problems.keySet()) {
                if (field == null) field = problems.get(exception);
                buffer.append(exception.getMessage()).append("<br>");
            }

            showNotification(MessageProvider.getMessage(WebWindow.class, "validationFail.caption"),
                    buffer.toString(), NotificationType.TRAY);
            if (field != null) {
                try {
                    com.vaadin.ui.Component c = field;
                    com.vaadin.ui.Component cp;
                    do{
                        cp = c.getParent();
                        if(cp != null)
                            if (cp instanceof com.vaadin.ui.Component.Focusable) {
                                ((com.vaadin.ui.Component.Focusable) cp).focus();
                            } else if (cp instanceof TabSheet){
                                ((TabSheet)cp).setSelectedTab(c);
                            }
                        c = cp;
                    }while(c != null);
                    field.focus();
                } catch (UnsupportedOperationException e) {
                    //
                }
            }

            return false;
        }

        private void validateField(com.vaadin.ui.Component impl, final Map<Exception, Field> problems) {
            try {
                ((Field) impl).validate();
            } catch (Validator.InvalidValueException e) {
                problems.put(e, ((Field) impl));
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

        public com.haulmont.cuba.gui.components.Component getLookupComponent() {
            return lookupComponent;
        }

        public void setLookupComponent(Component lookupComponent) {
            this.lookupComponent = lookupComponent;
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

        @Override
        protected com.vaadin.ui.Component createLayout() {
            final VerticalLayout form = new VerticalActionsLayout();

            container = new VerticalLayout();

            HorizontalLayout okbar = new HorizontalLayout();
            okbar.setHeight(-1, Sizeable.UNITS_PIXELS);
            okbar.setStyleName("Window-actionsPane");
            okbar.setMargin(true, false, false, false);
            okbar.setSpacing(true);

            final String messagesPackage = AppConfig.getInstance().getMessagesPack();
            selectButton = WebComponentsHelper.createButton();
            selectButton.setCaption(MessageProvider.getMessage(messagesPackage, "actions.Select"));
            selectButton.setIcon(new ThemeResource("icons/ok.png"));
            selectButton.addListener(new SelectAction(this));
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
