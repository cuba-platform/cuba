/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 13:21:13
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.List;
import java.util.Set;

public interface Table extends Component, Component.ActionsOwner {
    <T> T getSingleSelected();
    Set getSelected();

    List<Column> getColumns();
    void addColumn(Column column);
    void removeColumn(Column column);

    CollectionDatasource getDatasource();
    void setDatasource(CollectionDatasource datasource);

    public class Column {
        protected Object id;
        protected String caption;

        protected Class type;

        public Column(Object id) {
            this.id = id;
        }

        public Object getId() {
            return id;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public Class getType() {
            return type;
        }

        public void setType(Class type) {
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Column column = (Column) o;

            return id.equals(column.id);

        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
}
