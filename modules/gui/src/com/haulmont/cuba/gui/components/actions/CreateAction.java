/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Standard list action to create a new entity instance.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor, setting properties, or overriding
 * methods {@link #afterCommit(com.haulmont.cuba.core.entity.Entity)}, {@link #afterWindowClosed(com.haulmont.cuba.gui.components.Window)}
 *
 * @author krivopustov
 * @version $Id$
 */
public class CreateAction extends BaseAction implements Action.HasOpenType {

    public static final String ACTION_ID = ListActionType.CREATE.getId();

    protected WindowManager.OpenType openType;

    protected String windowId;
    protected Map<String, Object> windowParams;
    protected Map<String, Object> initialValues;

    protected Metadata metadata;

    protected AfterCommitHandler afterCommitHandler;

    protected AfterWindowClosedHandler afterWindowClosedHandler;

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
    public CreateAction(ListComponent target) {
        this(target, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    /**
     * Constructor that allows to specify how the editor screen opens. The action has default name.
     * @param target    component containing this action
     * @param openType  how to open the editor screen
     */
    public CreateAction(ListComponent target, WindowManager.OpenType openType) {
        this(target, openType, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name and how the editor screen opens.
     * @param target    component containing this action
     * @param openType  how to open the editor screen
     * @param id        action name
     */
    public CreateAction(ListComponent target, WindowManager.OpenType openType, String id) {
        super(id, null);

        this.target = target;
        this.openType = openType;
        this.caption = messages.getMainMessage("actions.Create");

        ThemeConstantsManager thCM = AppBeans.get(ThemeConstantsManager.NAME);
        this.icon = thCM.getThemeValue("actions.Create.icon");
        this.metadata = AppBeans.get(Metadata.NAME);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setShortcut(clientConfig.getTableInsertShortcut());
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
        MetaClass metaClass = ownerDatasource.getMetaClass();
        Security security = AppBeans.get(Security.NAME);
        boolean createPermitted = security.isEntityOpPermitted(metaClass, EntityOp.CREATE);

        if (createPermitted && ownerDatasource instanceof PropertyDatasource) {
            PropertyDatasource propertyDatasource = (PropertyDatasource) ownerDatasource;

            MetaClass parentMetaClass = propertyDatasource.getMaster().getMetaClass();
            MetaProperty metaProperty = propertyDatasource.getProperty();

            createPermitted = security.isEntityAttrPermitted(parentMetaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);
        }

        return createPermitted;
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     *
     * @param component component invoking action
     */
    @Override
    public void actionPerform(Component component) {
        final CollectionDatasource datasource = target.getDatasource();
        final DataSupplier dataservice = datasource.getDataSupplier();

        final Entity item = dataservice.newInstance(datasource.getMetaClass());

        // instantiate embedded fields
        Collection<MetaProperty> properties = datasource.getMetaClass().getProperties();
        for (MetaProperty property : properties) {
            if (!property.isReadOnly() && property.getAnnotations().containsKey("embedded")) {
                if (item.getValue(property.getName()) == null) {
                    Entity defaultEmbeddedInstance = dataservice.newInstance(property.getRange().asClass());
                    item.setValue(property.getName(), defaultEmbeddedInstance);
                }
            }
        }

        if (target instanceof Tree) {
            String hierarchyProperty = ((Tree) target).getHierarchyProperty();

            Entity parentItem = datasource.getItem();
            // datasource.getItem() may contain deleted item
            if (parentItem != null && !datasource.containsItem(parentItem.getId())) {
                parentItem = null;
            }

            item.setValue(hierarchyProperty, parentItem);
        }

        if (datasource instanceof NestedDatasource) {
            // Initialize reference to master entity
            Datasource masterDs = ((NestedDatasource) datasource).getMaster();
            MetaProperty metaProperty = ((NestedDatasource) datasource).getProperty();
            if (masterDs != null && metaProperty != null) {
                MetaProperty inverseProp = metaProperty.getInverse();
                if (inverseProp != null) {
                    ExtendedEntities extendedEntities = metadata.getExtendedEntities();

                    Class inversePropClass = extendedEntities.getEffectiveClass(inverseProp.getDomain());
                    Class dsClass = extendedEntities.getEffectiveClass(datasource.getMetaClass());
                    if (inversePropClass.isAssignableFrom(dsClass)) {
                        item.setValue(inverseProp.getName(), masterDs.getItem());
                    }
                }
            }
        }

        Map<String, Object> values = getInitialValues();
        if (values != null) {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                item.setValue(entry.getKey(), entry.getValue());
            }
        }

        Datasource parentDs = null;
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

        final Window window = target.getFrame().openEditor(getWindowId(), item, getOpenType(), params, parentDs);

        window.addListener(new Window.CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                    Entity item = ((Window.Editor) window).getItem();
                    if (item != null) {
                        if (pDs == null) {
                            boolean modified = datasource.isModified();
                            datasource.addItem(item);
                            ((DatasourceImplementation) datasource).setModified(modified);
                        }
                        target.setSelected(item);
                        afterCommit(item);
                        if (afterCommitHandler != null) {
                            afterCommitHandler.handle(item);
                        }
                    }
                }

                // move focus to owner
                target.requestFocus();

                afterWindowClosed(window);
                if (afterWindowClosedHandler != null) {
                    afterWindowClosedHandler.handle(window);
                }
            }
        });
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
     * @return  map of initial values for attributes of created entity
     */
    public Map<String, Object> getInitialValues() {
        return initialValues;
    }

    /**
     * @param initialValues map of initial values for attributes of created entity
     */
    public void setInitialValues(Map<String, Object> initialValues) {
        this.initialValues = initialValues;
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
}