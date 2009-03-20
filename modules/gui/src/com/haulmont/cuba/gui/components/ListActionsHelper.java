package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;

import java.util.Set;
import java.util.Map;
import java.util.Collections;

abstract class ListActionsHelper<T extends List> {
    protected IFrame frame;
    protected T component;

    ListActionsHelper(IFrame frame, T component) {
        this.frame = frame;
        this.component = component;
    }

    public Action createCreateAction() {
        return createCreateAction(new ValueProvider() {
            public Map<String, Object> getValues() {
                return Collections.emptyMap();
            }
        }, WindowManager.OpenType.THIS_TAB);
    }

    public Action createCreateAction(final WindowManager.OpenType openType) {
        return createCreateAction(new ValueProvider() {
            public Map<String, Object> getValues() {
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
                return MessageProvider.getMessage(messagesPackage, "actions.Edit");
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                final Set selected = ListActionsHelper.this.component.getSelected();
                if (selected.size() == 1) {
                    final CollectionDatasource datasource = ListActionsHelper.this.component.getDatasource();
                    final String windowID = datasource.getMetaClass().getName() + ".edit";

                    final Window window = frame.openEditor(windowID, datasource, openType);
                    window.addListener(new Window.CloseListener() {
                        public void windowClosed(String actionId) {
                            if ("commit".equals(actionId)) {
                                datasource.refresh();
                            }
                        }
                    });
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
        final Action action = new AbstractAction("remove") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                return MessageProvider.getMessage(messagesPackage, "actions.Remove");
            }

            public boolean isEnabled() {
                return true;
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
                                    final CollectionDatasource<Entity, Object> ds = ListActionsHelper.this.component.getDatasource();
                                    for (Object item : selected) {
                                        ds.removeItem((Entity) item);
                                    }

                                    ds.commit();
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
}
