package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaClass;

import java.util.Set;
import java.util.Map;
import java.util.Collections;

abstract class ListActionsHelper<T extends List> {
    protected IFrame frame;
    protected T component;
    protected UserSession userSession;
    protected MetaClass metaClass;

    ListActionsHelper(IFrame frame, T component) {
        if (component == null) {
            throw new IllegalStateException("Component cannot be null");
        }
        this.frame = frame;
        this.component = component;
        userSession = UserSessionClient.getUserSession();
        metaClass = component.getDatasource().getMetaClass();
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
        final AbstractAction action = new AbstractAction("edit") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                if (userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE))
                    return MessageProvider.getMessage(messagesPackage, "actions.Edit");
                else
                    return MessageProvider.getMessage(messagesPackage, "actions.View");
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                final Set selected = ListActionsHelper.this.component.getSelected();
                if (selected.size() == 1) {
                    final CollectionDatasource datasource = ListActionsHelper.this.component.getDatasource();
                    final String windowID = datasource.getMetaClass().getName() + ".edit";

                    Datasource parentDs = null;
                    if (datasource instanceof PropertyDatasource) {
                        MetaProperty metaProperty = ((PropertyDatasource) datasource).getProperty();
                        if (metaProperty.getType().equals(MetaProperty.Type.AGGREGATION)) {
                            parentDs = datasource;
                        }
                    }

                    final Window window = frame.openEditor(windowID, datasource.getItem(), openType, parentDs);

                    if (parentDs == null) {
                        window.addListener(new Window.CloseListener() {
                            public void windowClosed(String actionId) {
                                if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                                    Object item = ((Window.Editor) window).getItem();
                                    if (item instanceof Entity)
                                        datasource.updateItem((Entity) item);
                                }
                            }
                        });
                    }
                }
            }
        };
        ListActionsHelper.this.component.addAction(action);

        return action;
    }

    public Action createRefreshAction() {
        final Action action = new AbstractAction("refresh") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                return MessageProvider.getMessage(messagesPackage, "actions.Refresh");
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                ListActionsHelper.this.component.getDatasource().refresh();
            }
        };
        ListActionsHelper.this.component.addAction(action);

        return action;
    }

    public Action createRemoveAction() {
        return createRemoveAction(true);
    }

    public Action createRemoveAction(final boolean autocommit) {
        final Action action = new AbstractAction("remove") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                return MessageProvider.getMessage(messagesPackage, "actions.Remove");
            }

            public boolean isEnabled() {
                return userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE);
            }

            public void actionPerform(Component component) {
                final Set selected = ListActionsHelper.this.component.getSelected();
                if (!selected.isEmpty()) {
                    final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                    frame.showOptionDialog(
                            MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation"),
                            MessageProvider.getMessage(messagesPackage, "dialogs.Confirmation.Remove"),
                            IFrame.MessageType.CONFIRMATION,
                            new Action[]{new AbstractAction("ok") {
                                public String getCaption() {
                                    return MessageProvider.getMessage(messagesPackage, "actions.Ok");
                                }

                                public boolean isEnabled() {
                                    return true;
                                }

                                public void actionPerform(Component component) {
                                    @SuppressWarnings({"unchecked"})
                                    final CollectionDatasource ds = ListActionsHelper.this.component.getDatasource();
                                    for (Object item : selected) {
                                        ds.removeItem((Entity) item);
                                    }

                                    if (autocommit) {
                                        try {
                                            ds.commit();
                                        } catch (RuntimeException e) {
                                            ds.refresh();
                                            throw e;
                                        }
                                    }
                                }
                            }, new AbstractAction("cancel") {
                                public String getCaption() {
                                    return MessageProvider.getMessage(messagesPackage, "actions.Cancel");
                                }

                                public boolean isEnabled() {
                                    return true;
                                }

                                public void actionPerform(Component component) {
                                }
                            }});
                }
            }
        };
        ListActionsHelper.this.component.addAction(action);

        return action;
    }

    public Action createFilterApplyAction(final String componentId) {
        final Action action = new AbstractAction("apply") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                return MessageProvider.getMessage(messagesPackage, "actions.Apply");
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                ListActionsHelper.this.component.getDatasource().refresh();
            }
        };
        ((Button)ListActionsHelper.this.frame.getComponent(componentId)).setAction(action);

        return action;
    }

    public Action createFilterClearAction(final String componentId, final String containerName) {
        final Action action = new AbstractAction("clear") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                return MessageProvider.getMessage(messagesPackage, "actions.Clear");
            }

            public boolean isEnabled() {
                return true;
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
        };
        ((Button)ListActionsHelper.this.frame.getComponent(componentId)).setAction(action);

        return action;
    }

    public Action createAddAction(final Window.Lookup.Handler handler, final WindowManager.OpenType openType) {
        return createAddAction(handler, openType, Collections.<String, Object>emptyMap());
    }

    public Action createAddAction(final Window.Lookup.Handler handler) {
        return createAddAction(handler, WindowManager.OpenType.THIS_TAB);
    }

    public Action createAddAction(final Window.Lookup.Handler handler, final Map<String, Object> params) {
        return createAddAction(handler, WindowManager.OpenType.THIS_TAB, params);
    }

    public Action createAddAction(final Window.Lookup.Handler handler, final WindowManager.OpenType openType, final Map<String, Object> params) {
        return createAddAction(handler, openType, params, null);
    }

    public Action createAddAction(final Window.Lookup.Handler handler, final WindowManager.OpenType openType,
                                  final Map<String, Object> params, final String captionKey) {
        final AbstractAction action = new AbstractAction("add") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                return MessageProvider.getMessage(messagesPackage, captionKey == null ? "actions.Add" : captionKey);
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                final CollectionDatasource datasource = ListActionsHelper.this.component.getDatasource();
                final String windowID = datasource.getMetaClass().getName() + ".browse";

                frame.openLookup(windowID, handler, openType, params);
            }
        };
        ListActionsHelper.this.component.addAction(action);

        return action;
    }
}
