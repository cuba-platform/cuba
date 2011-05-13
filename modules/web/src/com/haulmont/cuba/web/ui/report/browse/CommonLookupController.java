/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 26.05.2010 16:52:04
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui.report.browse;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebLabel;
import com.haulmont.cuba.web.gui.components.WebTable;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import com.vaadin.ui.Component;

import java.util.ArrayList;
import java.util.Map;

public class CommonLookupController extends AbstractLookup {
    public CommonLookupController(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        WebVBoxLayout vbox = getComponent("vbox");
        final MetaClass metaClass = (MetaClass) params.get("param$class");
        final Class javaClass = metaClass.getJavaClass();
        setCaption(MessageProvider.getMessage(javaClass, javaClass.getSimpleName()));
        final CollectionDatasourceImpl cds = new CollectionDatasourceImpl(getDsContext(), getDsContext().getDataService(), "mainDs", metaClass, "_minimal");
        final WebTable table = new WebTable() {
            @Override
            protected void initComponent(com.haulmont.cuba.web.toolkit.ui.Table component) {
                super.initComponent(component);
                com.vaadin.ui.Table.ColumnGenerator generator = new com.vaadin.ui.Table.ColumnGenerator() {
                    public Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                        Object inst = cds.getItem(itemId);
                        WebLabel label = new WebLabel();
                        label.setValue(((Instance) inst).getInstanceName());
                        return WebComponentsHelper.unwrap(label);
                    }
                };

                MetaPropertyPath nameProperty = new MetaPropertyPath(metaClass, new ArrayList<MetaProperty>(metaClass.getOwnProperties()).get(0));
                Column column = new Column(nameProperty);
                column.setCaption("Name");
                addColumn(column);
                addGeneratedColumn(nameProperty, generator); //todo: govnokod. write ticket to gorodnov
            }
        };
        table.setId("table");
        table.setDatasource(cds);
        vbox.add(table);
        vbox.expand(table, "100%", "100%");
        table.setMultiSelect(true);
        table.refresh();
        this.setLookupComponent(table);
    }
}
