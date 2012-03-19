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

import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

import java.io.Serializable;
import java.util.Map;

public interface FieldGroup extends Component, Component.BelongToFrame, Component.HasCaption, Component.HasBorder,
        Component.Editable, Component.Expandable, Component.Collapsible, Component.HasSettings, Component.Validatable {

    String NAME = "fieldGroup";

    java.util.List<Field> getFields();
    Field getField(String id);

    void addField(Field field);
    void addField(Field field, int col);
    void removeField(Field field);

    Datasource getDatasource();
    void setDatasource(Datasource datasource);

    boolean isRequired(Field field);
    void setRequired(Field field, boolean required, String message);
    boolean isRequired(String fieldId);
    void setRequired(String fieldId, boolean required, String message);

    void addValidator(Field field, com.haulmont.cuba.gui.components.Field.Validator validator);
    void addValidator(String fieldId, com.haulmont.cuba.gui.components.Field.Validator validator);

    boolean isEditable(Field field);
    void setEditable(Field field, boolean editable);
    boolean isEditable(String fieldId);
    void setEditable(String fieldId, boolean editable);

    boolean isEnabled(Field field);
    void setEnabled(Field field, boolean enabled);
    boolean isEnabled(String fieldId);
    void setEnabled(String fieldId, boolean enabled);

    boolean isVisible(Field field);
    void setVisible(Field field, boolean visible);
    boolean isVisible(String fieldId);
    void setVisible(String fieldId, boolean visible);

    Object getFieldValue(Field field);
    void setFieldValue(Field field, Object value);
    Object getFieldValue(String fieldId);
    void setFieldValue(String fieldId, Object value);

    void setFieldCaption(String fieldId, String caption);

    void setCaptionAlignment(FieldCaptionAlignment captionAlignment);

    int getColumns();
    void setColumns(int cols);

    float getColumnExpandRatio(int col);
    void setColumnExpandRatio(int col, float ratio);

    void addCustomField(String fieldId, CustomFieldGenerator fieldGenerator);
    void addCustomField(Field field, CustomFieldGenerator fieldGenerator);

    void postInit();

    enum FieldCaptionAlignment {
        LEFT,
        TOP
    }

    public class Field implements HasXmlDescriptor, HasCaption, HasFomatter, Serializable {
        private String id;
        private String caption;
        private String description;
        private Formatter formatter;
        private Element element;
        private String width;
        private Datasource datasource;
        private String requiredError;
        
        private boolean custom;
        private boolean required; 
        
        private Class type;
        
        private static final long serialVersionUID = -148321034678616282L;

        public Field(String id) {
            this.id = id;
        }

        public String getId() {
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

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public String getRequiredError() {
            return requiredError;
        }

        public void setRequiredError(String requiredError) {
            this.requiredError = requiredError;
        }
    }

    public class FieldsValidationException extends ValidationException {
        private Map<Field, Exception> problemFields;

        public FieldsValidationException() {
        }

        public FieldsValidationException(String message) {
            super(message);
        }

        public FieldsValidationException(String message, Throwable cause) {
            super(message, cause);
        }

        public FieldsValidationException(Throwable cause) {
            super(cause);
        }

        public Map<Field, Exception> getProblemFields() {
            return problemFields;
        }

        public void setProblemFields(Map<Field, Exception> problemFields) {
            this.problemFields = problemFields;
        }
    }

    interface CustomFieldGenerator extends Serializable {
        Component generateField(Datasource datasource, Object propertyId);
    }
}
