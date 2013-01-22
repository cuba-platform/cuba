/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 20.01.2010 14:51:26
 *
 * $Id$
 */
package com.haulmont.cuba.web.app;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LinkColumnHelper {

    public interface Handler {
        void onClick(Entity entity);
    }

    public static void initColumn(Table table, final String propertyName, final Handler handler) {
        final CollectionDatasource ds = table.getDatasource();
        MetaPropertyPath nameProperty = ds.getMetaClass().getPropertyEx(propertyName);
        final com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(table);

        vTable.addGeneratedColumn(nameProperty, new com.vaadin.ui.Table.ColumnGenerator() {
            public Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                final Instance enclosingEntity = ds.getItem(itemId);
                if (enclosingEntity != null) {
                    //process properties like building.house.room
                    String[] props = propertyName.split("\\.");
                    Instance nestedEntity = enclosingEntity;
                    for (int i = 0; i < props.length - 1; i++) {
                        nestedEntity = (Instance) nestedEntity.getValue(props[i]);
                        if (nestedEntity == null) break;
                    }
                    final Object value = (nestedEntity == null) ? null : nestedEntity.getValue(props[props.length - 1]);
                    if (value != null) {
                        String str;
                        Datatype datatype = Datatypes.get(value.getClass());
                        if (datatype != null) {
                            str = datatype.format(value, UserSessionProvider.getLocale());
                        } else {
                            str = value.toString();
                        }

                        Button button = new Button(str,
                                new Button.ClickListener() {
                                    public void buttonClick(Button.ClickEvent event) {
                                        handler.onClick((Entity) enclosingEntity);
                                    }
                                });
                        button.setStyleName("link");
                        return button;
                    }
                }
                return new Label();
            }
        });
    }

    public static void removeColumn(Table table, final String propertyName) {
        final CollectionDatasource ds = table.getDatasource();
        MetaPropertyPath nameProperty = ds.getMetaClass().getPropertyEx(propertyName);
        final com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(table);
        vTable.removeGeneratedColumn(nameProperty);
    }
}
