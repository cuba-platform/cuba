/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;

import java.util.*;

/**
 * @deprecated Use these actions directly:<br/>
 *     <ul>
 *     <li>{@link com.haulmont.cuba.gui.components.actions.RefreshAction}
 *     <li>{@link com.haulmont.cuba.gui.components.actions.EditAction}
 *     <li>{@link com.haulmont.cuba.gui.components.actions.RemoveAction}
 *     <li>etc.
 *     </ul>
 *     See also:
 *     <ul>
 *         <li>{@link com.haulmont.cuba.gui.ComponentsHelper#createActions(ListComponent)}
 *         <li>{@link com.haulmont.cuba.gui.ComponentsHelper#createActions(ListComponent, java.util.EnumSet)}
  *     </ul>
 */
@Deprecated
public abstract class ListActionsHelper<T extends ListComponent> {

    protected IFrame frame;
    protected T component;
    protected UserSession userSession;
    protected MetaClass metaClass;
    protected MetaProperty metaProperty;

    protected java.util.List<Listener> listeners;
    protected java.util.List<WindowListener> windowListeners;

    ListActionsHelper(IFrame frame, T component) {
        if (component == null) {
            throw new IllegalStateException("Component cannot be null");
        }
        this.frame = frame;
        this.component = component;
        userSession = UserSessionClient.getUserSession();
        CollectionDatasource ds = component.getDatasource();
        metaClass = ds.getMetaClass();
        if (ds instanceof PropertyDatasource) {
            metaProperty = ((PropertyDatasource) ds).getProperty();
        }
        listeners = new ArrayList<Listener>();
        windowListeners = new ArrayList<WindowListener>();
    }

    public Action createCreateAction() {
        return createCreateAction(new ValueProvider() {
            public Map<String, Object> getValues() {
                return Collections.emptyMap();
            }

            public Map<String, Object> getParameters() {
                return Collections.emptyMap();
            }
        }, WindowManager.OpenType.THIS_TAB);
    }

    public Action createCreateAction(final WindowManager.OpenType openType) {
        return createCreateAction(new ValueProvider() {
            public Map<String, Object> getValues() {
                return Collections.emptyMap();
            }

            public Map<String, Object> getParameters() {
                return Collections.emptyMap();
            }
        }, openType);
    }

    public Action createCreateAction(final ValueProvider valueProvider) {
        return createCreateAction(valueProvider, WindowManager.OpenType.THIS_TAB);
    }

    public abstract Action createCreateAction(final ValueProvider valueProvider, final WindowManager.OpenType openType);

    public Action createEditAction() {
        return createEditAction(WindowManager.OpenType.THIS_TAB);
    }

    public Action createEditAction(final WindowManager.OpenType openType) {
        return createEditAction(openType, (ValueProvider) null);
    }

    public Action createEditAction(final WindowManager.OpenType openType, final Map<String, Object> params) {
        ValueProvider vp = new ValueProvider() {

            public Map<String, Object> getValues() {
                return Collections.EMPTY_MAP;
            }

            public Map<String, Object> getParameters() {
                return params;
            }
        };
        final AbstractAction action = new EditAction("edit", openType, vp);
        ListActionsHelper.this.component.addAction(action);

        return action;
    }

    public Action createEditAction(final WindowManager.OpenType openType, ValueProvider valueProvider) {
        final AbstractAction action = new EditAction("edit", openType, valueProvider);
        ListActionsHelper.this.component.addAction(action);

        return action;
    }

    public Action createRefreshAction() {
        Action action = new RefreshAction();
        component.addAction(action);
        return action;
    }

    public Action createRefreshAction(ValueProvider valueProvider) {
        Action action = new RefreshAction(valueProvider);
        component.addAction(action);
        return action;
    }

    public Action createRemoveAction() {
        return createRemoveAction(true);
    }

    public Action createRemoveAction(final boolean autocommit) {
        Action action = new RemoveAction(autocommit);
        component.addAction(action);
        return action;
    }

    public Action createExcludeAction(final boolean autocommit, final boolean confirm) {
        Action action = new ExcludeAction(autocommit, confirm);
        component.addAction(action);
        return action;
    }

