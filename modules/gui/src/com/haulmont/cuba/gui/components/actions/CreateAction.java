/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.03.11 9:26
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CreateAction extends AbstractAction {

    private static final long serialVersionUID = 442122838693493665L;

    public static final String ACTION_ID = "create";

    protected final ListComponent owner;
    protected final WindowManager.OpenType openType;
    protected final CollectionDatasource datasource;

    public CreateAction(ListComponent owner) {
        this(owner, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    public CreateAction(ListComponent owner, WindowManager.OpenType openType) {
        this(owner, openType, ACTION_ID);
    }

    public CreateAction(ListComponent owner, WindowManager.OpenType openType, String id) {
        super(id);
        this.owner = owner;
        this.openType = openType;
        datasource = owner.getDatasource();
    }

    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Create");
    }

    public boolean isEnabled() {
        return super.isEnabled() &&
                UserSessionClient.getUserSession().isEntityOpPermitted(datasource.getMetaClass(), EntityOp.CREATE);
    }

    public void actionPerform(Component component) {
        final DataService dataservice = datasource.getDataService();

        final Entity item = dataservice.newInstance(datasource.getMetaClass());

        if (owner instanceof Tree) {
            String hierarchyProperty = ((Tree) owner).getHierarchyProperty();

            Entity parentItem = datasource.getItem();
            // datasource.getItem() may contain deleted item
            if (parentItem != null && !datasource.containsItem(parentItem.getId())) {
                parentItem = null;
            }

            item.setValue(hierarchyProperty, parentItem);
        }

        Map<String, Object> values = getInitialValues();
        if (values != null) {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                final Object value = entry.getValue();
                if (value instanceof Collection) {
                    final Collection collection = (Collection) value;
                    if (!collection.isEmpty()) {
                        if (collection.size() != 1) {
                            throw new UnsupportedOperationException();
                        } else {
                            item.setValue(entry.getKey(), collection.iterator().next());
                        }
                    }
                } else {
                    item.setValue(entry.getKey(), value);
                }
            }
        }

        Datasource parentDs = null;
        if (datasource instanceof PropertyDatasource) {
            MetaProperty metaProperty = ((PropertyDatasource) datasource).getProperty();
            if (metaProperty.getType().equals(MetaProperty.Type.AGGREGATION)) {
                parentDs = datasource;
            }
        }
        final Datasource pDs = parentDs;

        Map<String, Object> params = getWindowParams();
        if (params == null)
            params = new HashMap<String, Object>();

        final Window window = owner.getFrame().openEditor(getWindowId(), item, openType, params, parentDs);

        window.addListener(new Window.CloseListener() {
            public void windowClosed(String actionId) {
                if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                    Object item = ((Window.Editor) window).getItem();
                    if (item instanceof Entity) {
                        if (pDs == null) {
                            boolean modified = datasource.isModified();
                            datasource.addItem((Entity) item);
                            ((DatasourceImplementation) datasource).setModified(modified);
                        }
                        owner.setSelected((Entity) item);
                        afterCommit((Entity) item);
                    }
                }
                afterWindowClosed(window);
            }
        });
    }

    protected String getWindowId() {
        return datasource.getMetaClass().getName() + ".edit";
    }

    protected Map<String, Object> getWindowParams() {
        return null;
    }

    protected Map<String, Object> getInitialValues() {
        return null;
    }

    protected void afterCommit(Entity entity) {
    }

    protected void afterWindowClosed(Window window) {
    }
}
