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
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.export.ExcelExporter;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
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
public class TableActionsHelper extends ListActionsHelper<Table> {

    private static final long serialVersionUID = -8710627236049217204L;

    public TableActionsHelper(IFrame frame, Table table) {
        super(frame, table);
    }

    public Action createCreateAction(final ValueProvider valueProvider, final WindowManager.OpenType openType) {
        AbstractAction action = new CreateAction(valueProvider, openType);
        component.addAction(action);
        return action;
    }

    public Action createCreateAction(final ValueProvider valueProvider, final WindowManager.OpenType openType, String actionMessage) {
        AbstractAction action = new CreateAction(valueProvider, openType, actionMessage);
        component.addAction(action);
        return action;
    }

    public Action createExcelAction(final ExportDisplay display) {
        AbstractAction action = new ExcelAction(display);
        component.addAction(action);
        return action;
    }

    public Action createParametrizedExcelAction(final ExportDisplay display) {
        AbstractAction action = new ParameterizedExcelAction(display);
        component.addAction(action);
        return action;
    }

    @Deprecated
    private class CreateAction extends AbstractAction {
        private final ValueProvider valueProvider;
        private final WindowManager.OpenType openType;
        private String actionMessage;

        public CreateAction(ValueProvider valueProvider, WindowManager.OpenType openType) {
            super("create");
            this.valueProvider = valueProvider;
            this.openType = openType;
            this.actionMessage = "actions.Create";
        }

        public CreateAction(ValueProvider valueProvider, WindowManager.OpenType openType, String actionMessage) {
            this(valueProvider, openType);
            this.actionMessage = actionMessage;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, actionMessage);
        }

        public boolean isEnabled() {
            return super.isEnabled() && userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE);
        }

        public void actionPerform(Component component) {
            final CollectionDatasource datasource = TableActionsHelper.this.component.getDatasource();
            final DataSupplier dataservice = datasource.getDataSupplier();
            final String windowID = datasource.getMetaClass().getName() + ".edit";

            final Entity item = dataservice.newInstance(datasource.getMetaClass());
            if (valueProvider.getValues() != null) {
                for (Map.Entry<String, Object> entry : valueProvider.getValues().entrySet()) {
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
                            TableActionsHelper.this.component.setSelected((Entity) item);
                            fireCreateEvent((Entity) item);
                        }
                    }
                    fireChildWindowClosedEvent(window);
                }
            });
        }
    }

    @Deprecated
    private class ExcelAction extends AbstractAction {
        private final ExportDisplay display;

        public ExcelAction(ExportDisplay display) {
            super("excel");
            this.display = display;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Excel");
        }

        public void actionPerform(Component component) {
            ExcelExporter exporter = new ExcelExporter();
            exporter.exportTable(TableActionsHelper.this.component, TableActionsHelper.this.component.getNotCollapsedColumns(), display);
        }
    }

    @Deprecated
    private class ParameterizedExcelAction extends AbstractAction {
        private final ExportDisplay display;

        public ParameterizedExcelAction(ExportDisplay display) {
            super("excel");
            this.display = display;
        }

        public String getCaption() {
            final String messagesPackage = AppConfig.getMessagesPack();
            return MessageProvider.getMessage(messagesPackage, "actions.Excel");
        }

        public void actionPerform(Component component) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("table", TableActionsHelper.this.component);
            params.put("exportDisplay", display);
            frame.openWindow("excelExport", WindowManager.OpenType.DIALOG, params);
        }
    }
}
