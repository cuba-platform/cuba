/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.02.2009 17:18:03
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.export.ExcelExporter;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class TableActionsHelper extends ListActionsHelper<Table>{
    public TableActionsHelper(IFrame frame, Table table) {
        super(frame, table);
    }

    public Action createCreateAction(final ValueProvider valueProvider, final WindowManager.OpenType openType) {
        final AbstractAction action = new AbstractAction("create") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                return MessageProvider.getMessage(messagesPackage, "actions.Create");
            }

            public boolean isEnabled() {
                return userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE);
            }

            public void actionPerform(Component component) {
                final CollectionDatasource datasource = TableActionsHelper.this.component.getDatasource();
                final DataService dataservice = datasource.getDataService();
                final String windowID = datasource.getMetaClass().getName() + ".edit";

                final Entity item = dataservice.newInstance(datasource.getMetaClass());
                if (valueProvider.getValues() != null) {
                    for (Map.Entry<String, Object> entry : valueProvider.getValues().entrySet()) {
                        final Object value = entry.getValue();
                        if (value instanceof Collection) {
                            final Collection collection = (Collection) value;
                            if (collection.size() != 1) {
                                throw new UnsupportedOperationException();
                            } else {
                                ((Instance) item).setValue(entry.getKey(), collection.iterator().next());
                            }
                        } else {
                            ((Instance) item).setValue(entry.getKey(), value);
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

                Map<String, Object> params = valueProvider.getParameters() != null ?
                        valueProvider.getParameters() : Collections.<String, Object>emptyMap();

                final Window window = frame.openEditor(windowID, item, openType, params, parentDs);

                if (parentDs == null) {
                    window.addListener(new Window.CloseListener() {
                        public void windowClosed(String actionId) {
                            if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                                Object item = ((Window.Editor) window).getItem();
                                if (item instanceof Entity) {
                                    boolean modified = datasource.isModified();
                                    datasource.addItem((Entity) item);
                                    ((DatasourceImplementation) datasource).setModified(modified);
                                }
                            }
                        }
                    });
                }
            }
        };
        TableActionsHelper.this.component.addAction(action);

        return action;
    }

    public Action createExcelAction(final ExportDisplay display) {
        AbstractAction action = new AbstractAction("excel") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                return MessageProvider.getMessage(messagesPackage, "actions.Excel");
            }

            public void actionPerform(Component component) {
                ExcelExporter exporter = new ExcelExporter();
                exporter.exportTable(TableActionsHelper.this.component, display);
            }
        };
        TableActionsHelper.this.component.addAction(action);
        return action;
    }

    public Action createParametrizedExcelAction(final ExportDisplay display) {
        AbstractAction action = new AbstractAction("excel") {
            public String getCaption() {
                final String messagesPackage = AppConfig.getInstance().getMessagesPack();
                return MessageProvider.getMessage(messagesPackage, "actions.Excel");
            }

            public void actionPerform(Component component) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("table", TableActionsHelper.this.component);
                params.put("exportDisplay", display);                
                frame.openWindow("cuba$ExcelExport", WindowManager.OpenType.DIALOG, params);
            }
        };
        TableActionsHelper.this.component.addAction(action);
        return action;
    }

}
