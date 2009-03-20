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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;

import java.util.Collection;
import java.util.Map;

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
                return true;
            }

            public void actionPerform(Component component) {
                final CollectionDatasource datasource = TableActionsHelper.this.component.getDatasource();
                final DataService dataservice = datasource.getDataService();
                final String windowID = datasource.getMetaClass().getName() + ".edit";

                final Entity item = dataservice.<Entity>newInstance(datasource.getMetaClass());
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
                final Window window = frame.openEditor(windowID, item, openType);
                window.addListener(new Window.CloseListener() {
                    public void windowClosed(String actionId) {
                        if ("commit".equals(actionId)) {
                            datasource.refresh();
                        }
                    }
                });
            }
        };
        TableActionsHelper.this.component.addAction(action);

        return action;
    }
}
