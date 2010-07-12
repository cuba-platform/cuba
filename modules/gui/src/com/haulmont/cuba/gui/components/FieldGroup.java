/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 22.06.2010 15:42:19
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

import java.io.Serializable;

public interface FieldGroup extends Component, Component.BelongToFrame,
        Component.HasCaption, Component.Editable, Component.Expandable, Component.HasSettings {

    java.util.List<Field> getFields();
    Field getField(String id);

    void addField(Field field);
    void addField(Field field, int col);
    void removeField(Field field);

    void setDatasource(Datasource datasource);

    boolean isRequired(Field field);
    void setRequired(Field field, boolean required, String message);

    void addValidator(Field field, com.haulmont.cuba.gui.components.Field.Validator validator);

    boolean isSwitchable();
    void setSwitchable(boolean switchable);

    boolean isExpanded();
    void setExpanded(boolean expanded);

    boolean isEditable(Field field);
    void setEditable(Field field, boolean editable);

    void setCaptionAlignment(FieldCaptionAlignment captionAlignment);

    int getColumns();
    void setColumns(int cols);

    float getColumnExpandRatio(int col);
    void setColumnExpandRatio(int col, float ratio);

    void addCustomField(Field field, CustomFieldGenerator fieldGenerator);

    enum FieldCaptionAlignment {
        LEFT,
        TOP
    }

    public class Field implements HasXmlDescriptor, HasCaption, HasFomatter, Serializable {
        private Object id;
        private String caption;
        private String description;
        private Formatter formatter;
        private Element element;
        private String width;
        private Datasource datasource;

        private boolean custom;

        private Class type;
        
        private static final long serialVersionUID = -148321034678616282L;

        public Field(Object id) {
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Formatter getFormatter() {
            return formatter;
        }

        public void setFormatter(Formatter formatter) {
            this.formatter = formatter;
        }

        public Element getXmlDescriptor() {
            return element;
        }

        public void setXmlDescriptor(Element element) {
            this.element = element;
        }

        public Class getType() {
            return type;
        }

        public void setType(Class type) {
            this.type = type;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public Datasource getDatasource() {
            return datasource;
        }

        public void setDatasource(Datasource datasource) {
            this.datasource = datasource;
        }

        public boolean isCustom() {
            return custom;
        }

        public void setCustom(boolean custom) {
            this.custom = custom;
        }
    }

    interface ExpandListener extends Serializable {
        void onExpand(FieldGroup component);
    }

    interface CollapseListener extends Serializable {
        void onCollapse(FieldGroup component);
    }

    interface CustomFieldGenerator extends Serializable {
        Component generateField(Datasource datasource, Object propertyId);
    }
}
