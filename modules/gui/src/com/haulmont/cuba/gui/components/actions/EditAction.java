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
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.Collections;
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

    protected WindowManager.OpenType openType;

    protected String windowId;
    protected Map<String, Object> windowParams;

    // Set default caption only once
    protected boolean captionInitialized = false;

    protected AfterCommitHandler afterCommitHandler;

    protected AfterWindowClosedHandler afterWindowClosedHandler;

    protected Window.CloseListener editorCloseListener;

    public interface AfterCommitHandler {
        /**
        * @param entity    new committed entity instance
        */
        void handle(Entity entity);
    }

    public interface AfterWindowClosedHandler {
        /**
         * @param window    the editor window
         */
        void handle(Window window);
    }

    /**
     * The simplest constructor. The action has default name and opens the editor screen in THIS tab.
     * @param target    component containing this action
     */
    public EditAction(ListComponent target) {
        this(target, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    /**
     * Constructor that allows to specify how the editor screen opens. The action has default name.
     * @param target    component containing this action
     * @param openType  how to open the editor screen
     */
    public EditAction(ListComponent target, WindowManager.OpenType openType) {
        this(target, openType, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name and how the editor screen opens.
     * @param target    component containing this action
     * @param openType  how to open the editor screen
     * @param id        action name
     */
    public EditAction(ListComponent target, WindowManager.OpenType openType, String id) {
        super(id);

        this.target = target;
        this.openType = openType;

        ThemeConstantsManager thCM = AppBeans.get(ThemeConstantsManager.NAME);
        this.icon = thCM.getThemeValue("actions.Edit.icon");

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig config = configuration.getConfig(ClientConfig.class);
        setShortcut(config.getTableEditShortcut());
    }

    @Override
    public void setCaption(String caption) {
        super.setCaption(caption);

        captionInitialized = true;
    }

    @Override
    public void refreshState() {
        super.refreshState();

        if (target != null) {
            CollectionDatasource ds = target.getDatasource();

            if (ds != null && !captionInitialized) {
                Security security = AppBeans.get(Security.NAME);
                final String messagesPackage = AppConfig.getMessagesPack();
                if (security.isEntityOpPermitted(ds.getMetaClass(), EntityOp.UPDATE)) {
                    setCaption(messages.getMessage(messagesPackage, "actions.Edit"));
                } else {
                    setCaption(messages.getMessage(messagesPackage, "actions.View"));
                }
            }
        }
    }

    /**
     * Check permissions for Action
     */
    @Override
    protected boolean isPermitted() {
        if (target == null || target.getDatasource() == null) {
            return false;
        }

        CollectionDatasource ownerDatasource = target.getDatasource();
        Security security = AppBeans.get(Security.NAME);
        return security.isEntityOpPermitted(ownerDatasource.getMetaClass(), EntityOp.READ) && super.isPermitted();
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     * @param component component invoking action
     */
    @Override
    public void actionPerform(Component component) {
        final Set selected = target.getSelected();
        if (selected.size() == 1) {
            Datasource parentDs = null;
            final CollectionDatasource datasource = target.getDatasource();
            if (datasource instanceof PropertyDatasource) {
                MetaProperty metaProperty = ((PropertyDatasource) datasource).getProperty();
                if (metaProperty.getType().equals(MetaProperty.Type.COMPOSITION)) {
                    parentDs = datasource;
                }
            }

            Map<String, Object> params = getWindowParams();
            if (params == null) {
                params = Collections.emptyMap();
            }

            internalOpenEditor(datasource, datasource.getItem(), parentDs, params);
        }
    }

    protected void internalOpenEditor(CollectionDatasource datasource, Entity existingItem,
                                      Datasource parentDs, Map<String, Object> params) {

        Window.Editor window = target.getFrame().openEditor(getWindowId(), existingItem, getOpenType(), params, parentDs);
        if (editorCloseListener == null) {
            window.addCloseListener(actionId -> {
                // move focus to owner
                target.requestFocus();

                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    Entity editedItem = window.getItem();
                    if (editedItem != null) {
                        if (parentDs == null) {
                            //noinspection unchecked
                            datasource.updateItem(editedItem);
                        }
                        afterCommit(editedItem);
                        if (afterCommitHandler != null) {
                            afterCommitHandler.handle(editedItem);
                        }
                    }
                }

                afterWindowClosed(window);
                if (afterWindowClosedHandler != null) {
                    afterWindowClosedHandler.handle(window);
                }
            });
        } else {
            window.addCloseListener(editorCloseListener);
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
            MetaClass metaClass = target.getDatasource().getMetaClass();
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

    /**
     * @param afterCommitHandler handler that is invoked after the editor was commited and closed
     */
    public void setAfterCommitHandler(AfterCommitHandler afterCommitHandler) {
        this.afterCommitHandler = afterCommitHandler;
    }

    /**
     * @param afterWindowClosedHandler handler that is always invoked after the editor closed
     */
    public void setAfterWindowClosedHandler(AfterWindowClosedHandler afterWindowClosedHandler) {
        this.afterWindowClosedHandler = afterWindowClosedHandler;
    }

    /**
     * Overwrites default close listener for editor window.
     *
     * @param editorCloseListener new close listener
     */
    public void setEditorCloseListener(Window.CloseListener editorCloseListener) {
        this.editorCloseListener = editorCloseListener;
    }
}