    public Action createFilterApplyAction(final String componentId) {
        Action action = new FilterApplyAction();
        ((Button) frame.getComponent(componentId)).setAction(action);
        return action;
    }

    public Action createFilterClearAction(final String componentId, final String containerName) {
        Action action = new FilterClearAction(containerName);
        ((Button) frame.getComponent(componentId)).setAction(action);
        return action;
    }

    public Action createAddAction(final Window.Lookup.Handler handler, final WindowManager.OpenType openType) {
        return createAddAction(handler, openType, Collections.<String, Object>emptyMap());
    }

    public Action createAddAction(final Window.Lookup.Handler handler) {
        return createAddAction(handler, WindowManager.OpenType.THIS_TAB);
    }

    public Action createAddAction(final Window.Lookup.Handler handler, String windowId) {
        return createAddAction(handler, Collections.<String, Object>emptyMap(), windowId);
    }

    public Action createAddAction(final Window.Lookup.Handler handler, final Map<String, Object> params, String windowId) {
        return createAddAction(handler, WindowManager.OpenType.THIS_TAB, params, null, windowId);
    }

    public Action createAddAction(final Window.Lookup.Handler handler, final Map<String, Object> params) {
        return createAddAction(handler, WindowManager.OpenType.THIS_TAB, params);
    }

    public Action createAddAction(final Window.Lookup.Handler handler, final WindowManager.OpenType openType, final Map<String, Object> params) {
        return createAddAction(handler, openType, params, null);
    }

    public Action createAddAction(String windowAlias, final Window.Lookup.Handler handler, final WindowManager.OpenType openType, final Map<String, Object> params) {
        AddAction addAction = (AddAction) createAddAction(handler, openType, params, null);
        addAction.setWindow(windowAlias);
        return addAction;
    }

    public Action createAddAction(final Window.Lookup.Handler handler, final WindowManager.OpenType openType,
                                  final Map<String, Object> params, final String captionKey) {
        return createAddAction(handler, openType, params, captionKey, null);
    }

    public Action createAddAction(final Window.Lookup.Handler handler, final WindowManager.OpenType openType,
                                  final Map<String, Object> params, final String captionKey, String windowId) {
        AbstractAction action = new AddAction(captionKey, handler, openType, params, windowId);
        component.addAction(action);
        return action;
    }

    protected void fireCreateEvent(Entity entity) {
        for (Listener listener : listeners) {
            listener.entityCreated(entity);
        }
    }

    protected void fireEditEvent(Entity entity) {
        for (Listener listener : listeners) {
            listener.entityEdited(entity);
        }
    }

    protected void fireChildWindowClosedEvent(Window window) {
        for (WindowListener listener : windowListeners) {
            listener.childWindowClosed(window);
        }
    }

    protected void fireRemoveEvent(Set<Entity> entities) {
        for (Listener listener : listeners) {
            listener.entityRemoved(entities);
        }
    }

    public void addListener(Listener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void addListener(WindowListener listener) {
        if (!windowListeners.contains(listener)) {
            windowListeners.add(listener);
        }
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void removeListener(WindowListener listener) {
        windowListeners.remove(listener);
    }

    @Deprecated
    protected class EditAction extends AbstractAction {
        private final WindowManager.OpenType openType;

        private ValueProvider valueProvider;

        public EditAction(String id, WindowManager.OpenType openType) {
            super(id);
            this.openType = openType;
        }

        public EditAction(String id, WindowManager.OpenType openType, ValueProvider valueProvider) {
            super(id);
            this.openType = openType;
            this.valueProvider = valueProvider;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            if (userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE))
                return MessageProvider.getMessage(messagesPackage, "actions.Edit");
            else
                return MessageProvider.getMessage(messagesPackage, "actions.View");
        }

        public void actionPerform(Component component) {
            final Set selected = ListActionsHelper.this.component.getSelected();
            if (selected.size() == 1) {
                final CollectionDatasource datasource = ListActionsHelper.this.component.getDatasource();
                final String windowID = datasource.getMetaClass().getName() + ".edit";

                Datasource parentDs = null;
                if (datasource instanceof PropertyDatasource) {
                    MetaProperty metaProperty = ((PropertyDatasource) datasource).getProperty();
                    if (metaProperty.getType().equals(MetaProperty.Type.COMPOSITION)) {
                        parentDs = datasource;
                    }
                }
                final Datasource pDs = parentDs;

                Map<String, Object> params;
                if (valueProvider != null) {
                    params = valueProvider.getParameters();
                } else {
                    params = new HashMap<String, Object>();
                }
                final Window window = frame.openEditor(windowID, datasource.getItem(), openType, params, parentDs);

                window.addListener(new Window.CloseListener() {
                    public void windowClosed(String actionId) {
                        if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                            Object item = ((Window.Editor) window).getItem();
                            if (item instanceof Entity) {
                                if (pDs == null) {
                                    datasource.updateItem((Entity) item);
                                }
                                fireEditEvent((Entity) item);
                            }
                        }
                        fireChildWindowClosedEvent(window);
                    }
                });
            }
        }
    }

