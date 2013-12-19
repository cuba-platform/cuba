/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Deprecated
public class ActionsFieldHelper {
    private ActionsField component;
    private MetaClass metaClass;
    private String defaultQuery;

    public ActionsFieldHelper(ActionsField component) {
        this(component, component.getMetaProperty().getRange().asClass());
    }

    public ActionsFieldHelper(ActionsField component, MetaClass metaClass) {
        this.component = component;
        this.metaClass = metaClass;
        if (metaClass != null) {
            defaultQuery = String.format("select e from %s e where e.id is null", metaClass.getName());
        }
    }

    public void createLookupAction() {
        createLookupAction(metaClass.getName() + ".browse", WindowManager.OpenType.THIS_TAB, Collections.<String, Object>emptyMap());
    }

    public void createLookupAction(WindowManager.OpenType openType) {
        createLookupAction(metaClass.getName() + ".browse", openType, Collections.<String, Object>emptyMap());
    }

    public void createLookupAction(String windowAlias) {
        createLookupAction(windowAlias, WindowManager.OpenType.THIS_TAB, Collections.<String, Object>emptyMap());
    }

    public void createLookupAction(final String windowAlias, final WindowManager.OpenType openType, final Map<String, Object> params) {
        Action action = new AbstractAction(ActionsField.LOOKUP) {
            public void actionPerform(Component componend) {
                Window window = component.getFrame().openLookup(windowAlias,
                        new Window.Lookup.Handler() {
                            public void handleLookup(Collection items) {
                                if (items != null && items.size() > 0) {
                                    component.setValue(items.iterator().next());
                                }
                            }
                        }, openType, params);

                window.addListener(new Window.CloseListener() {
                    public void windowClosed(String actionId) {
                        CollectionDatasource ds = component.getOptionsDatasource();
                        if (ds != null && !defaultQuery.equals(ds.getQuery())) {
                            ds.refresh();
                        }
                    }
                });
            }

            @Override
            public String getCaption() {
                return "";
            }
        };
        component.addAction(action);
    }

    public void createOpenAction() {
        createOpenAction(WindowManager.OpenType.THIS_TAB);
    }

    public void createOpenAction(final WindowManager.OpenType openType) {
        Action action = new AbstractAction(ActionsField.OPEN) {
            public void actionPerform(Component componend) {
                Entity entity = component.getValue();
                
                if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
                    component.getFrame().showNotification(
                            MessageProvider.getMessage(ActionsFieldHelper.class, "OpenAction.objectIsDeleted"),
                            IFrame.NotificationType.HUMANIZED);
                    return;
                }

                if (entity != null) {
                    LoadContext ctx = new LoadContext(entity.getClass());
                    ctx.setId(entity.getId());
                    ctx.setView(View.MINIMAL);
                    entity = ServiceLocator.getDataService().load(ctx);
                }

                if (entity != null) {
                    String windowAlias = entity.getMetaClass().getName() + ".edit";
                    final Window.Editor editor = component.getFrame().openEditor(windowAlias, entity, openType);
                    editor.addListener(new Window.CloseListener() {
                        public void windowClosed(String actionId) {
                            if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                                Entity item = editor.getItem();

                                CollectionDatasource optionsDatasource = component.getOptionsDatasource();
                                if (optionsDatasource != null && optionsDatasource.containsItem(item.getId())) {
                                    optionsDatasource.updateItem(item);
                                }

                                boolean modified  = component.getDatasource().isModified();
                                component.setValue(null);
                                component.setValue(item);
                                ((DatasourceImplementation) component.getDatasource()).setModified(modified);
                            }
                        }
                    });
                }
            }

            @Override
            public String getCaption() {
                return "";
            }
        };
        component.addAction(action);
    }

}

