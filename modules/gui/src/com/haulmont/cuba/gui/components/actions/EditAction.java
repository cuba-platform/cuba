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

/**
 * Standard list action to edit an entity instance.
 * <p>
 *      Action's behaviour can be customized by providing arguments to constructor, as well as overriding the following
 *      methods:
 *      <ul>
 *          <li>{@link #getCaption()}</li>
 *          <li>{@link #isEnabled()}</li>
 *          <li>{@link #getWindowId()}</li>
 *          <li>{@link #getWindowParams()}</li>
 *          <li>{@link #afterCommit(com.haulmont.cuba.core.entity.Entity)}</li>
 *          <li>{@link #afterWindowClosed(com.haulmont.cuba.gui.components.Window)}</li>
 *      </ul>
 * </p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class EditAction extends AbstractAction implements CollectionDatasourceListener {

    private static final long serialVersionUID = -4849373795449480016L;

    public static final String ACTION_ID = ListActionType.EDIT.getId();

    protected ListComponent holder;
    protected WindowManager.OpenType openType;
    protected CollectionDatasource datasource;

    /**
     * The simplest constructor. The action has default name and opens the editor screen in THIS tab.
     * @param holder    component containing this action
     */
    public EditAction(ListComponent holder) {
        this(holder, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    /**
     * Constructor that allows to specify how the editor screen opens. The action has default name.
     * @param holder    component containing this action
     * @param openType  how to open the editor screen
     */
    public EditAction(ListComponent holder, WindowManager.OpenType openType) {
        this(holder, openType, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name and how the editor screen opens.
     * @param holder    component containing this action
     * @param openType  how to open the editor screen
     * @param id        action name
     */
    public EditAction(ListComponent holder, WindowManager.OpenType openType, String id) {
        super(id);
        this.holder = holder;
        this.openType = openType;
        this.datasource = holder.getDatasource();
    }

    /**
     * Returns the action's caption. Override to provide a specific caption.
     * @return  localized caption
     */
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        if (UserSessionProvider.getUserSession().isEntityOpPermitted(holder.getDatasource().getMetaClass(), EntityOp.UPDATE))
            return MessageProvider.getMessage(messagesPackage, "actions.Edit");
        else
            return MessageProvider.getMessage(messagesPackage, "actions.View");
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     * @param component component invoking action
     */
    public void actionPerform(Component component) {
        final Set selected = holder.getSelected();
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

            final Window window = holder.getFrame().openEditor(windowID, datasource.getItem(), openType, params, parentDs);

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

    /**
     * Provides editor screen identifier. Override to provide a specific value.
     * @return  editor screen id
     */
    protected String getWindowId() {
        return datasource.getMetaClass().getName() + ".edit";
    }

    /**
     * Provides editor screen parameters. Override to provide a specific value.
     * @return  editor screen parameters
     */
    protected Map<String, Object> getWindowParams() {
        return null;
    }

    /**
     * Hook invoked after the editor was committed and closed
     * @param entity    new committed entity instance
     */
    protected void afterCommit(Entity entity) {
    }

    /**
     * Hook invoked always after the editor was closed
     * @param window    the editor window
     */
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
