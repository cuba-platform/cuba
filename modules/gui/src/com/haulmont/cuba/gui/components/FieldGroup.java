/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

import java.util.Map;

public interface FieldGroup extends Component, Component.BelongToFrame, Component.HasCaption, Component.HasBorder,
        Component.Editable, Component.Validatable {

    String NAME = "fieldGroup";

    String DEFAULT_FIELD_WIDTH = "200px";

    java.util.List<FieldConfig> getFields();
    FieldConfig getField(String id);

    Component getFieldComponent(String id);
    Component getFieldComponent(FieldConfig fieldConfig);
    java.util.List<Component> getOwnComponents();

    void addField(FieldConfig field);
    void addField(FieldConfig field, int col);
    void removeField(FieldConfig field);

    Datasource getDatasource();
    void setDatasource(Datasource datasource);

    boolean isRequired(FieldConfig field);
    void setRequired(FieldConfig field, boolean required, String message);
    void setRequired(FieldConfig field, boolean required);
    String getRequiredMessage(FieldConfig field);
    void setRequiredMessage(FieldConfig field, String message);
    boolean isRequired(String fieldId);
    void setRequired(String fieldId, boolean required, String message);
    void setRequired(String fieldId, boolean required);
    String getRequiredMessage(String fieldId);
    void setRequiredMessage(String fieldId, String message);

    void addValidator(FieldConfig field, com.haulmont.cuba.gui.components.Field.Validator validator);
    void addValidator(String fieldId, com.haulmont.cuba.gui.components.Field.Validator validator);

    boolean isEditable(FieldConfig field);
    void setEditable(FieldConfig field, boolean editable);
    boolean isEditable(String fieldId);
    void setEditable(String fieldId, boolean editable);

    boolean isEnabled(FieldConfig field);
    void setEnabled(FieldConfig field, boolean enabled);
    boolean isEnabled(String fieldId);
    void setEnabled(String fieldId, boolean enabled);

    boolean isVisible(FieldConfig field);
    void setVisible(FieldConfig field, boolean visible);
    boolean isVisible(String fieldId);
    void setVisible(String fieldId, boolean visible);

    Object getFieldValue(FieldConfig field);
    void setFieldValue(FieldConfig field, Object value);
    Object getFieldValue(String fieldId);
    void setFieldValue(String fieldId, Object value);

    /**
     * Try to acquire focus on field
     * @param fieldId field id
     */
    void requestFocus(String fieldId);

    String getFieldCaption(String fieldId);
    void setFieldCaption(String fieldId, String caption);

    void setCaptionAlignment(FieldCaptionAlignment captionAlignment);

    int getFieldCaptionWidth();
    void setFieldCaptionWidth(int fixedCaptionWidth);

    int getFieldCaptionWidth(int column);
    void setFieldCaptionWidth(int column, int width);

    int getColumns();
    void setColumns(int cols);

    float getColumnExpandRatio(int col);
    void setColumnExpandRatio(int col, float ratio);

    void addCustomField(String fieldId, CustomFieldGenerator fieldGenerator);
    void addCustomField(FieldConfig field, CustomFieldGenerator fieldGenerator);

    enum FieldCaptionAlignment {
        LEFT,
        TOP
    }

    class FieldConfig implements HasXmlDescriptor, HasCaption, HasFormatter {
        private String id;
        private String caption;
        private String description;
        private Formatter formatter;
        private Element element;
        private String width;
        private String stylename;
        private Datasource datasource;
        private String requiredError;
        private MetaPropertyPath metaPropertyPath;
        
        private boolean custom;
        private boolean required;
        private boolean editable;
        
        private Class type;
        
        public FieldConfig(String id) {
            this.id = id;
            editable = true;
        }

        public String getId() {
            return id;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public void setCaption(String caption) {
            this.caption = caption;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public Formatter getFormatter() {
            return formatter;
        }

        @Override
        public void setFormatter(Formatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public Element getXmlDescriptor() {
            return element;
        }

        @Override
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

        public String getStyleName() {
            return stylename;
        }

        public void setStyleName(String stylename) {
            this.stylename = stylename;
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

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public String getRequiredError() {
            return requiredError;
        }

        public void setRequiredError(String requiredError) {
            this.requiredError = requiredError;
        }

        public MetaPropertyPath getMetaPropertyPath() {
            return metaPropertyPath;
        }

        public void setMetaPropertyPath(MetaPropertyPath metaPropertyPath) {
            this.metaPropertyPath = metaPropertyPath;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    class FieldsValidationException extends ValidationException {
        private Map<FieldConfig, Exception> problemFields;

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

        public Map<FieldConfig, Exception> getProblemFields() {
            return problemFields;
        }

        public void setProblemFields(Map<FieldConfig, Exception> problemFields) {
            this.problemFields = problemFields;
        }
    }

    /**
     * Allows to show an arbitrary field inside a {@link FieldGroup}. Implementors of this interface have to be passed
     * to one of <code>FieldGroup.addCustomField</code> methods.
     */
    interface CustomFieldGenerator {
        /**
         * Called by the {@link FieldGroup} to get a generated field instance.
         *
         * @param datasource    a datasource specified for the field or the whole FieldGroup in XML
         * @param propertyId    field identifier as defined in XML, with <code>custom</code> attribute set to true
         * @return  a component to be rendered for the field
         */
        Component generateField(Datasource datasource, String propertyId);
    }
}