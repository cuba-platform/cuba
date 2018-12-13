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

import com.haulmont.cuba.gui.components.Field.Validator;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Multi-column form component.
 */
public interface FieldGroup extends Component, Component.BelongToFrame, Component.HasCaption, Component.HasIcon,
                                    HasBorder, HasContextHelp, Component.Editable, Validatable,
                                    EditableChangeNotifier, ChildEditableController,
                                    ComponentContainer, HasSubParts {
    String NAME = "fieldGroup";

    /**
     * Create new field config.
     * Field will not be automatically added to layout use additional {@link #addField(FieldConfig)} call.
     *
     * @param id field id
     * @return field config
     */
    FieldConfig createField(String id);

    /**
     * @return all added field configs
     */
    List<FieldConfig> getFields();

    /**
     * @param column column index
     * @return added field configs of {@code column}
     */
    List<FieldConfig> getFields(int column);

    /**
     * @param column column index
     * @param row    row index
     * @return field config
     */
    FieldConfig getField(int column, int row);

    /**
     * Get field config by id.
     *
     * @param fieldId field id
     * @return field config or null
     */
    @Nullable
    FieldConfig getField(String fieldId);
    /**
     * Get field config by id.
     *
     * @param fieldId field id
     * @return field config. Throws exception if not found.
     */
    FieldConfig getFieldNN(String fieldId);

    /**
     * Append field to 1 column.
     *
     * @param field field config
     */
    void addField(FieldConfig field);
    /**
     * Append field to {@code colIndex} column.
     *
     * @param fieldConfig field config
     * @param colIndex column index
     */
    void addField(FieldConfig fieldConfig, int colIndex);
    /**
     * Insert field to {@code colIndex} column to {@code rowIndex} position.
     *
     * @param fieldConfig field config
     * @param colIndex column index
     * @param rowIndex row index
     */
    void addField(FieldConfig fieldConfig, int colIndex, int rowIndex);

    /**
     * Remove field by id.
     *
     * @param fieldId field id
     */
    void removeField(String fieldId);
    /**
     * Remove field associated with {@code fieldConfig}.
     *
     * @param fieldConfig field id
     */
    void removeField(FieldConfig fieldConfig);

    /**
     * Request focus on field. <br>
     * Throws exception if field is not found or field does not have component.
     *
     * @param fieldId field id
     * @deprecated Use {@link #focusField(String)} instead.
     */
    @Deprecated
    default void requestFocus(String fieldId) {
        focusField(fieldId);
    }

    /**
     * @deprecated Use {@link #focusFirstField()} instead.
     */
    @Deprecated
    default void requestFocus() {
        focusFirstField();
    }

    /**
     * Focus the first enabled, visible and editable field.
     */
    void focusFirstField();

    /**
     * Request focus on field. <br>
     * Throws exception if field is not found or field does not have component.
     *
     * @param fieldId field id
     */
    void focusField(String fieldId);

    /**
     * @return default datasource for declarative fields
     */
    Datasource getDatasource();
    /**
     * Set default datasource for declarative fields.
     *
     * @param datasource datasource
     */
    void setDatasource(Datasource datasource);

    /**
     * Create and bind components for all declarative fields.
     */
    void bind();

    /**
     * @return attached field components
     */
    List<Component> getOwnComponents();

    /**
     * @return alignment of field captions
     */
    FieldCaptionAlignment getCaptionAlignment();
    /**
     * Set alignment of field captions
     *
     * @param captionAlignment field captions alignment
     */
    void setCaptionAlignment(FieldCaptionAlignment captionAlignment);

    /**
     * @return fixed field caption width
     */
    int getFieldCaptionWidth();
    /**
     * Set fixed field captions width. Set -1 to use auto size.
     *
     * @param fixedCaptionWidth fixed field caption width
     */
    void setFieldCaptionWidth(int fixedCaptionWidth);

    /**
     * @param colIndex column index
     * @return fixed field caption width for column {@code colIndex}
     */
    int getFieldCaptionWidth(int colIndex);
    /**
     * Set fixed field captions width for column {@code colIndex}. Set -1 to use auto size.
     *
     * @param colIndex column index
     * @param width width
     */
    void setFieldCaptionWidth(int colIndex, int width);

    /**
     * @return column count
     */
    int getColumns();
    /**
     * Set column count.
     *
     * @param columns column count
     */
    void setColumns(int columns);

    /**
     * @param colIndex column index
     * @return column expand ratio
     */
    float getColumnExpandRatio(int colIndex);
    /**
     * Set column expand ratio.
     *
     * @param colIndex column index
     * @param ratio column expand ratio
     */
    void setColumnExpandRatio(int colIndex, float ratio);

    boolean isValid();

    void validate() throws ValidationException;

    /**
     * Field caption alignment.
     */
    enum FieldCaptionAlignment {
        LEFT,
        TOP
    }

    /**
     * Whether apply declarative defaults for custom field or not.
     */
    enum FieldAttachMode {
        APPLY_DEFAULTS,
        CUSTOM
    }

    /**
     * Configuration of a field. Used as declarative configuration object.
     * After component is set it can be used as Field API for a Component that does not implement {@link Field}.
     */
    interface FieldConfig extends HasXmlDescriptor, HasCaption, HasHtmlCaption,
            HasHtmlDescription, HasFormatter, HasInputPrompt {
        /**
         * @return id
         */
        String getId();

        /**
         * @return true if this field config is connected to the concrete component and cannot be reconfigured.
         */
        boolean isBound();

        /**
         * @return width
         */
        String getWidth();
        /**
         * Set width parameter. <br>
         * If {@link #isBound()} is true sets width to the connected Component.
         *
         * @param width width
         */
        void setWidth(String width);

        /**
         * @return style name
         */
        String getStyleName();
        /**
         * Set stylename parameter. <br>
         * If {@link #isBound()} is true sets stylename to the connected Component.
         *
         * @param stylename style name
         */
        void setStyleName(String stylename);

        /**
         * @return own datasource of a field or datasource of the parent FieldGroup
         */
        Datasource getTargetDatasource();
        /**
         * @return datasource
         */
        Datasource getDatasource();
        /**
         * Set datasource for declarative field. <br>
         * Throws exception if FieldConfig is already connected to Component.
         *
         * @param datasource datasource
         */
        void setDatasource(Datasource datasource);

        /**
         * @return true if field is required, null if not set for declarative field
         */
        Boolean isRequired();
        /**
         * Set required for declarative field. <br>
         * If {@link #isBound()} is true and Component implements {@link Field} then sets required to the connected Component.
         *
         * @param required required flag
         */
        void setRequired(Boolean required);

        /**
         * @return true if field is editable, null if not set for declarative field
         */
        Boolean isEditable();
        /**
         * Set editable for declarative field. <br>
         * If {@link #isBound()} is true and Component implements {@link Field} then sets editable to the connected Component.
         *
         * @param editable editable flag
         */
        void setEditable(Boolean editable);

        /**
         * @return true if field is enabled, null if not set for declarative field
         */
        Boolean isEnabled();
        /**
         * Set enabled for declarative field. <br>
         * If {@link #isBound()} is true then sets enabled to the connected Component.
         *
         * @param enabled enabled flag
         */
        void setEnabled(Boolean enabled);

        /**
         * @return true if field is visible, null if not set for declarative field
         */
        Boolean isVisible();
        /**
         * Set visible for declarative field. <br>
         * If {@link #isBound()} is true then sets visible to the connected Component.
         *
         * @param visible visible flag
         */
        void setVisible(Boolean visible);

        /**
         * @return property name
         */
        String getProperty();
        /**
         * Set property for declarative field. <br>
         * Throws exception if FieldConfig is already connected to Component.
         *
         * @param property property name
         */
        void setProperty(String property);

        /**
         * @return tab index
         */
        Integer getTabIndex();
        /**
         * Set tab index for declarative field. <br>
         * If {@link #isBound()} is true and Component implements {@link Focusable} then sets tab index to the connected Component.
         *
         * @param tabIndex tab index
         */
        void setTabIndex(Integer tabIndex);

        /**
         * @return required message
         * @deprecated Use {@link #getRequiredMessage()}
         */
        @Deprecated
        String getRequiredError();
        /**
         * @deprecated Use {@link #setRequiredMessage(String)}}
         */
        @Deprecated
        void setRequiredError(String requiredError);

        /**
         * @return true if field is marked as custom
         */
        boolean isCustom();
        /**
         * Set custom flag. <br>
         * If field is marked as custom then {@link #bind()} will not create Component for field even if it does not have connected Component.
         *
         * @param custom custom flag
         */
        void setCustom(boolean custom);

        /**
         * @return required message
         */
        String getRequiredMessage();
        /**
         * Set required message for declarative field. <br>
         * If {@link #isBound()} is true and Component implements {@link Field} then sets required message to the connected Component.
         *
         * @param requiredMessage required message
         */
        void setRequiredMessage(String requiredMessage);

        /**
         * @return bound component
         */
        @Nullable
        Component getComponent();
        /**
         * @return bound component. Throws exception if component is null.
         */
        Component getComponentNN();

        /**
         * Bind Component to this field config. Component cannot be changed if it is assigned. <br>
         * FieldConfig will apply default values for caption, description, width, required and other Field properties.
         * <p>
         * When used with custom="true", the datasource and the property should be set up manually.
         *
         * @param component component
         * @see FieldConfig#setComponent(Component, FieldAttachMode)
         */
        void setComponent(Component component);

        /**
         * Bind Component to this field config. Component cannot be changed if it is assigned. <br>
         * If {@code mode} is {@link FieldAttachMode#APPLY_DEFAULTS} then FieldConfig will apply default values for
         * caption, description, width, required and other Field properties otherwise it will not.
         * <p>
         * When used with custom="true", the datasource and the property should be set up manually.
         *
         * @param component component
         * @param mode field attach mode
         */
        void setComponent(Component component, FieldAttachMode mode);

        /**
         * Add validator for declarative field. <br>
         * If field is bound to Component and Component implements {@link Field} then {@code validator} will be added
         * to Component directly.
         *
         * @param validator validator
         */
        void addValidator(Consumer<?> validator);

        /**
         * Remove validator. <br>
         * If field is bound to Component and Component implements {@link Field} then {@code validator} will be removed
         * from Component directly.
         *
         * @param validator validator
         */
        void removeValidator(Consumer<?> validator);

        /**
         * Set options datasource for declarative field. <br>
         * If field is bound to Component and Component implements {@link OptionsField} then {@code optionsDatasource}
         * will be set to Component directly.
         *
         * @param optionsDatasource options datasource
         */
        void setOptionsDatasource(CollectionDatasource optionsDatasource);
        /**
         * @return options datasource
         */
        CollectionDatasource getOptionsDatasource();

        /**
         * @return context help text
         */
        String getContextHelpText();

        /**
         * Set context help text for declarative field.
         *
         * If {@link #isBound()} is true and Component implements {@link Field} then sets context help text
         * to the connected Component.
         *
         * @param contextHelpText context help text to be set
         */
        void setContextHelpText(String contextHelpText);

        /**
         * @return true if field accepts context help text in HTML format, null if not set for a declarative field
         */
        Boolean isContextHelpTextHtmlEnabled();

        /**
         * Defines if context help text can be presented as HTML.
         * <p>
         * If {@link #isBound()} is true and Component implements {@link Field} then sets this attribute
         * to the connected Component.
         *
         * @param enabled true if field accepts context help text in HTML format
         */
        void setContextHelpTextHtmlEnabled(Boolean enabled);

        /**
         * @return a context help icon click handler
         */
        Consumer<HasContextHelp.ContextHelpIconClickEvent> getContextHelpIconClickHandler();

        /**
         * Sets a context help icon click handler
         *
         * @param handler the handler to set
         */
        void setContextHelpIconClickHandler(Consumer<HasContextHelp.ContextHelpIconClickEvent> handler);
    }

    /**
     * Exception that is thrown from {@link #validate()}.
     * Contains validation exceptions for fields that have failed validation.
     */
    class FieldsValidationException extends ValidationException {
        private Map<Validatable, ValidationException> problemFields;

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

        public Map<Validatable, ValidationException> getProblemFields() {
            return problemFields;
        }

        public void setProblemFields(Map<Validatable, ValidationException> problemFields) {
            this.problemFields = problemFields;
        }
    }

    /**
     * @return field factory for declarative fields
     */
    FieldGroupFieldFactory getFieldFactory();
    /**
     * Set field factory for declarative fields.
     *
     * @param fieldFactory field factory
     */
    void setFieldFactory(FieldGroupFieldFactory fieldFactory);

    @Override
    default boolean isValidateOnCommit() {
        return false;
    }

    @Nullable
    @Override
    default Object getSubPart(String name) {
        return getField(name);
    }

    /*
     * Deprecated API
     */

    @Deprecated
    default Component getFieldComponent(String id) {
        return getFieldNN(id).getComponent();
    }
    @Deprecated
    default Component getFieldComponent(FieldConfig fieldConfig) {
        return fieldConfig.getComponent();
    }

    /**
     * @deprecated Use {@code FieldConfig.isRequired()} instead.
     */
    @Deprecated
    default boolean isRequired(FieldConfig fc) {
        Boolean required = fc.isRequired();
        return required == null ? false : required;
    }
    /**
     * @deprecated Use {@code FieldConfig.setRequired(required)} instead.
     */
    @Deprecated
    default void setRequired(FieldConfig field, boolean required, String message) {
        field.setRequired(required);
        field.setRequiredMessage(message);
    }
    /**
     * @deprecated Use {@code FieldConfig.setRequired(required)} instead.
     */
    @Deprecated
    default void setRequired(FieldConfig field, boolean required) {
        field.setRequired(required);
    }
    /**
     * @deprecated Use {@code FieldConfig.getRequiredMessage()} instead.
     */
    @Deprecated
    default String getRequiredMessage(FieldConfig field) {
        return field.getRequiredMessage();
    }
    /**
     * @deprecated Use {@code FieldConfig.setRequiredMessage(message)} instead.
     */
    @Deprecated
    default void setRequiredMessage(FieldConfig field, String message) {
        field.setRequiredMessage(message);
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).isRequired()} instead.
     */
    @Deprecated
    default boolean isRequired(String fieldId) {
        return isRequired(getFieldNN(fieldId));
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).setRequired(required)} instead.
     */
    @Deprecated
    default void setRequired(String fieldId, boolean required, String message) {
        setRequired(getFieldNN(fieldId), required, message);
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).setRequired(fieldId)} instead.
     */
    @Deprecated
    default void setRequired(String fieldId, boolean required) {
        setRequired(getFieldNN(fieldId), required);
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).getRequiredMessage()} instead.
     */
    @Deprecated
    default String getRequiredMessage(String fieldId) {
        return getRequiredMessage(getFieldNN(fieldId));
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).setRequiredMessage(message)} instead.
     */
    @Deprecated
    default void setRequiredMessage(String fieldId, String message) {
        setRequiredMessage(getFieldNN(fieldId), message);
    }
    /**
     * @deprecated Use {@code FieldConfig.addValidator(validator)} instead.
     */
    @Deprecated
    default void addValidator(FieldConfig field, Validator validator) {
        field.addValidator(validator);
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).addValidator(validator)} instead.
     */
    @Deprecated
    default void addValidator(String fieldId, Validator validator) {
        getFieldNN(fieldId).addValidator(validator);
    }

    /**
     * @deprecated Use {@code FieldConfig.isEditable()} instead.
     */
    @Deprecated
    default boolean isEditable(FieldConfig field) {
        Boolean editable = field.isEditable();
        return editable == null ? true : editable;
    }
    /**
     * @deprecated Use {@code FieldConfig.setEditable(editable)} instead.
     */
    @Deprecated
    default void setEditable(FieldConfig field, boolean editable) {
        field.setEditable(editable);
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).isEditable()}
     */
    @Deprecated
    default boolean isEditable(String fieldId) {
        return isEditable(getFieldNN(fieldId));
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).setEditable(editable)} instead.
     */
    @Deprecated
    default void setEditable(String fieldId, boolean editable) {
        setEditable(getFieldNN(fieldId), editable);
    }

    /**
     * @deprecated Use {@code FieldConfig.isEnabled()} instead.
     */
    @Deprecated
    default boolean isEnabled(FieldConfig fc) {
        Boolean enabled = fc.isEnabled();
        return enabled == null ? true : enabled;
    }
    /**
     * @deprecated Use {@code FieldConfig.setEnabled(enabled)} instead.
     */
    @Deprecated
    default void setEnabled(FieldConfig fc, boolean enabled) {
        fc.setEnabled(enabled);
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).isEnabled()}
     */
    @Deprecated
    default boolean isEnabled(String fieldId) {
        return isEnabled(getFieldNN(fieldId));
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).setEnabled(enabled)} instead.
     */
    @Deprecated
    default void setEnabled(String fieldId, boolean enabled) {
        setEnabled(getFieldNN(fieldId), enabled);
    }

    /**
     * @deprecated Use {@code FieldConfig.isVisible()} instead.
     */
    @Deprecated
    default boolean isVisible(FieldConfig fc) {
        Boolean visible = fc.isVisible();
        return visible == null ? true : visible;
    }
    /**
     * @deprecated Use {@code FieldConfig.setVisible(visible)} instead.
     */
    @Deprecated
    default void setVisible(FieldConfig fc, boolean visible) {
        fc.setVisible(visible);
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN("fieldId").isVisible()}
     */
    @Deprecated
    default boolean isVisible(String fieldId) {
        return isVisible(getFieldNN(fieldId));
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).setVisible(visible)} instead.
     */
    @Deprecated
    default void setVisible(String fieldId, boolean visible) {
        setVisible(getFieldNN(fieldId), visible);
    }

    /**
     * @deprecated Inject field instance and use typed component instead of this method.
     */
    @Deprecated
    default Object getFieldValue(FieldConfig fc) {
        return ((HasValue) fc.getComponentNN()).getValue();
    }
    /**
     * @deprecated Inject field instance and use typed component instead of this method.
     */
    @Deprecated
    default void setFieldValue(FieldConfig fc, Object value) {
        ((HasValue) fc.getComponentNN()).setValue(value);
    }
    /**
     * @deprecated Inject field instance and use typed component instead of this method.
     */
    @Deprecated
    default Object getFieldValue(String fieldId) {
        return getFieldValue(getFieldNN(fieldId));
    }
    /**
     * @deprecated Inject field instance and use typed component instead of this method.
     */
    @Deprecated
    default void setFieldValue(String fieldId, Object value) {
        setFieldValue(getFieldNN(fieldId), value);
    }

    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).getCaption()} instead.
     */
    @Deprecated
    default String getFieldCaption(String fieldId) {
        return getFieldNN(fieldId).getCaption();
    }
    /**
     * @deprecated Use {@code FieldGroup.getFieldNN(fieldId).setCaption(caption)} instead.
     */
    @Deprecated
    default void setFieldCaption(String fieldId, String caption) {
        getFieldNN(fieldId).setCaption(caption);
    }

    /**
     * Generate and set Component to custom field.
     *
     * @deprecated Set Component implementation directly to {@link FieldConfig} using {@link FieldConfig#setComponent(Component)} method.
     *
     * @param field field config
     * @param fieldGenerator field generator
     */
    @Deprecated
    void addCustomField(FieldConfig field, CustomFieldGenerator fieldGenerator);
    /**
     * Generate and set Component to custom field.
     *
     * @deprecated Set Component implementation directly to {@link FieldConfig} using {@link FieldConfig#setComponent(Component)} method.
     *
     * @param fieldId field id
     * @param fieldGenerator field generator
     */
    @Deprecated
    void addCustomField(String fieldId, CustomFieldGenerator fieldGenerator);

    /**
     * Allows to show an arbitrary field inside a {@link FieldGroup}. Implementors of this interface have to be passed
     * to one of <code>FieldGroup.addCustomField</code> methods.
     *
     * @deprecated Set component implementation directly to {@link FieldConfig} using {@link FieldConfig#setComponent(Component)} method.
     */
    @Deprecated
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