    public static interface Listener {
        void entityCreated(Entity entity);

        void entityEdited(Entity entity);

        void entityRemoved(Set<Entity> entity);

    }

    public static interface WindowListener {
        void childWindowClosed(Window window);
    }

    @Deprecated
    protected class RefreshAction extends AbstractAction {

        private ValueProvider valueProvider;

        public RefreshAction() {
            this(null);
        }

        private RefreshAction(ValueProvider valueProvider) {
            super("refresh");
            this.valueProvider = valueProvider;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Refresh");
        }

        public void actionPerform(Component component) {
            CollectionDatasource datasource = ListActionsHelper.this.component.getDatasource();
            if (valueProvider != null) {
                datasource.refresh(valueProvider.getParameters());
            } else {
                datasource.refresh();
            }
        }
    }

    @Deprecated
    protected class RemoveAction extends AbstractAction {
        private final boolean autocommit;

        public RemoveAction(boolean autocommit) {
            super("remove");
            this.autocommit = autocommit;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Remove");
        }

        public boolean isManyToMany() {
            return metaProperty != null && metaProperty.getRange() != null && metaProperty.getRange().getCardinality() != null && metaProperty.getRange().getCardinality() == Range.Cardinality.MANY_TO_MANY;
        }

        public boolean isEnabled() {
            return super.isEnabled() && (isManyToMany() || userSession.isEntityOpPermitted(metaClass, EntityOp.DELETE));
        }

        public void actionPerform(Component component) {
            if(!isEnabled()) return;
            final Set selected = ListActionsHelper.this.component.getSelected();
            if (!selected.isEmpty()) {
                final String messagesPackage = AppConfig.getMessagesPack();
                frame.showOptionDialog(
                        MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation"),
                        MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation.Remove"),
                        IFrame.MessageType.CONFIRMATION,
                        new Action[]{
                                new AbstractAction("ok") {
                                    public String getCaption() {
                                        return MessageProvider.getMessage(messagesPackage, "actions.Ok");
                                    }

                                    public boolean isEnabled() {
                                        return true;
                                    }

                                    @Override
                                    public String getIcon() {
                                        return "icons/ok.png";
                                    }

                                    public void actionPerform(Component component) {
                                        doRemove(selected, autocommit);
                                        fireRemoveEvent(selected);
                                    }
                                }, new AbstractAction("cancel") {
                                    public String getCaption() {
                                        return MessageProvider.getMessage(messagesPackage, "actions.Cancel");
                                    }

                                    public boolean isEnabled() {
                                        return true;
                                    }

                                    @Override
                                    public String getIcon() {
                                        return "icons/cancel.png";
                                    }

                                    public void actionPerform(Component component) {
                                    }
                                }
                        }
                );
            }
        }

        protected void doRemove(Set selected, boolean autocommit) {
            @SuppressWarnings({"unchecked"})
            final CollectionDatasource ds = ListActionsHelper.this.component.getDatasource();
            for (Object item : selected) {
                ds.removeItem((Entity) item);
            }

            if (this.autocommit) {
                try {
                    ds.commit();
                } catch (RuntimeException e) {
                    ds.refresh();
                    throw e;
                }
            }
        }
    }

    @Deprecated
    protected class ExcludeAction extends AbstractAction {
        private final boolean autocommit;
        private final boolean confirm;

