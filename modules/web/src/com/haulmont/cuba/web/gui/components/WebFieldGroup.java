/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaCheckBox;
import com.haulmont.cuba.web.toolkit.ui.CubaFieldGroup;
import com.haulmont.cuba.web.toolkit.ui.CubaFieldGroupLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaFieldWrapper;
import com.vaadin.data.Item;
import com.vaadin.data.util.converter.Converter;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.text.ParseException;
import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebFieldGroup
        extends
            WebAbstractComponent<CubaFieldGroup>
        implements
            com.haulmont.cuba.gui.components.FieldGroup {

    protected Map<String, FieldConfig> fields = new LinkedHashMap<>();
    protected Map<FieldConfig, Integer> fieldsColumn = new HashMap<>();
    protected Map<Integer, List<FieldConfig>> columnFields = new HashMap<>();

    protected Set<FieldConfig> readOnlyFields = new HashSet<>();

    protected Map<FieldConfig, List<com.haulmont.cuba.gui.components.Field.Validator>> fieldValidators = new HashMap<>();

    protected Datasource<Entity> datasource;
    protected FieldFactory fieldFactory = new WebFieldGroupFieldFactory();

    protected int cols = 1;

    protected Item itemWrapper;

    protected Security security = AppBeans.get(Security.class);

    protected MessageTools messageTools = AppBeans.get(MessageTools.class);
    protected Messages messages = AppBeans.get(Messages.class);

    public WebFieldGroup() {
        component = new CubaFieldGroup() {
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
        component.setLayout(new CubaFieldGroupLayout());
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);
        final List<FieldConfig> fieldConfs = getFields();
        for (final FieldConfig fieldConf : fieldConfs) {
            com.vaadin.ui.Field field = component.getField(fieldConf.getId());
            if (field != null) {
                field.setId(id + ":" + fieldConf.getId());
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
//        vaadin7
//        CubaFieldGroupLayout layout = component.getLayout();
//        layout.setCaptionAlignment(WebComponentsHelper.convertFieldGroupCaptionAlignment(captionAlignment));
    }

    @Override
    public void addCustomField(String fieldId, CustomFieldGenerator fieldGenerator) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        addCustomField(field, fieldGenerator);
    }

    @Override
    public void addCustomField(final FieldConfig fieldConf, final CustomFieldGenerator fieldGenerator) {
        if (!fieldConf.isCustom()) {
            throw new IllegalStateException(String.format("Field '%s' must be defined as custom", fieldConf.getId()));
        }

        component.addCustomField(fieldConf.getId(), new CubaFieldGroup.CustomFieldGenerator() {
            @Override
            public com.vaadin.ui.Field generateField(Object propertyId, CubaFieldGroup component) {
                Datasource fieldDatasource;
                if (fieldConf.getDatasource() != null) {
                    fieldDatasource = fieldConf.getDatasource();
                } else {
                    fieldDatasource = datasource;
                }

                String id = (String) propertyId;

                Component fieldComponent = fieldGenerator.generateField(fieldDatasource, id);
                com.vaadin.ui.Field fieldImpl = getFieldImplementation(fieldComponent);

                assignTypicalAttributes(fieldComponent);

                MetaPropertyPath propertyPath = fieldDatasource.getMetaClass().getPropertyPath(id);
                if (propertyPath != null && (fieldComponent instanceof DatasourceComponent)) {
                    DatasourceComponent datasourceComponent = (DatasourceComponent) fieldComponent;
                    if (datasourceComponent.getDatasource() == null) {
                        if (fieldConf.getDatasource() != null) {
                            datasourceComponent.setDatasource(fieldConf.getDatasource(), id);
                        } else {
                            datasourceComponent.setDatasource(getDatasource(), id);
                        }
                    }
                }

                if (fieldComponent instanceof Field) {
                    Field cubaField = (Field) fieldComponent;

                    String caption = fieldConf.getCaption();
                    if (caption == null) {
                        if (propertyPath != null) {
                            caption = messageTools.getPropertyCaption(propertyPath.getMetaClass(), fieldConf.getId());
                        }
                    }

                    if (StringUtils.isEmpty(cubaField.getCaption())) {
                        // if custom field hasn't manually set caption
                        cubaField.setCaption(caption);
                    }

                    if (fieldConf.getDescription() != null && StringUtils.isEmpty(cubaField.getDescription())) {
                        // custom field hasn't manually set description
                        cubaField.setDescription(fieldConf.getDescription());
                    }
                    if (fieldConf.isRequired()) {
                        cubaField.setRequired(fieldConf.isRequired());
                    }
                    if (fieldConf.getRequiredError() != null) {
                        cubaField.setRequiredMessage(fieldConf.getRequiredError());
                    }
                }

                // some components (e.g. LookupPickerField) have width from the creation, so I commented out this check
                if (/*f.getWidth() == -1f &&*/ fieldConf.getWidth() != null) {
                    fieldComponent.setWidth(fieldConf.getWidth());
                }

                applyPermissions(fieldComponent);

                return fieldImpl;
            }
        });
    }

    protected com.vaadin.ui.Field getFieldImplementation(Component c) {
        com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(c);
        if (composition instanceof com.vaadin.ui.Field) {
            return  (com.vaadin.ui.Field) composition;
        } else {
            return new CubaFieldWrapper(composition);
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

    @Override
    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;

        component.setCols(cols);

        MetadataTools metadataTools = AppBeans.get(MetadataTools.class);
        Collection<MetaPropertyPath> fieldsMetaProps = null;
        if (this.fields.isEmpty() && datasource != null) {//collects fields by entity view
            fieldsMetaProps = metadataTools.getViewPropertyPaths(datasource.getView(), datasource.getMetaClass());

            final ArrayList<MetaPropertyPath> propertyPaths = new ArrayList<>(fieldsMetaProps);
            for (final MetaPropertyPath propertyPath : propertyPaths) {
                MetaProperty property = propertyPath.getMetaProperty();
                if (property.getRange().getCardinality().isMany() || metadataTools.isSystem(property)) {
                    fieldsMetaProps.remove(propertyPath);
                }
            }

            component.setRows(fieldsMetaProps.size());

        } else {
            if (datasource != null) {
                final List<String> fieldIds = new ArrayList<>(this.fields.keySet());
                fieldsMetaProps = new ArrayList<>();
                for (final String id : fieldIds) {
                    final FieldConfig field = getField(id);
                    final MetaPropertyPath propertyPath = datasource.getMetaClass().getPropertyPath(field.getId());
                    final Element descriptor = field.getXmlDescriptor();
                    final String clickAction = (descriptor == null) ? (null) : (descriptor.attributeValue("clickAction"));
                    if (field.getDatasource() == null && propertyPath != null
                            && StringUtils.isEmpty(clickAction)) {
                        //fieldsMetaProps with attribute "clickAction" will be created manually
                        fieldsMetaProps.add(propertyPath);
                    }
                }
            }

            component.setRows(rowsCount());
        }

        if (datasource != null) {
            itemWrapper = createDatasourceWrapper(datasource, fieldsMetaProps);

            if (!this.fields.isEmpty()) {
                //Removes custom fieldsMetaProps from the list. We shouldn't to create components for custom fieldsMetaProps
                for (MetaPropertyPath propertyPath : new ArrayList<>(fieldsMetaProps)) {
                    final FieldConfig field = getField(propertyPath.toString());
                    if (field.isCustom()) {
                        fieldsMetaProps.remove(propertyPath);
                    }
                }
            }
        }

        createFields(datasource);
    }

    protected void createFields(Datasource datasource) {
        for (final String id : this.fields.keySet()) {
            final FieldConfig fieldConf = getField(id);
            if (!fieldConf.isCustom()) {
                com.vaadin.ui.Field field;

                Element descriptor = fieldConf.getXmlDescriptor();
                if (datasource != null && fieldConf.getDatasource() == null) {
                    field = createField(datasource, fieldConf);
                    component.addField(fieldConf.getId(), field);
                } else if (fieldConf.getDatasource() != null) {
                    field = createField(fieldConf.getDatasource(), fieldConf);
                    component.addField(fieldConf.getId(), field);
                } else {
                    throw new IllegalStateException(String.format("Unable to get datasource for field '%s'", id));
                }
            }
        }
    }

    protected com.vaadin.ui.Field createField(Datasource fieldDatasource, FieldConfig fieldConf) {
        Component fieldComponent =
                fieldFactory.createField(fieldDatasource, fieldConf.getId(), fieldConf.getXmlDescriptor());

        com.vaadin.ui.Field fieldImpl = getFieldImplementation(fieldComponent);

        assignTypicalAttributes(fieldComponent);

        // move checkbox caption to captions column
        if (fieldImpl instanceof CubaCheckBox) {
            ((CubaCheckBox) fieldImpl).setCaptionManagedByLayout(true);
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
        }

        if (fieldComponent instanceof HasFormatter) {
            ((HasFormatter) fieldComponent).setFormatter(fieldConf.getFormatter());
        }

        // some components (e.g. LookupPickerField) have width from the creation, so I commented out this check
        if (/*f.getWidth() == -1f &&*/ fieldConf.getWidth() != null) {
            fieldComponent.setWidth(fieldConf.getWidth());
        }

        applyPermissions(fieldComponent);

        return fieldImpl;
    }

    protected void applyPermissions(Component c) {
        if (c instanceof DatasourceComponent) {
            DatasourceComponent dsComponent = (DatasourceComponent) c;
            MetaProperty metaProperty = dsComponent.getMetaProperty();

            if (metaProperty != null) {
                dsComponent.setEditable(security.isEntityAttrModificationPermitted(metaProperty)
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

    protected Object convertRawValue(FieldConfig field, Object value) throws ValidationException {
        if (value instanceof String) {
            Datatype datatype = null;
            MetaPropertyPath propertyPath = null;

            if (field.getDatasource() != null) {
                propertyPath = field.getDatasource().getMetaClass().getPropertyPath(field.getId());
            } else if (datasource != null) {
                propertyPath = datasource.getMetaClass().getPropertyPath(field.getId());
            }

            if (propertyPath != null) {
                if (propertyPath.getMetaProperty().getRange().isDatatype()) {
                    datatype = propertyPath.getRange().asDatatype();
                }
            }

            if (datatype != null) {
                try {
                    return datatype.parse((String) value, AppBeans.get(UserSessionSource.class).getLocale());
                } catch (ParseException ignored) {
                    String message = messages.getMessage(WebWindow.class, "invalidValue");
                    String fieldCaption = messageTools.getPropertyCaption(propertyPath.getMetaProperty());
                    message = String.format(message, fieldCaption);
                    throw new ValidationException(message);
                }
            }
        }
        return value;
    }

    @Override
    public void addValidator(final FieldConfig field, final com.haulmont.cuba.gui.components.Field.Validator validator) {
        List<com.haulmont.cuba.gui.components.Field.Validator> validators = fieldValidators.get(field);
        if (validators == null) {
            validators = new ArrayList<>();
            fieldValidators.put(field, validators);
        }
        if (!validators.contains(validator)) {
            validators.add(validator);
        }
    }

    @Override
    public void addValidator(String fieldId, com.haulmont.cuba.gui.components.Field.Validator validator) {
        FieldConfig field = getField(fieldId);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        }
        addValidator(field, validator);
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
        com.vaadin.ui.Field f = component.getField(field.getId());
        return f.isRequired();
    }

    @Override
    public void setRequired(FieldConfig field, boolean required, String message) {
        com.vaadin.ui.Field f = component.getField(field.getId());
        f.setRequired(required);
        if (required) {
            f.setRequiredError(message);
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
        com.vaadin.ui.Field f = component.getField(field.getId());
        f.setReadOnly(!editable);
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
        com.vaadin.ui.Field f = component.getField(field.getId());
        return f.getValue();
    }

    @Override
    public void setFieldValue(FieldConfig field, Object value) {
        com.vaadin.ui.Field f = component.getField(field.getId());
        f.setValue(value);
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

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new FieldGroupItemWrapper(datasource, propertyPaths);
    }

    @Override
    public void postInit() {
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

    @Override
    public void validate() throws ValidationException {
        final Map<Object, Exception> problems = new HashMap<>();

        for (FieldConfig field : getFields()) {
            com.vaadin.ui.Field f = component.getField(field.getId());
            if (f != null && f.isVisible() && f.isEnabled() && !f.isReadOnly()) {
                Object value = convertRawValue(field, getFieldValue(field));
                if (isEmpty(value)) {
                    if (isRequired(field)) {
                        problems.put(field.getId(), new RequiredValueMissingException(f.getRequiredError(), this));
                    }
                } else {
                    List<com.haulmont.cuba.gui.components.Field.Validator> validators = fieldValidators.get(field);
                    if (validators != null) {
                        for (com.haulmont.cuba.gui.components.Field.Validator validator : validators) {
                            try {
                                validator.validate(value);
                            } catch (ValidationException e) {
                                problems.put(field.getId(), e);
                            }
                        }
                    }
                }
            }
        }

        if (!problems.isEmpty()) {
            Map<FieldConfig, Exception> problemFields = new HashMap<>();
            for (Map.Entry<Object, Exception> entry : problems.entrySet()) {
                problemFields.put(getField(entry.getKey().toString()), entry.getValue());
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

    protected boolean isEmpty(Object value) {
        if (value instanceof String) {
            return StringUtils.isBlank((String) value);
        } else {
            return value == null;
        }
    }

    public class FieldGroupItemWrapper extends ItemWrapper {

        private static final long serialVersionUID = -7877886198903628220L;

        public FieldGroupItemWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
            super(datasource, propertyPaths);
        }

        public Datasource getDatasource() {
            return (Datasource) item;
        }

        @Override
        protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
            return new PropertyWrapper(item, propertyPath) {
                @Override
                public boolean isReadOnly() {
                    FieldConfig field = fields.get(propertyPath.toString());
                    return !isEditable(field);
                }

                @Override
                public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
                    if (newValue instanceof String) {
                        newValue = ((String) newValue).trim();
                    }
                    super.setValue(newValue);
                }

                @Override
                public String getFormattedValue() {
                    Object value = getValue();
                    if (value == null) {
                        return "";
                    }

                    FieldConfig field = fields.get(propertyPath.toString());
                    if (field.getFormatter() != null) {
                        return field.getFormatter().format(value);
                    }
                    return super.getFormattedValue();
                }
            };
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
}