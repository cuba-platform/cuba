/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.ui.FieldGroupLayout;
import com.haulmont.cuba.web.toolkit.ui.FieldWrapper;
import com.vaadin.ui.AbstractComponent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebFieldGroup
        extends
            WebAbstractComponent<com.haulmont.cuba.web.toolkit.ui.FieldGroup>
        implements
            com.haulmont.cuba.gui.components.FieldGroup {

    protected Map<String, FieldConfig> fields = new LinkedHashMap<>();
    protected Map<FieldConfig, Integer> fieldsColumn = new HashMap<>();
    protected Map<Integer, List<FieldConfig>> columnFields = new HashMap<>();

    protected Map<FieldConfig, Component> fieldComponents = new LinkedHashMap<>();

    protected Set<FieldConfig> readOnlyFields = new HashSet<>();

    protected Datasource<Entity> datasource;

    protected int cols = 1;

    protected final com.haulmont.cuba.gui.components.FieldFactory fieldFactory = new WebFieldGroupFieldFactory();

    protected Security security = AppBeans.get(Security.class);

    protected MessageTools messageTools = AppBeans.get(MessageTools.class);
    protected Messages messages = AppBeans.get(Messages.class);

    public WebFieldGroup() {
        component = new com.haulmont.cuba.web.toolkit.ui.FieldGroup() {
            @Override
            public void addField(Object propertyId, com.vaadin.ui.Field field) {
                FieldConfig fieldConf = WebFieldGroup.this.getField(propertyId.toString());
                if (fieldConf != null) {
                    int col = fieldsColumn.get(fieldConf);
                    List<FieldConfig> colFields = columnFields.get(col);
                    super.addField(propertyId.toString(), field, col, colFields.indexOf(fieldConf));
                } else {
                    super.addField(propertyId.toString(), field, 0);
                }
            }

            @Override
            public void addCustomField(Object propertyId, CustomFieldGenerator fieldGenerator) {
                FieldConfig fieldConf = WebFieldGroup.this.getField(propertyId.toString());
                int col = fieldsColumn.get(fieldConf);
                List<FieldConfig> colFields = columnFields.get(col);
                super.addCustomField(propertyId, fieldGenerator, col, colFields.indexOf(fieldConf));
            }
        };
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        if (id != null && App.getInstance().isTestMode()) {
            final List<FieldConfig> fieldConfs = getFields();
            for (final FieldConfig fieldConf : fieldConfs) {
                com.vaadin.ui.Field field = component.getField(fieldConf.getId());
                if (field instanceof AbstractComponent) {
                    ((AbstractComponent)field).setCubaId(fieldConf.getId());
                }
            }
        }
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);
        final List<FieldConfig> fieldConfs = getFields();
        for (final FieldConfig fieldConf : fieldConfs) {
            com.vaadin.ui.Field field = component.getField(fieldConf.getId());
            if (field != null) {
                field.setDebugId(id + ":" + fieldConf.getId());
            }
        }
    }

    @Override
    public List<FieldConfig> getFields() {
        return new ArrayList<>(fields.values());
    }

    @Override
    public FieldConfig getField(String id) {
        return fields.get(id);
    }

    @Override
    public Component getFieldComponent(String id) {
        FieldConfig fc = getField(id);
        return getFieldComponent(fc);
    }

    @Override
    public Component getFieldComponent(FieldConfig fieldConfig) {
        return fieldComponents.get(fieldConfig);
    }

    @Override
    public void addField(FieldConfig field) {
        fields.put(field.getId(), field);
        fieldsColumn.put(field, 0);
        fillColumnFields(0, field);
    }

    @Override
    public void addField(FieldConfig field, int col) {
        if (col < 0 || col >= cols) {
            throw new IllegalStateException(String.format("Illegal column number %s, available amount of columns is %s",
                    col, cols));
        }
        fields.put(field.getId(), field);
        fieldsColumn.put(field, col);
        fillColumnFields(col, field);
    }

    private void fillColumnFields(int col, FieldConfig field) {
        List<FieldConfig> fields = columnFields.get(col);
        if (fields == null) {
            fields = new ArrayList<>();

            columnFields.put(col, fields);
        }
        fields.add(field);
    }

    @Override
    public void removeField(FieldConfig field) {
        if (fields.remove(field.getId()) != null) {
            Integer col = fieldsColumn.get(field);

            final List<FieldConfig> fields = columnFields.get(col);
            fields.remove(field);
            fieldsColumn.remove(field);
        }
    }

    @Override
    public float getColumnExpandRatio(int col) {
        return component.getColumnExpandRatio(col);
    }

    @Override
    public void setColumnExpandRatio(int col, float ratio) {
        component.setColumnExpandRatio(col, ratio);
    }

    @Override
    public void setCaptionAlignment(FieldCaptionAlignment captionAlignment) {
        FieldGroupLayout layout = component.getContent();
        layout.setCaptionAlignment(WebComponentsHelper.convertFieldGroupCaptionAlignment(captionAlignment));
    }

    @Override
    public int getFieldCaptionWidth() {
        return -1;
    }

    @Override
    public void setFieldCaptionWidth(int fixedCaptionWidth) {
        LogFactory.getLog(getClass()).warn("setFieldCaptionWidth not implemented for web 6");
    }

    @Override
    public int getFieldCaptionWidth(int column) {
        return -1;
    }

    @Override
    public void setFieldCaptionWidth(int column, int width) {
        LogFactory.getLog(getClass()).warn("setFieldCaptionWidth not implemented for web 6");
    }

    @Override
    public void addCustomField(String fieldId, CustomFieldGenerator fieldGenerator) {
        FieldConfig fieldConf = getField(fieldId);
        if (fieldConf == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        addCustomField(fieldConf, fieldGenerator);
    }

    @Override
    public void addCustomField(final FieldConfig fieldConfig, final CustomFieldGenerator fieldGenerator) {
        if (!fieldConfig.isCustom()) {
            throw new IllegalStateException(String.format("Field '%s' must be defined as custom", fieldConfig.getId()));
        }
        component.addCustomField(fieldConfig.getId(), new com.haulmont.cuba.web.toolkit.ui.FieldGroup.CustomFieldGenerator() {
            @Override
            public com.vaadin.ui.Field generateField(Object propertyId, com.haulmont.cuba.web.toolkit.ui.FieldGroup component) {
                Datasource fieldDatasource;
                if (fieldConfig.getDatasource() != null) {
                    fieldDatasource = fieldConfig.getDatasource();
                } else {
                    fieldDatasource = datasource;
                }

                String id = (String) propertyId;

                Component fieldComponent = fieldGenerator.generateField(fieldDatasource, id);
                com.vaadin.ui.Field fieldImpl = getFieldImplementation(fieldComponent);
                if (fieldComponent instanceof WebCheckBox) {
                    ((com.haulmont.cuba.web.toolkit.ui.CheckBox) fieldImpl).setLayoutCaption(true);
                }

                assignTypicalAttributes(fieldComponent);

                if (fieldComponent instanceof Field) {
                    Field cubaField = (Field) fieldComponent;

                    String caption = fieldConfig.getCaption();
                    if (caption == null) {
                        MetaPropertyPath propertyPath =
                                fieldDatasource != null ? fieldDatasource.getMetaClass().getPropertyPath(id) : null;

                        if (propertyPath != null) {
                            caption = messageTools.getPropertyCaption(propertyPath.getMetaClass(), fieldConfig.getId());
                        }
                    }

                    if (StringUtils.isEmpty(cubaField.getCaption())) {
                        // if custom field hasn't manually set caption
                        cubaField.setCaption(caption);
                    }

                    if (fieldConfig.getDescription() != null && StringUtils.isEmpty(cubaField.getDescription())) {
                        // custom field hasn't manually set description
                        cubaField.setDescription(fieldConfig.getDescription());
                    }
                    if (fieldConfig.isRequired()) {
                        cubaField.setRequired(fieldConfig.isRequired());
                    }
                    if (fieldConfig.getRequiredError() != null) {
                        cubaField.setRequiredMessage(fieldConfig.getRequiredError());
                    }
                    if (!fieldConfig.isEditable()) {
                        cubaField.setEditable(fieldConfig.isEditable());
                    }
                }

                // some components (e.g. LookupPickerField) have width from the creation, so I commented out this check
                if (/*f.getWidth() == -1f &&*/ fieldConfig.getWidth() != null) {
                    fieldComponent.setWidth(fieldConfig.getWidth());
                } else {
                    fieldComponent.setWidth(DEFAULT_FIELD_WIDTH);
                }

                applyPermissions(fieldComponent);

                registerFieldComponent(fieldConfig, fieldComponent);

                if (App.getInstance().isTestMode() && fieldImpl instanceof AbstractComponent) {
                    ((AbstractComponent)fieldImpl).setCubaId(fieldConfig.getId());
                }

                return fieldImpl;
            }
        });
    }

    protected com.vaadin.ui.Field getFieldImplementation(Component c) {
        com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(c);
        if (composition instanceof com.vaadin.ui.Field) {
            return (com.vaadin.ui.Field) composition;
        } else {
            return new FieldWrapper(c);
        }
    }

    protected void assignTypicalAttributes(Component c) {
        if (c instanceof BelongToFrame) {
            BelongToFrame belongToFrame = (BelongToFrame) c;
            if (belongToFrame.getFrame() == null) {
                belongToFrame.setFrame(getFrame());
            }
        }
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;

        component.setCols(cols);

        if (datasource != null) {
            if (this.fields.isEmpty()) {
                LogFactory.getLog(getClass()).warn("Field group does not have fields");
            } else {
                component.setRows(rowsCount());
            }
        }

        assignAutoDebugId();

        createFields(datasource);
    }

    protected void createFields(Datasource datasource) {
        for (final String id : this.fields.keySet()) {
            final FieldConfig fieldConf = getField(id);
            if (!fieldConf.isCustom()) {
                Datasource fieldDatasource;

                if (datasource != null && fieldConf.getDatasource() == null) {
                    fieldDatasource = datasource;
                } else if (fieldConf.getDatasource() != null) {
                    fieldDatasource = fieldConf.getDatasource();
                } else {
                    throw new IllegalStateException(String.format("Unable to get datasource for field '%s'", id));
                }

                FieldBasket fieldBasket = createField(fieldDatasource, fieldConf);
                registerFieldComponent(fieldConf, fieldBasket.getField());

                com.vaadin.ui.Field composition = fieldBasket.getComposition();
                if (composition instanceof AbstractComponent && App.getInstance().isTestMode()) {
                    ((AbstractComponent)composition).setCubaId(fieldConf.getId());
                }

                component.addField(fieldConf.getId(), fieldBasket.getComposition());
            }
        }
    }

    protected void registerFieldComponent(FieldConfig fieldConf, Component field) {
        fieldComponents.put(fieldConf, field);
    }

    protected FieldBasket createField(Datasource fieldDatasource, FieldConfig fieldConf) {
        Component fieldComponent =
                fieldFactory.createField(fieldDatasource, fieldConf.getId(), fieldConf.getXmlDescriptor());

        com.vaadin.ui.Field fieldImpl = getFieldImplementation(fieldComponent);

        assignTypicalAttributes(fieldComponent);

        // move checkbox caption to captions column
        if (fieldImpl instanceof com.haulmont.cuba.web.toolkit.ui.CheckBox) {
            ((com.haulmont.cuba.web.toolkit.ui.CheckBox) fieldImpl).setLayoutCaption(true);
        }

        if (fieldComponent instanceof Field) {
            Field cubaField = (Field) fieldComponent;
            String caption = fieldConf.getCaption();
            if (caption == null) {
                MetaPropertyPath propertyPath = fieldDatasource.getMetaClass().getPropertyPath(fieldConf.getId());
                if (propertyPath != null) {
                    caption = messageTools.getPropertyCaption(propertyPath.getMetaClass(), fieldConf.getId());
                }
            }

            cubaField.setCaption(caption);

            if (fieldConf.getDescription() != null) {
                cubaField.setDescription(fieldConf.getDescription());
            }
            if (fieldConf.isRequired()) {
                cubaField.setRequired(fieldConf.isRequired());
            }
            if (fieldConf.getRequiredError() != null) {
                cubaField.setRequiredMessage(fieldConf.getRequiredError());
            }
            if (!fieldConf.isEditable()) {
                cubaField.setEditable(fieldConf.isEditable());
            }
        }

        if (fieldComponent instanceof HasFormatter) {
            ((HasFormatter) fieldComponent).setFormatter(fieldConf.getFormatter());
        }

        // some components (e.g. LookupPickerField) have width from the creation, so I commented out this check
        if (/*f.getWidth() == -1f &&*/ fieldConf.getWidth() != null) {
            fieldComponent.setWidth(fieldConf.getWidth());
        } else {
            fieldComponent.setWidth(DEFAULT_FIELD_WIDTH);
        }

        applyPermissions(fieldComponent);

        return new FieldBasket(fieldComponent, fieldImpl);
    }

    protected void applyPermissions(Component c) {
        if (c instanceof DatasourceComponent) {
            DatasourceComponent dsComponent = (DatasourceComponent) c;
            MetaProperty metaProperty = dsComponent.getMetaProperty();

            if (metaProperty != null) {
                MetaClass metaClass = dsComponent.getDatasource().getMetaClass();
                dsComponent.setEditable(security.isEntityAttrModificationPermitted(metaClass, metaProperty.getName())
                        && dsComponent.isEditable());
            }
        }
    }

    protected int rowsCount() {
        int rowsCount = 0;
        for (final List<FieldConfig> fields : columnFields.values()) {
            rowsCount = Math.max(rowsCount, fields.size());
        }
        return rowsCount;
    }

    @Override
    public int getColumns() {
        return cols;
    }

    @Override
    public void setColumns(int cols) {
        this.cols = cols;
    }

    @Override
    public void addValidator(FieldConfig field, Field.Validator validator) {
        Component component = fieldComponents.get(field);
        if (component instanceof Field) {
            ((Field) component).addValidator(validator);
        }
    }

    @Override
    public void addValidator(String fieldId, Field.Validator validator) {
        FieldConfig field = fields.get(fieldId);
        if (fieldId != null) {
            addValidator(field, validator);
        }
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        component.setDescription(description);
    }

    @Override
    public boolean isEditable() {
        return !component.isReadOnly();
    }

    @Override
    public void setEditable(boolean editable) {
        component.setReadOnly(!editable);
        // if we have editable field group with some read-only fields then we keep them read-only
        if (editable) {
            for (FieldConfig field : readOnlyFields) {
                com.vaadin.ui.Field f = component.getField(field.getId());
                f.setReadOnly(true);
            }
        }
    }

    @Override
    public boolean isRequired(FieldConfig field) {
        Component fieldComponent = fieldComponents.get(field);
        if (fieldComponent instanceof Field) {
            Field cubaField = (Field) fieldComponent;
            return cubaField.isRequired();
        } else {
            com.vaadin.ui.Field f = component.getField(field.getId());
            return f.isRequired();
        }
    }

    @Override
    public void setRequired(FieldConfig field, boolean required, String message) {
        Component fieldComponent = fieldComponents.get(field);
        if (fieldComponent instanceof Field) {
            Field cubaField = (Field) fieldComponent;
            cubaField.setRequired(required);
            cubaField.setRequiredMessage(message);
        } else {
            com.vaadin.ui.Field f = component.getField(field.getId());
            f.setRequired(required);
            if (required) {
                f.setRequiredError(message);
            }
        }
    }

    @Override
    public boolean isRequired(String fieldId) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        return isRequired(field);
    }

    @Override
    public void setRequired(String fieldId, boolean required, String message) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        setRequired(field, required, message);
    }

    @Override
    public boolean isEditable(FieldConfig field) {
        return !readOnlyFields.contains(field);
    }

    @Override
    public void setEditable(FieldConfig field, boolean editable) {
        Component fieldComponent = fieldComponents.get(field);
        if (fieldComponent instanceof Field) {
            Field cubaField = (Field) fieldComponent;
            cubaField.setEditable(editable);
        } else {
            com.vaadin.ui.Field f = component.getField(field.getId());
            f.setReadOnly(!editable);
        }

        if (editable) {
            readOnlyFields.remove(field);
        } else {
            readOnlyFields.add(field);
        }
    }

    @Override
    public void setEditable(String fieldId, boolean editable) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        setEditable(field, editable);
    }

    @Override
    public boolean isEditable(String fieldId) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        return isEditable(field);
    }

    @Override
    public boolean isEnabled(FieldConfig field) {
        com.vaadin.ui.Field f = component.getField(field.getId());
        return f.isEnabled();
    }

    @Override
    public void setEnabled(FieldConfig field, boolean enabled) {
        com.vaadin.ui.Field f = component.getField(field.getId());
        f.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled(String fieldId) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        return isEnabled(field);
    }

    @Override
    public void setEnabled(String fieldId, boolean enabled) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        setEnabled(field, enabled);
    }

    @Override
    public boolean isVisible(FieldConfig field) {
        com.vaadin.ui.Field f = component.getField(field.getId());
        return f.isVisible();
    }

    @Override
    public void setVisible(FieldConfig field, boolean visible) {
        com.vaadin.ui.Field f = component.getField(field.getId());
        f.setVisible(visible);
    }

    @Override
    public boolean isVisible(String fieldId) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        return isVisible(field);
    }

    @Override
    public void setVisible(String fieldId, boolean visible) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        setVisible(field, visible);
    }

    @Override
    public boolean isBorderVisible() {
        return component.isBorderVisible();
    }

    @Override
    public void setBorderVisible(boolean borderVisible) {
        component.setBorderVisible(borderVisible);
    }

    @Override
    public Object getFieldValue(FieldConfig field) {
        Component fieldComponent = fieldComponents.get(field);
        if (fieldComponent instanceof HasValue) {
            return ((HasValue) fieldComponent).getValue();
        }
        return null;
    }

    @Override
    public void setFieldValue(FieldConfig field, Object value) {
        Component fieldComponent = fieldComponents.get(field);
        if (fieldComponent instanceof HasValue) {
            ((HasValue) fieldComponent).setValue(value);
        }
    }

    @Override
    public Object getFieldValue(String fieldId) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        return getFieldValue(field);
    }

    @Override
    public void setFieldValue(String fieldId, Object value) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        setFieldValue(field, value);
    }

    @Override
    public void requestFocus(String fieldId) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        com.vaadin.ui.Field componentField = component.getField(field.getId());
        if (componentField != null) {
            componentField.focus();
        }
    }

    @Override
    public void setFieldCaption(String fieldId, String caption) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }

        com.vaadin.ui.Field f = component.getField(field.getId());
        f.setCaption(caption);
    }

    @Override
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Override
    public void validate() throws ValidationException {
        if (!isVisible() || !isEditable() || !isEnabled()) {
            return;
        }
        final Map<FieldConfig, Exception> problems = new LinkedHashMap<>();

        for (Map.Entry<FieldConfig, Component> componentEntry : fieldComponents.entrySet()) {
            FieldConfig field = componentEntry.getKey();
            Component component = componentEntry.getValue();

            if (!isEditable(field) || !isEnabled(field) || !isVisible(field)) {
                continue;
            }

            // If has valid state
            if ((component instanceof Validatable) &&
                    (component instanceof Editable)) {
                // If editable
                if (component.isVisible() &&
                        component.isEnabled() &&
                        ((Editable) component).isEditable()) {
                    try {
                        ((Validatable) component).validate();
                    } catch (ValidationException ex) {
                        problems.put(field, ex);
                    }
                }
            }
        }

        if (!problems.isEmpty()) {
            Map<FieldConfig, Exception> problemFields = new LinkedHashMap<>();
            for (Map.Entry<FieldConfig, Exception> entry : problems.entrySet()) {
                problemFields.put(entry.getKey(), entry.getValue());
            }

            StringBuilder msgBuilder = new StringBuilder();
            for (Iterator<FieldConfig> iterator = problemFields.keySet().iterator(); iterator.hasNext(); ) {
                FieldConfig field = iterator.next();
                Exception ex = problemFields.get(field);
                msgBuilder.append(ex.getMessage());
                if (iterator.hasNext()) {
                    msgBuilder.append("\n");
                }
            }

            FieldsValidationException validationException = new FieldsValidationException(msgBuilder.toString());
            validationException.setProblemFields(problemFields);

            throw validationException;
        }
    }

    @Override
    public void requestFocus() {
        for (Component component : fieldComponents.values()) {
            com.vaadin.ui.Component vComponent = WebComponentsHelper.unwrap(component);
            if (vComponent instanceof com.vaadin.ui.Component.Focusable) {
                ((com.vaadin.ui.Component.Focusable) vComponent).focus();
                break;
            }
        }
    }

    protected boolean isEmpty(Object value) {
        if (value instanceof String) {
            return StringUtils.isBlank((String) value);
        } else {
            return value == null;
        }
    }

    protected class WebFieldGroupFieldFactory extends com.haulmont.cuba.web.gui.components.AbstractFieldFactory {

        @Override
        protected CollectionDatasource getOptionsDatasource(Datasource datasource, String property) {
            final FieldConfig field = fields.get(property);

            Datasource ds = datasource;

            DsContext dsContext;
            if (ds == null) {
                ds = field.getDatasource();
                if (ds == null) {
                    throw new IllegalStateException("FieldGroup datasource is null");
                }
                dsContext = ds.getDsContext();
            } else {
                dsContext = ds.getDsContext();
            }
            Element descriptor = field.getXmlDescriptor();
            String optDsName = descriptor == null ? null : descriptor.attributeValue("optionsDatasource");

            if (!StringUtils.isBlank(optDsName)) {
                CollectionDatasource optDs = dsContext.get(optDsName);
                if (optDs == null) {
                    throw new IllegalStateException("Options datasource not found: " + optDsName);
                }
                return optDs;
            } else {
                return null;
            }
        }
    }

    protected class FieldBasket {

        private com.vaadin.ui.Field composition;

        private Component field;

        public FieldBasket(Component field, com.vaadin.ui.Field composition) {
            this.field = field;
            this.composition = composition;
        }

        public com.vaadin.ui.Field getComposition() {
            return composition;
        }

        public Component getField() {
            return field;
        }
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId())) {
            return "fieldGroup_" + datasource.getId();
        }

        return getClass().getSimpleName();
    }
}