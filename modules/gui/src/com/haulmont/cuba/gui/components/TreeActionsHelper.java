/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.Map;
import java.util.Collections;

/**
 * @deprecated Use these actions directly:<br/>
 *     <ul>
 *     <li>{@link com.haulmont.cuba.gui.components.actions.RefreshAction}
 *     <li>{@link com.haulmont.cuba.gui.components.actions.EditAction}
 *     <li>{@link com.haulmont.cuba.gui.components.actions.RemoveAction}
 *     <li>etc.
 *     </ul>
 *     See also:
 *     <ul>
 *         <li>{@link com.haulmont.cuba.gui.ComponentsHelper#createActions(ListComponent)}
 *         <li>{@link com.haulmont.cuba.gui.ComponentsHelper#createActions(ListComponent, java.util.EnumSet)}
  *     </ul>
 */
@Deprecated
public class TreeActionsHelper extends ListActionsHelper<Tree>{

    private static final long serialVersionUID = 1841229918439188174L;

    public TreeActionsHelper(IFrame frame, Tree tree) {
        super(frame, tree);
    }

    public Action createCreateAction(final ValueProvider valueProvider, final WindowManager.OpenType openType) {
        AbstractAction action = new CreateAction(valueProvider, openType);
        component.addAction(action);
        return action;
    }

    @Deprecated
    private class CreateAction extends AbstractAction {
        private final ValueProvider valueProvider;
        private final WindowManager.OpenType openType;

        public CreateAction(ValueProvider valueProvider, WindowManager.OpenType openType) {
            super("create");
            this.valueProvider = valueProvider;
            this.openType = openType;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Create");
        }

        public boolean isEnabled() {
            return super.isEnabled() && userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE);
        }

        public void actionPerform(Component component) {
            final CollectionDatasource datasource = TreeActionsHelper.this.component.getDatasource();
            final String hierarchyProperty = TreeActionsHelper.this.component.getHierarchyProperty();
            final DataSupplier dataservice = datasource.getDataSupplier();

            Entity parentItem = datasource.getItem();

            //datasource.getItem() may contain deleted item
            if(parentItem != null && !datasource.containsItem(parentItem.getId()))
            {
                parentItem = null;
            }
            // if (parentItem == null) return;

            final Entity item = dataservice.<Entity>newInstance(datasource.getMetaClass());
            item.setValue(hierarchyProperty, parentItem);

            final String windowID = datasource.getMetaClass().getName() + ".edit";
            if (valueProvider.getValues() != null) {
                for (Map.Entry<String, Object> entry : valueProvider.getValues().entrySet()) {
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

            Map<String, Object> params = valueProvider.getParameters() != null ?
                    valueProvider.getParameters() : Collections.<String, Object>emptyMap();

            final Window window = frame.openEditor(windowID, item, openType, params, parentDs);
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
                            fireCreateEvent((Entity) item);
                        }
                    }
                    fireChildWindowClosedEvent(window);
                }
            });
        }
    }
}
