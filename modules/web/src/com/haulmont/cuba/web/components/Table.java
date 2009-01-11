/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 14:34:57
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.data.CollectionDatasourceWrapper;
import com.haulmont.chile.core.model.MetaProperty;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public class Table
    extends
        AbstractComponent<com.itmill.toolkit.ui.Table>
    implements
        com.haulmont.cuba.gui.components.Table, Component.Wrapper
{
    public Table() {
        component = new com.itmill.toolkit.ui.Table();
    }

    public <T> T getSingleSelected() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List getSelected() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Column> getColumns() {
        final List<Column> res = new ArrayList<Column>();

        final Collection columns = component.getContainerPropertyIds();
        for (Object columnId : columns) {
            final Column column = new Column(columnId);
            column.setType(component.getType(columnId));

            res.add(column);
        }

        return res;
    }

    public void addColumn(Column column) {
        component.addContainerProperty(column.getId(), column.getType(), null);
    }

    public void removeColumn(Column column) {
        component.removeContainerProperty(column.getId());
    }

    public void setDatasource(CollectionDatasource datasource) {
        final CollectionDatasourceWrapper ds =
                new CollectionDatasourceWrapper(datasource);

        component.setContainerDataSource(ds);
        for (MetaProperty metaProperty : (Collection<MetaProperty>)ds.getContainerPropertyIds()) {
            component.setColumnHeader(metaProperty, StringUtils.capitalize(metaProperty.getName()));
        }
    }
}
