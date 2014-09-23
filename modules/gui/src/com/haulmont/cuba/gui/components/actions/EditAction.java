/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
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
 *
 * @author krivopustov
 * @version $Id$
 */
public class EditAction extends ItemTrackingAction implements Action.HasOpenType {

    public static final String ACTION_ID = ListActionType.EDIT.getId();

    protected final ListComponent owner;
    protected WindowManager.OpenType openType;

    protected String windowId;
    protected Map<String, Object> windowParams;

    protected boolean permissionFlag = false;

    // Set default caption only once
    protected boolean captionInitialized = false;

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
        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig config = configuration.getConfig(ClientConfig.class);
        setShortcut(config.getTableEditShortcut());

        refreshState();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(permissionFlag && enabled);
    }

    @Override
    public boolean isEnabled() {
        return permissionFlag && super.isEnabled();
    }

    protected void setEnabledInternal(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void setCaption(String caption) {
        super.setCaption(caption);

        captionInitialized = true;
    }

    @Override
    public void refreshState() {
        permissionFlag = isPermitted();

        setEnabledInternal(permissionFlag);

        CollectionDatasource ds = owner.getDatasource();

        if (ds != null && !captionInitialized) {
            Security security = AppBeans.get(Security.NAME);
            final String messagesPackage = AppConfig.getMessagesPack();
            if (security.isEntityOpPermitted(ds.getMetaClass(), EntityOp.UPDATE)) {
                setCaption(messages.getMessage(messagesPackage, "actions.Edit"));
            } else {
                setCaption(messages.getMessage(messagesPackage, "actions.View"));
            }
        }

        if (permissionFlag && ds != null) {
            updateApplicableTo(isApplicableTo(ds.getState(),
                    ds.getState() == Datasource.State.VALID ? ds.getItem() : null));
        }
    }

    /**
     * Check permissions for Action
     */
    protected boolean isPermitted() {
        Security security = AppBeans.get(Security.NAME);

        return owner.getDatasource() != null &&
                security.isEntityOpPermitted(owner.getDatasource().getMetaClass(), EntityOp.READ);
    }

    @Override
    public boolean isApplicableTo(Datasource.State state, Entity item) {
        return super.isApplicableTo(state, item) && owner.getSelected().size() < 2;
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     * @param component component invoking action
     */
    @Override
    public void actionPerform(Component component) {
        final Set selected = owner.getSelected();
        if (selected.size() == 1) {
            String windowID = getWindowId();

            Datasource parentDs = null;
            final CollectionDatasource datasource = owner.getDatasource();
            if (datasource instanceof PropertyDatasource) {
                MetaProperty metaProperty = ((PropertyDatasource) datasource).getProperty();
                if (metaProperty.getType().equals(MetaProperty.Type.COMPOSITION)) {
                    parentDs = datasource;
                }
            }
            final Datasource pDs = parentDs;

            Map<String, Object> params = getWindowParams();
            if (params == null)
                params = new HashMap<>();

            final Window window = owner.getFrame().openEditor(windowID, datasource.getItem(), openType, params, parentDs);

            window.addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                        Entity item = ((Window.Editor) window).getItem();
                        if (item != null) {
                            if (pDs == null) {
                                //noinspection unchecked
                                datasource.updateItem(item);
                            }
                            afterCommit(item);
                        }
                    }

                    // move focus to owner
                    owner.requestFocus();

                    afterWindowClosed(window);
                }
            });
        }
    }

    /**
     * @return  editor screen open type
     */
    @Override
    public WindowManager.OpenType getOpenType() {
        return openType;
    }

    /**
     * @param openType  editor screen open type
     */
    @Override
    public void setOpenType(WindowManager.OpenType openType) {
        this.openType = openType;
    }

    /**
     * @return  editor screen identifier
     */
    public String getWindowId() {
        if (windowId != null) {
            return windowId;
        } else {
            MetaClass metaClass = owner.getDatasource().getMetaClass();
            WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
            return windowConfig.getEditorScreenId(metaClass);
        }
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
}