        public ExcludeAction(boolean autocommit, boolean confirm) {
            super("exclude");
            this.autocommit = autocommit;
            this.confirm = confirm;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Exclude");
        }

        public boolean isManyToMany() {
            return metaProperty != null && metaProperty.getRange() != null && metaProperty.getRange().getCardinality() != null && metaProperty.getRange().getCardinality() == Range.Cardinality.MANY_TO_MANY;
        }

        public boolean isEnabled() {
            return super.isEnabled() && (isManyToMany() || userSession.isEntityOpPermitted(metaClass, EntityOp.DELETE));
        }

        public void actionPerform(Component component) {
            if(!isEnabled()) return;
            final Set selected = ListActionsHelper.this.component.getSelected();
            if (!selected.isEmpty()) {
                if (confirm) {
                    final String messagesPackage = AppConfig.getMessagesPack();
                    frame.showOptionDialog(
                            MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation"),
                            MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation.Remove"),
                            IFrame.MessageType.CONFIRMATION,
                            new Action[]{
                                    new AbstractAction("ok") {
                                        public String getCaption() {
                                            return MessageProvider.getMessage(messagesPackage, "actions.Ok");
                                        }

                                        public boolean isEnabled() {
                                            return true;
                                        }

                                        @Override
                                        public String getIcon() {
                                            return "icons/ok.png";
                                        }

                                        public void actionPerform(Component component) {
                                            doExclude(selected, autocommit);
                                            fireRemoveEvent(selected);
                                        }
                                    }, new AbstractAction("cancel") {
                                        public String getCaption() {
                                            return MessageProvider.getMessage(messagesPackage, "actions.Cancel");
                                        }

                                        public boolean isEnabled() {
                                            return true;
                                        }

                                        @Override
                                        public String getIcon() {
                                            return "icons/cancel.png";
                                        }

                                        public void actionPerform(Component component) {
                                        }
                                    }
                            }
                    );
                } else {
                    doExclude(selected, autocommit);
                    fireRemoveEvent(selected);
                }
            }
        }

        protected void doExclude(Set selected, boolean autocommit) {
            @SuppressWarnings({"unchecked"})
            final CollectionDatasource ds = ListActionsHelper.this.component.getDatasource();
            for (Object item : selected) {
                ds.excludeItem((Entity) item);
            }

            if (this.autocommit) {
                try {
                    ds.commit();
                } catch (RuntimeException e) {
                    ds.refresh();
                    throw e;
                }
            }
        }
    }

    @Deprecated
    protected class FilterApplyAction extends AbstractAction {
        public FilterApplyAction() {
            super("apply");
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Apply");
        }

        public void actionPerform(Component component) {
            ListActionsHelper.this.component.getDatasource().refresh();
        }
    }

    @Deprecated
    protected class FilterClearAction extends AbstractAction {
        private final String containerName;

        public FilterClearAction(String containerName) {
            super("clear");
            this.containerName = containerName;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Clear");
        }

        public void actionPerform(Component component) {
            Component.Container container = ListActionsHelper.this.frame.getComponent(containerName);
            ComponentsHelper.walkComponents(container,
                    new ComponentVisitor() {
                        public void visit(Component component, String name) {
                            if (component instanceof Field) {
                                ((Field) component).setValue(null);
                            }
                        }
                    }
            );
        }
    }

    @Deprecated
    protected class AddAction extends AbstractAction {
        private final String captionKey;
        private final Window.Lookup.Handler handler;
        private final WindowManager.OpenType openType;
        private final Map<String, Object> params;
        private String windowId;

        public AddAction(String captionKey, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params, String windowId) {
            super("add");
            this.captionKey = captionKey;
            this.handler = handler;
            this.openType = openType;
            this.params = params;
            this.windowId = windowId;
        }

        public void setWindow(String window) {
            this.windowId = window;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, captionKey == null ? "actions.Add" : captionKey);
        }

        public void actionPerform(Component component) {
            final CollectionDatasource datasource = ListActionsHelper.this.component.getDatasource();
            final String winID = windowId == null ? datasource.getMetaClass().getName() + ".browse" : windowId;

            frame.openLookup(winID, handler, openType, params);
        }
    }
}
