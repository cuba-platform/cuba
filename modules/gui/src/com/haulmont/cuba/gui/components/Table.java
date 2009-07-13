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
import com.haulmont.cuba.core.entity.Entity;

import java.util.List;
import java.util.Set;

import org.dom4j.Element;

public interface Table
    extends
        com.haulmont.cuba.gui.components.List, Component.Editable, Component.HasSettings, Component.Expandable
{
    List<Column> getColumns();
    Column getColumn(String id);
    void addColumn(Column column);
    void removeColumn(Column column);

    void setDatasource(CollectionDatasource datasource);

    void setRequired(Column column, boolean required, String message);
    void addValidator(Column column, com.haulmont.cuba.gui.components.Field.Validator validator);

    void addValidator(com.haulmont.cuba.gui.components.Field.Validator validator);

    void setSortable(boolean sortable);
    boolean isSortable();

    public class Column implements HasXmlDescriptor, HasCaption {
        protected Object id;
        protected String caption;
        protected boolean editable;
        protected Formatter formatter;

        protected Class type;
        private Element element;

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

        public Boolean isEditable() {
            return editable;
        }

        public void setEditable(Boolean editable) {
            this.editable = editable;
        }

        public Class getType() {
            return type;
        }

        public void setType(Class type) {
            this.type = type;
        }

        public Formatter getFormatter() {
            return formatter;
        }

        public void setFormatter(Formatter formatter) {
            this.formatter = formatter;
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

        public Element getXmlDescriptor() {
            return element;
        }

        public void setXmlDescriptor(Element element) {
            this.element = element;
        }
    }

    public interface Formatter<T extends Object> {

        String format(T value);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    enum RowHeaderMode {
        NONE,
        ICON
    }

    void setRowHeaderMode(RowHeaderMode mode);

    interface StyleProvider {
        String getStyleName(Entity item, Object property);
        String getItemIcon(Entity item);
    }

    void setStyleProvider(StyleProvider styleProvider);
}
