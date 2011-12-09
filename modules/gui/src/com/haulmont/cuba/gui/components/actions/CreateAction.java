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
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.AppConfig;
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

/**
 * Standard list action to create a new entity instance.
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
public class CreateAction extends AbstractAction {

    private static final long serialVersionUID = 442122838693493665L;

    public static final String ACTION_ID = ListActionType.CREATE.getId();

    protected final ListComponent holder;
    protected final WindowManager.OpenType openType;
    protected final CollectionDatasource datasource;

    /**
     * The simplest constructor. The action has default name and opens the editor screen in THIS tab.
     * @param holder    component containing this action
     */
    public CreateAction(ListComponent holder) {
        this(holder, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    /**
     * Constructor that allows to specify how the editor screen opens. The action has default name.
     * @param holder    component containing this action
     * @param openType  how to open the editor screen
     */
    public CreateAction(ListComponent holder, WindowManager.OpenType openType) {
        this(holder, openType, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name and how the editor screen opens.
     * @param holder    component containing this action
     * @param openType  how to open the editor screen
     * @param id        action name
     */
    public CreateAction(ListComponent holder, WindowManager.OpenType openType, String id) {
        super(id);
        this.holder = holder;
        this.openType = openType;
        datasource = holder.getDatasource();
    }

    /**
     * Returns the action's caption. Override to provide a specific caption.
     * @return  localized caption
     */
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Create");
    }

    /**
     * Whether the action is currently enabled. Override to provide specific behaviour.
     * @return  true if enabled
     */
    public boolean isEnabled() {
        return super.isEnabled() &&
                UserSessionProvider.getUserSession().isEntityOpPermitted(datasource.getMetaClass(), EntityOp.CREATE);
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     * @param component component invoking action
     */
    public void actionPerform(Component component) {
        final DataService dataservice = datasource.getDataService();

        final Entity item = dataservice.newInstance(datasource.getMetaClass());

        if (holder instanceof Tree) {
            String hierarchyProperty = ((Tree) holder).getHierarchyProperty();

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

        final Window window = holder.getFrame().openEditor(getWindowId(), item, openType, params, parentDs);

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
                        holder.setSelected((Entity) item);
                        afterCommit((Entity) item);
                    }
                }
                afterWindowClosed(window);
            }
        });
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
     * Provides initial values for attributes of creating entity. Override to provide a specific value.
     * @return  a map of attribute name to an initial value
     */
    protected Map<String, Object> getInitialValues() {
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
}
