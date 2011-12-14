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
 * Action's behaviour can be customized by providing arguments to constructor, setting properties, or overriding
 * methods {@link #afterCommit(com.haulmont.cuba.core.entity.Entity)}, {@link #afterWindowClosed(com.haulmont.cuba.gui.components.Window)}
 * </p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class EditAction extends AbstractAction implements CollectionDatasourceListener {

    private static final long serialVersionUID = -4849373795449480016L;

    public static final String ACTION_ID = ListActionType.EDIT.getId();

    protected final ListComponent owner;
    protected WindowManager.OpenType openType;

    protected String windowId;
    protected Map<String, Object> windowParams;

    /**
     * The simplest constructor. The action has default name and opens the editor screen in THIS tab.
     * @param owner    component containing this action
     */
    public EditAction(ListComponent owner) {
        this(owner, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    /**
     * Constructor that allows to specify how the editor screen opens. The action has default name.
     * @param owner    component containing this action
     * @param openType  how to open the editor screen
     */
    public EditAction(ListComponent owner, WindowManager.OpenType openType) {
        this(owner, openType, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name and how the editor screen opens.
     * @param owner    component containing this action
     * @param openType  how to open the editor screen
     * @param id        action name
     */
    public EditAction(ListComponent owner, WindowManager.OpenType openType, String id) {
        super(id);
        this.owner = owner;
        this.openType = openType;
        this.icon = "icons/edit.png";
    }

    @Override
    public String getCaption() {
        if (caption != null)
            return caption;

        final String messagesPackage = AppConfig.getMessagesPack();
        if (UserSessionProvider.getUserSession().isEntityOpPermitted(owner.getDatasource().getMetaClass(), EntityOp.UPDATE))
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
        final Set selected = owner.getSelected();
        if (selected.size() == 1) {
            String windowID = getWindowId();

            Datasource parentDs = null;
            final CollectionDatasource datasource = owner.getDatasource();
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

    /**
     * @return  editor screen open type
     */
    public WindowManager.OpenType getOpenType() {
        return openType;
    }

    /**
     * @param openType  editor screen open type
     */
    public void setOpenType(WindowManager.OpenType openType) {
        this.openType = openType;
    }

    /**
     * @return  editor screen identifier
     */
    public String getWindowId() {
        if (windowId != null)
            return windowId;
        else
            return owner.getDatasource().getMetaClass().getName() + ".edit";
    }

    /**
     * @param windowId  editor screen identifier
     */
    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }

    /**
     * @return  editor screen parameters
     */
    public Map<String, Object> getWindowParams() {
        return windowParams;
    }

    /**
     * @param windowParams  editor screen parameters
     */
    public void setWindowParams(Map<String, Object> windowParams) {
        this.windowParams = windowParams;
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
