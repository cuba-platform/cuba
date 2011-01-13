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
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LockInfo;
import com.haulmont.cuba.core.global.LockNotSupported;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.*;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.gui.components.WebAbstractTable;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.data.Validator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.haulmont.cuba.web.toolkit.ui.FieldGroup;
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

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
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

    private Window wrapper;

    protected List<com.haulmont.cuba.gui.components.Action> actionsOrder = new LinkedList<com.haulmont.cuba.gui.components.Action>();
    protected BiMap<com.vaadin.event.Action, Action> actions = HashBiMap.create();

    public WebWindow() {
        component = createLayout();
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
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openWindow(windowInfo, openType, params);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openWindow(windowInfo, openType);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, params, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, params);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openEditor(windowInfo, item, openType);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openLookup(windowInfo, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openLookup(windowInfo, handler, openType);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        final WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return App.getInstance().getWindowManager().<T>openFrame(wrapper, parent, windowInfo);
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

    public void addListener(CloseListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
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
        return WebComponentsHelper.getComponents(this);
    }

    public boolean onClose(String actionId) {
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
                String subPath = ValuePathHelper.format(subList.toArray(new String[]{}));
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

    public Window wrapBy(Class<Window> aClass) {
        try {
            Constructor<?> constructor;
            try {
                constructor = aClass.getConstructor(Window.class);
            } catch (NoSuchMethodException e) {
                constructor = aClass.getConstructor(IFrame.class);
            }

            wrapper = (Window) constructor.newInstance(this);
            return wrapper;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Window getWrapper() {
        return wrapper;
    }

    public static class Editor extends WebWindow implements Window.Editor {

        protected Entity item;
        protected boolean justLocked;

        private boolean commitActionPerformed;

        private boolean commitAndCloseButtonExists = false;

        public Entity getItem() {
            return item;
        }

        public Editor() {
            super();

            addAction(new ActionWrapper(WINDOW_COMMIT) {
                @Override
                public String getCaption() {
                    final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                    if (commitAndCloseButtonExists) return MessageProvider.getMessage(messagesPackage, "actions.Apply");
                    else return MessageProvider.getMessage(messagesPackage, "actions.Ok");
                }

                @Override
                public boolean isEnabled() {
                    return super.isEnabled() &&
                            UserSessionClient.getUserSession().isEntityOpPermitted(getMetaClass(), EntityOp.UPDATE);
                }

                public void actionPerform(Component component) {
                    if (action != null) {
                        action.actionPerform(component);
                    } else {
                        commit();
                    }
                }
            });

            addAction(new ActionWrapper(WINDOW_COMMIT_AND_CLOSE) {
                @Override
                public String getCaption() {
                    final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                    return MessageProvider.getMessage(messagesPackage, "actions.OkClose");
                }

                @Override
                public boolean isEnabled() {
                    return super.isEnabled() &&
                            UserSessionClient.getUserSession().isEntityOpPermitted(getMetaClass(), EntityOp.UPDATE);
                }

                public void actionPerform(Component component) {
                    if (action != null) {
                        action.actionPerform(component);
                    } else {
                        commitAndClose();
                    }
                }
            });

            addAction(new ActionWrapper(WINDOW_CLOSE) {
                @Override
                public String getCaption() {
                    final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                    boolean commitPermitted = UserSessionClient.getUserSession().isEntityOpPermitted(
                            getMetaClass(), EntityOp.UPDATE);
                    return MessageProvider.getMessage(messagesPackage,
                            commitPermitted ? "actions.Cancel" : "actions.Close");
                }

                public void actionPerform(Component component) {
                    if (action != null) {
                        action.actionPerform(component);
                    } else {
                        close(getId());
                    }
                }
            });
        }

        @Override
        public Window wrapBy(Class<Window> aClass) {
            final Window.Editor window = (Window.Editor) super.wrapBy(aClass);
            final Component commitAndCloseButton = WebComponentsHelper.findComponent(window, WINDOW_COMMIT_AND_CLOSE);
            if (commitAndCloseButton!=null) commitAndCloseButtonExists = true; 
            final Action commitAction = getAction(WINDOW_COMMIT);
            ((ActionWrapper) commitAction).setAction(new AbstractAction(WINDOW_COMMIT) {
                @Override
                public String getCaption() {
                    final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                    return MessageProvider.getMessage(messagesPackage, "actions.Ok");
                }

                public void actionPerform(Component component) {
                    if (!commitAndCloseButtonExists) {
                        window.commitAndClose();
                    } else {
                        if (window.commit()) {
                            commitActionPerformed = true;
                            window.showNotification(MessageProvider.formatMessage(AppConfig.getInstance().getMessagesPack(),
                                    "info.EntitySave", ((Instance) window.getItem()).getInstanceName()),
                                    NotificationType.HUMANIZED);
                        }
                    }
                }
            });

            if (commitAndCloseButtonExists) {
                final Action commitAndCloseAction = getAction(WINDOW_COMMIT_AND_CLOSE);
                ((ActionWrapper) commitAndCloseAction).setAction(new AbstractAction(WINDOW_COMMIT_AND_CLOSE) {
                    @Override
                    public String getCaption() {
                        final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                        return MessageProvider.getMessage(messagesPackage, "actions.OkClose");
                    }

                    public void actionPerform(Component component) {
                        window.commitAndClose();
                    }
                });
            }

            final Action closeAction = getAction(WINDOW_CLOSE);
            ((ActionWrapper) closeAction).setAction(new AbstractAction(WINDOW_CLOSE) {
                @Override
                public String getCaption() {
                    final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                    return MessageProvider.getMessage(messagesPackage, "actions.Cancel");
                }

                public void actionPerform(Component component) {
                    window.close(commitActionPerformed ? COMMIT_ACTION_ID : getId());
                }
            });

            return window;
        }

        public void setItem(Entity item) {
            final Datasource ds = getDatasource();

            if (ds.getCommitMode().equals(Datasource.CommitMode.PARENT)) {
                Datasource parentDs = ((DatasourceImpl) ds).getParent();
                //We have to reload items in parent datasource because when item in child datasource is commited,
                //item in parant datasource must already have all item fields loaded.
                if (parentDs != null) {
                    Collection justChangedItems = new HashSet(((AbstractDatasource) parentDs).getItemsToCreate());
                    justChangedItems.addAll(((AbstractDatasource) parentDs).getItemsToUpdate());

                    DataService dataservice = ds.getDataService();
                    if ((parentDs instanceof CollectionDatasourceImpl) && !(justChangedItems.contains(item)) && ((CollectionDatasourceImpl) parentDs).containsItem(item)) {
                        item = dataservice.reload(item, ds.getView(), ds.getMetaClass());
                        ((CollectionDatasourceImpl) parentDs).updateItem(item);
                    } else if ((parentDs instanceof LazyCollectionDatasource) && !(justChangedItems.contains(item)) && ((LazyCollectionDatasource) parentDs).containsItem(item)) {
                        item = dataservice.reload(item, ds.getView(), ds.getMetaClass());
                        ((LazyCollectionDatasource) parentDs).updateItem(item);
                    } else if ((parentDs instanceof CollectionPropertyDatasourceImpl) && !(justChangedItems.contains(item)) && ((CollectionPropertyDatasourceImpl) parentDs).containsItem(item)) {
                        item = dataservice.reload(item, ds.getView(), ds.getMetaClass());
                        ((CollectionPropertyDatasourceImpl) parentDs).replaceItem(item);
                    }
                }
                item = (Entity) InstanceUtils.copy((Instance) item);
            } else {
                if (!PersistenceHelper.isNew(item)) {
                    final DataService dataservice = ds.getDataService();
                    item = dataservice.reload(item, ds.getView(), ds.getMetaClass());
                }
            }

            if (PersistenceHelper.isNew(item)
                    && !ds.getMetaClass().equals(((Instance) item).getMetaClass()))
            {
                Entity newItem = ds.getDataService().newInstance(ds.getMetaClass());
                InstanceUtils.copy(((Instance) item), ((Instance) newItem));
                item = newItem;
            }

            this.item = item;
            //noinspection unchecked
            ds.setItem(item);
            ((DatasourceImplementation) ds).setModified(false);

            LockService lockService = ServiceLocator.lookup(LockService.NAME);
            LockInfo lockInfo = lockService.lock(ds.getMetaClass().getName(), item.getId().toString());
            if (lockInfo == null) {
                justLocked = true;
            } else if (!(lockInfo instanceof LockNotSupported)) {
                String mp = AppConfig.getInstance().getMessagesPack();
                App.getInstance().getWindowManager().showNotification(
                        MessageProvider.getMessage(mp, "entityLocked.msg"),
                        MessageProvider.formatMessage(mp, "entityLocked.desc",
                                lockInfo.getUser().getLogin(),
                                new SimpleDateFormat(MessageProvider.getMessage(mp, "dateTimeFormat")).format(lockInfo.getSince())
                        ),
                        NotificationType.HUMANIZED
                );
                Action action = getAction(WINDOW_COMMIT);
                if (action != null)
                    action.setEnabled(false);
                action = getAction(WINDOW_COMMIT_AND_CLOSE);
                if (action != null)
                    action.setEnabled(false);
            }
        }

        @Override
        public boolean onClose(String actionId) {
            releaseLock();
            return super.onClose(actionId);
        }

        public void releaseLock() {
            if (justLocked) {
                Entity entity = getDatasource().getItem();
                if (entity != null) {
                    LockService lockService = ServiceLocator.lookup(LockService.NAME);
                    lockService.unlock(getDatasource().getMetaClass().getName(), entity.getId().toString());
                }
            }
        }

        public void setParentDs(Datasource parentDs) {
            Datasource ds = getDatasource();

            if (parentDs == null) {
                ((DatasourceImplementation) ds).setCommitMode(Datasource.CommitMode.DATASTORE);
            } else {
                ((DatasourceImplementation) ds).setCommitMode(Datasource.CommitMode.PARENT);
                ((DatasourceImplementation) ds).setParent(parentDs);
            }
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
            Datasource ds = null;
            Element element = getXmlDescriptor();
            String datasourceName = element.attributeValue("datasource");
            if (!StringUtils.isEmpty(datasourceName)) {
                final DsContext context = getDsContext();
                if (context != null) {
                    ds = context.get(datasourceName);
                }
            }
            if (ds == null)
                throw new IllegalStateException("Can't find main datasource");
            else
                return ds;
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

            final DsContext context = getDsContext();
            if (context != null) {
                context.commit();
                item = getDatasource().getItem();
            } else {
                if (item instanceof Datasource) {
                    final Datasource ds = (Datasource) item;
                    ds.commit();
                } else {
                    DataService service = getDataService();
                    item = service.commit((Entity) item, null);
                }
            }

            return true;
        }

        public void commitAndClose() {
            if (commit()) {
                close(COMMIT_ACTION_ID);
            }
        }

        public boolean isLocked() {
            return !justLocked;
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

        protected DataService getDataService() {
            final DsContext context = getDsContext();
            if (context == null) {
                throw new UnsupportedOperationException();
            } else {
                return context.getDataService();
            }
        }
    }

    protected static class CloseWindowAction implements Button.ClickListener {
        private Window window;

        public CloseWindowAction(com.haulmont.cuba.gui.components.Window window) {
            this.window = window;
        }

        public void buttonClick(Button.ClickEvent event) {
            window.close("cancel");
        }
    }

    public static class Lookup extends WebWindow implements Window.Lookup {

        private Handler handler;

        private Validator validator;

        private Component lookupComponent;
        private VerticalLayout contaiter;

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
            return contaiter;
        }

        public void setLookupValidator(Validator validator) {
            this.validator = validator;
        }

        public Validator getLookupValidator() {
            return validator;
        }

        @Override
        protected com.vaadin.ui.Component createLayout() {
            final VerticalLayout form = new VerticalActionsLayout();

            contaiter = new VerticalLayout();

            HorizontalLayout okbar = new HorizontalLayout();
            okbar.setHeight(-1, Sizeable.UNITS_PIXELS);
            okbar.setStyleName("Window-actionsPane");
            okbar.setMargin(true, false, false, false);
            okbar.setSpacing(true);
            okbar.setWidth("100%");

            final String messagesPackage = AppConfig.getInstance().getMessagesPack();
            final Button selectButton = WebComponentsHelper.createButton();
            selectButton.setCaption(MessageProvider.getMessage(messagesPackage, "actions.Select"));
            selectButton.setIcon(new ThemeResource("icons/ok.png"));
            selectButton.addListener(new SelectAction(this));
            selectButton.setStyleName("Window-actionButton");
//            selectButton.setDebugId("selectButton");

            final Button cancelButton = WebComponentsHelper.createButton();
            cancelButton.setCaption(MessageProvider.getMessage(messagesPackage, "actions.Cancel"));
            cancelButton.addListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    close("cancel");
                }
            });
            cancelButton.setStyleName("Window-actionButton");
            cancelButton.setIcon(new ThemeResource("icons/cancel.png"));
//            cancelButton.setDebugId("cancelButton");

            okbar.addComponent(selectButton);
            okbar.addComponent(cancelButton);

            final WebVBoxLayout vBoxLayout = new WebVBoxLayout();
            okbar.addComponent(vBoxLayout);
            okbar.setExpandRatio(vBoxLayout, 1);

            form.addComponent(contaiter);
            form.addComponent(okbar);

            contaiter.setSizeFull();
            form.setExpandRatio(contaiter, 1);
            form.setComponentAlignment(okbar, com.vaadin.ui.Alignment.MIDDLE_LEFT);

            return form;
        }
    }
}
