/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 26.05.2010 16:52:04
 *
 * $Id$
 */
package cuba.client.web.ui.report.browse;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.web.gui.components.WebTable;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;

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
        CollectionDatasourceImpl cds = new CollectionDatasourceImpl(getDsContext(), getDsContext().getDataService(), "mainDs", metaClass, "_minimal");
        WebTable table = new WebTable() {
            @Override
            protected void initComponent(com.haulmont.cuba.web.toolkit.ui.Table component) {
                super.initComponent(component);
                for (MetaProperty prop : metaClass.getOwnProperties()) {//todo: reimplement - not all properties
                    MetaPropertyPath mpp = new MetaPropertyPath(metaClass, prop);
                    Table.Column column = new Table.Column(mpp);
                    column.setCaption(MessageProvider.getMessage(javaClass, javaClass.getSimpleName() + "." + prop.getName()));
                    addColumn(column);
                }
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
