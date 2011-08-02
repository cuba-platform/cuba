/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.03.11 17:20
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EditAction extends AbstractAction implements CollectionDatasourceListener {

    private static final long serialVersionUID = -4849373795449480016L;

    public static final String ACTION_ID = "edit";

    protected ListComponent owner;
    protected WindowManager.OpenType openType;
    protected CollectionDatasource datasource;

    public EditAction(ListComponent owner) {
        this(owner, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    public EditAction(ListComponent owner, WindowManager.OpenType openType) {
        this(owner, openType, ACTION_ID);
    }

    public EditAction(ListComponent owner, WindowManager.OpenType openType, String id) {
        super(id);
        this.owner = owner;
        this.openType = openType;
        this.datasource = owner.getDatasource();
    }

    public String getCaption() {
        final String messagesPackage = AppConfig.getInstance().getMessagesPack();
        if (UserSessionProvider.getUserSession().isEntityOpPermitted(owner.getDatasource().getMetaClass(), EntityOp.UPDATE))
            return MessageProvider.getMessage(messagesPackage, "actions.Edit");
        else
            return MessageProvider.getMessage(messagesPackage, "actions.View");
    }

    public void actionPerform(Component component) {
        final Set selected = owner.getSelected();
        if (selected.size() == 1) {
            String windowID = getWindowId();

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

            final Window window = owner.getFrame().openEditor(windowID, datasource.getItem(), openType, params, parentDs);

            window.addListener(new Window.CloseListener() {
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                        Object item = ((Window.Editor) window).getItem();
                        if (item instanceof Entity) {
                            if (pDs == null) {
                                datasource.updateItem((Entity) item);
                            }
                            afterCommit((Entity) item);
                        }
                    }
                    afterWindowClosed(window);
                }
            });
        }
    }

    protected String getWindowId() {
        return datasource.getMetaClass().getName() + ".edit";
    }

    protected Map<String, Object> getWindowParams() {
        return null;
    }

    protected void afterCommit(Entity entity) {
    }

    protected void afterWindowClosed(Window window) {
    }

    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation) {
    }

    @Override
    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
        setEnabled(item != null);
    }

    @Override
    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
        setEnabled(Datasource.State.VALID.equals(state) && ds.getItem() != null);
    }

    @Override
    public void valueChanged(Object source, String property, Object prevValue, Object value) {
    }
}
