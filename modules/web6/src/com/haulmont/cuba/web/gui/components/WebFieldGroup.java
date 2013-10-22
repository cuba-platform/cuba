/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.web.gui.AbstractFieldFactory;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.CheckBox;
import com.haulmont.cuba.web.toolkit.ui.CustomField;
import com.haulmont.cuba.web.toolkit.ui.FieldGroup;
import com.haulmont.cuba.web.toolkit.ui.FieldGroupLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebFieldGroup extends WebAbstractComponent<FieldGroup> implements com.haulmont.cuba.gui.components.FieldGroup {

    private static final String BORDER_STYLE_NAME = "edit-area";

    protected Map<String, FieldConfig> fields = new LinkedHashMap<>();
    protected Map<FieldConfig, Integer> fieldsColumn = new HashMap<>();
    protected Map<Integer, List<FieldConfig>> columnFields = new HashMap<>();

    protected Set<FieldConfig> readOnlyFields = new HashSet<>();

    protected Map<FieldConfig, List<com.haulmont.cuba.gui.components.Field.Validator>> fieldValidators = new HashMap<>();

    protected Datasource<Entity> datasource;

    protected String caption;
    protected String description;

    protected int cols = 1;

    protected final FieldFactory fieldFactory = new FieldFactory();

    protected Item itemWrapper;

    protected Security security = AppBeans.get(Security.class);

    protected MessageTools messageTools = AppBeans.get(MessageTools.class);
    protected Messages messages = AppBeans.get(Messages.class);

    public WebFieldGroup() {
        component = new FieldGroup(fieldFactory) {
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
        component.setLayout(new FieldGroupLayout());
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
        for (final Map.Entry<String, FieldConfig> entry : fields.entrySet()) {
            if (entry.getKey().equals(id)) {
                return entry.getValue();
            }
        }
        return null;
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
        FieldGroupLayout layout = component.getLayout();
        layout.setCaptionAlignment(WebComponentsHelper.convertFieldGroupCaptionAlignment(captionAlignment));
    }

    @Override
    public void addCustomField(String fieldId, CustomFieldGenerator fieldGenerator) {
        FieldConfig fieldConf = getField(fieldId);
        if (fieldConf == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        addCustomField(fieldConf, fieldGenerator);
    }

    @Override
    public void addCustomField(final FieldConfig fieldConfig, final CustomFieldGenerator fieldGenerator) {
        if (!fieldConfig.isCustom()) {
            throw new IllegalStateException(String.format("Field '%s' must be defined as custom", fieldConfig.getId()));
        }
        component.addCustomField(fieldConfig.getId(), new FieldGroup.CustomFieldGenerator() {
            @Override
            public com.vaadin.ui.Field generateField(Item item, Object propertyId, FieldGroup component) {
                Datasource ds;
                if (fieldConfig.getDatasource() != null) {
                    ds = fieldConfig.getDatasource();
                } else {
                    ds = datasource;
                }

                Component c;
                com.vaadin.ui.Field f;

                String id = (String) propertyId;

                c = fieldGenerator.generateField(ds, id);
                assignTypicalAttributes(c);
                f = getFieldComponent(c);

                MetaPropertyPath propertyPath = ds.getMetaClass().getPropertyPath(id);
                if (propertyPath != null) {
                    if (f.getPropertyDataSource() == null) {
                        if (fieldConfig.getDatasource() != null) {
                            final ItemWrapper dsWrapper = createDatasourceWrapper(ds,
                                    Collections.<MetaPropertyPath>singleton(propertyPath));
                            f.setPropertyDataSource(dsWrapper.getItemProperty(propertyPath));
                        } else {
                            f.setPropertyDataSource(itemWrapper.getItemProperty(propertyPath));
                        }
                    }
                }

                if (f.getCaption() == null) {
                    if (fieldConfig.getCaption() != null) {
                        f.setCaption(fieldConfig.getCaption());
                    } else if (propertyPath != null) {
                        f.setCaption(messageTools.getPropertyCaption(propertyPath.getMetaClass(), id));
                    }
                }

                if (f.getDescription() == null && fieldConfig.getDescription() != null) {
                    f.setDescription(fieldConfig.getDescription());
                }

                // some components (e.g. LookupPickerField) have width from the creation, so I commented out this check
                if (/*f.getWidth() == -1f &&*/ fieldConfig.getWidth() != null) {
                    f.setWidth(fieldConfig.getWidth());
                }

                if (!f.isRequired()) {
                    f.setRequired(fieldConfig.isRequired());
                }

                if ((f.getRequiredError() == null || f.getRequiredError().isEmpty())
                        && fieldConfig.getRequiredError() != null)
                    f.setRequiredError(fieldConfig.getRequiredError());

                applyPermissions(c);

                return f;
            }
        });
    }

    private com.vaadin.ui.Field getFieldComponent(Component c) {
        com.vaadin.ui.Field f;
        com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(c);
        if (composition instanceof com.vaadin.ui.Field)
            f = (com.vaadin.ui.Field) composition;
        else
            f = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(c);
        return f;
    }

    private void assignTypicalAttributes(Component c) {
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
                    final FieldConfig fieldConfig = getField(id);
                    final MetaPropertyPath propertyPath = datasource.getMetaClass().getPropertyPath(fieldConfig.getId());
                    final Element descriptor = fieldConfig.getXmlDescriptor();
                    final String clickAction = (descriptor == null) ? (null) : (descriptor.attributeValue("clickAction"));
                    if (fieldConfig.getDatasource() == null && propertyPath != null
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
                    final FieldConfig fieldConfig = getField(propertyPath.toString());
                    if (fieldConfig.isCustom()) {
                        fieldsMetaProps.remove(propertyPath);
                    }
                }
            }

            component.setItemDataSource(itemWrapper, fieldsMetaProps);
        } else {
            component.setItemDataSource(null, null);
        }

        createFields(datasource);
    }

    private void createFields(Datasource datasource) {
        for (final String id : this.fields.keySet()) {
            final FieldConfig fieldConf = getField(id);
            if (!fieldConf.isCustom()) {
                com.vaadin.ui.Field field;
                Element descriptor = fieldConf.getXmlDescriptor();
                final String clickAction = (descriptor == null) ? (null) : (descriptor.attributeValue("clickAction"));
                Datasource fieldDs;
                if (datasource != null && fieldConf.getDatasource() == null) {
                    if (!StringUtils.isEmpty(clickAction)) {
                        field = createField(datasource, fieldConf);
                        component.addField(fieldConf.getId(), field);
                    } else {
                        field = component.getField(id);
                    }
                    fieldDs = datasource;
                } else if (fieldConf.getDatasource() != null) {
                    if (!StringUtils.isEmpty(clickAction)) {
                        field = createField(fieldConf.getDatasource(), fieldConf);
                        component.addField(fieldConf.getId(), field);
                    } else {
                        MetaPropertyPath propertyPath = fieldConf.getDatasource().getMetaClass().getPropertyPath(fieldConf.getId());
                        final ItemWrapper dsWrapper = createDatasourceWrapper(fieldConf.getDatasource(),
                                Collections.<MetaPropertyPath>singleton(propertyPath));

                        field = fieldFactory.createField(dsWrapper, propertyPath, component);

                        if (field != null && dsWrapper.getItemProperty(propertyPath) != null) {
                            field.setPropertyDataSource(dsWrapper.getItemProperty(propertyPath));
                            component.addField(fieldConf.getId(), field);
                        }
                    }
                    fieldDs = fieldConf.getDatasource();
                } else {
                    throw new IllegalStateException(String.format("Unable to get datasource for field '%s'", id));
                }

                if (field != null) {
                    if (fieldConf.getCaption() != null) {
                        field.setCaption(fieldConf.getCaption());
                    }
                    if (fieldConf.getDescription() != null) {
                        field.setDescription(fieldConf.getDescription());
                    }
                    if (!field.isRequired()) {
                        field.setRequired(fieldConf.isRequired());
                    }
                    if (fieldConf.getRequiredError() != null) {
                        field.setRequiredError(fieldConf.getRequiredError());
                    }
                }

                applyPermissions(fieldConf, fieldDs);
            }
        }
    }

    private void applyPermissions(Component c) {
        if (c instanceof DatasourceComponent) {
            DatasourceComponent dsComponent = (DatasourceComponent) c;
            MetaProperty metaProperty = dsComponent.getMetaProperty();

            if (metaProperty != null) {
                dsComponent.setEditable(security.isEntityAttrModificationPermitted(metaProperty)
                        && dsComponent.isEditable());
            }
        }
    }

    private void applyPermissions(FieldConfig fieldConf, Datasource datasource) {
        MetaPropertyPath propertyPath = datasource.getMetaClass().getPropertyPath(fieldConf.getId());
        if (propertyPath != null) {
            MetaProperty metaProperty = propertyPath.getMetaProperty();

            setEditable(fieldConf, security.isEntityAttrModificationPermitted(metaProperty)
                    && isEditable(fieldConf));
        }
    }

    private int rowsCount() {
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
                if (propertyPath.getMetaProperty().getRange().isDatatype())
                    datatype = propertyPath.getRange().asDatatype();
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
        if (!validators.contains(validator))
            validators.add(validator);
    }

    @Override
    public void addValidator(String fieldId, com.haulmont.cuba.gui.components.Field.Validator validator) {
        FieldConfig field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        addValidator(field, validator);
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        return isRequired(field);
    }

    @Override
    public void setRequired(String fieldId, boolean required, String message) {
        FieldConfig field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
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
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        setEditable(field, editable);
    }

    @Override
    public boolean isEditable(String fieldId) {
        FieldConfig field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
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
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        return isEnabled(field);
    }

    @Override
    public void setEnabled(String fieldId, boolean enabled) {
        FieldConfig field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
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
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        return isVisible(field);
    }

    @Override
    public void setVisible(String fieldId, boolean visible) {
        FieldConfig field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        setVisible(field, visible);
    }

    @Override
    public boolean isBorderVisible() {
        String styleName = getStyleName();
        if (StringUtils.isNotEmpty(styleName))
            return styleName.contains(BORDER_STYLE_NAME);
        return false;
    }

    @Override
    public void setBorderVisible(boolean borderVisible) {
        String styleName = getStyleName();
        if (borderVisible) {
            if (StringUtils.isNotEmpty(styleName)) {
                if (!styleName.contains(BORDER_STYLE_NAME))
                    styleName = styleName + " " + BORDER_STYLE_NAME;
            } else
                styleName = BORDER_STYLE_NAME;
        } else {
            if (StringUtils.isNotEmpty(styleName))
                styleName = styleName.replace(BORDER_STYLE_NAME, "");
        }
        setStyleName(styleName);
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
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        return getFieldValue(field);
    }

    @Override
    public void setFieldValue(String fieldId, Object value) {
        FieldConfig field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        setFieldValue(field, value);
    }

    @Override
    public void requestFocus(String fieldId) {
        FieldConfig field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));
        com.vaadin.ui.Field componentField = component.getField(field.getId());
        if (componentField != null)
            componentField.focus();
    }

    @Override
    public void setFieldCaption(String fieldId, String caption) {
        FieldConfig field = getField(fieldId);
        if (field == null)
            throw new IllegalArgumentException(String.format("Field '%s' doesn't exist", fieldId));

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
        if (!isVisible() || !isEditable() || !isEnabled()) {
            return;
        }

        final Map<Object, Exception> problems = new HashMap<>();

        // todo use cuba fields for validation

        for (FieldConfig field : getFields()) {
            com.vaadin.ui.Field f = component.getField(field.getId());
            if (f != null && f.isVisible() && f.isEnabled() && !f.isReadOnly()) {
                Object value = convertRawValue(field, getFieldValue(field));
                if (isEmpty(value)) {
                    if (isRequired(field))
                        problems.put(field.getId(), new RequiredValueMissingException(f.getRequiredError(), this));
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
                if (iterator.hasNext())
                    msgBuilder.append("<br>");
            }

            FieldsValidationException validationException = new FieldsValidationException(msgBuilder.toString());
            validationException.setProblemFields(problemFields);

            throw validationException;
        }
    }

    protected boolean isEmpty(Object value) {
        if (value instanceof String)
            return StringUtils.isBlank((String) value);
        else
            return value == null;
    }

    protected class FieldFactory extends AbstractFieldFactory {
        @Override
        protected Datasource getDatasource() {
            return datasource;
        }

        @Override
        protected void initCommon(com.vaadin.ui.Field field, com.haulmont.cuba.gui.components.Field cubaField, MetaPropertyPath propertyPath) {
            final FieldConfig fieldConf = getField(propertyPath.toString());
            if ("timeField".equals(fieldType(propertyPath)) || (cubaField instanceof WebTimeField)) {
                String s = fieldConf.getXmlDescriptor().attributeValue("showSeconds");
                if (Boolean.valueOf(s)) {
                    ((TimeField) cubaField).setShowSeconds(true);
                }
            } else if (cubaField instanceof WebMaskedField) {
                initMaskedField((WebMaskedField) cubaField, propertyPath.getMetaProperty(), fieldConf.getXmlDescriptor());
            } else if (field instanceof TextField) {
                ((TextField) field).setNullRepresentation("");
                field.setInvalidCommitted(true);
                if (fieldConf != null) {
                    initTextField((TextField) field, propertyPath.getMetaProperty(), fieldConf.getXmlDescriptor());
                }
            } else if (cubaField instanceof WebDateField) {
                if (getFormatter(propertyPath) != null) {
                    String format = getFormat(propertyPath);
                    if (format != null) {
                        ((WebDateField) cubaField).setDateFormat(format);
                    }
                }
                if (fieldConf != null) {
                    initDateField(field, propertyPath.getMetaProperty(), fieldConf.getXmlDescriptor());
                }
            } else if (field instanceof CheckBox) {
                ((CheckBox) field).setLayoutCaption(true);
            }

            if (fieldConf != null && fieldConf.getWidth() != null) {
                field.setWidth(fieldConf.getWidth());
            }

            if (cubaField != null)
                cubaField.setFrame(getFrame());
        }

        @Override
        protected CollectionDatasource getOptionsDatasource(MetaClass metaClass, MetaPropertyPath propertyPath) {
            final FieldConfig field = fields.get(propertyPath.toString());

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

        @Override
        protected void initValidators(com.vaadin.ui.Field field, com.haulmont.cuba.gui.components.Field cubaField, MetaPropertyPath propertyPath, boolean validationVisible) {
            //do nothing
        }

        @Override
        protected Collection<com.haulmont.cuba.gui.components.Field.Validator> getValidators(MetaPropertyPath propertyPath) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected boolean required(MetaPropertyPath propertyPath) {
            return false;
        }

        @Override
        protected String requiredMessage(MetaPropertyPath propertyPath) {
            return null;
        }

        @Override
        protected Formatter getFormatter(MetaPropertyPath propertyPath) {
            FieldConfig field = fields.get(propertyPath.toString());
            if (field != null) {
                return field.getFormatter();
            } else {
                return null;
            }
        }

        @Override
        protected String getFormat(MetaPropertyPath propertyPath) {
            FieldConfig field = fields.get(propertyPath.toString());
            if (field != null) {
                Element formatterElement = field.getXmlDescriptor().element("formatter");
                return formatterElement.attributeValue("format");
            }
            return null;
        }

        @Override
        protected String fieldType(MetaPropertyPath propertyPath) {
            FieldConfig field = fields.get(propertyPath.toString());
            if (field != null) {
                if (field.getXmlDescriptor() != null) {
                    String fieldType = field.getXmlDescriptor().attributeValue("field");
                    if (!StringUtils.isEmpty(fieldType)) {
                        return fieldType;
                    }
                }
            }
            return null;
        }

        @Override
        protected Element getXmlDescriptor(MetaPropertyPath propertyPath) {
            FieldConfig field = fields.get(propertyPath.toString());
            return field != null ? field.getXmlDescriptor() : null;
        }

        @Override
        protected void setCaption(com.vaadin.ui.Field field, MetaPropertyPath propertyPath) {
            // if caption not already loaded from attributes then load default caption
            FieldConfig fieldConf = WebFieldGroup.this.fields.get(propertyPath.toString());
            if ((fieldConf == null) || (StringUtils.isEmpty(fieldConf.getCaption())))
                super.setCaption(field, propertyPath);
        }
    }

    protected com.vaadin.ui.Field createField(final Datasource datasource, final FieldConfig fieldConf) {
        MetaPropertyPath propertyPath = datasource.getMetaClass().getPropertyPath(fieldConf.getId());
        final ItemWrapper dsWrapper = createDatasourceWrapper(
                datasource,
                Collections.<MetaPropertyPath>singleton(propertyPath)
        );

        final LinkField field = new LinkField(datasource, fieldConf);
        field.setCaption(messageTools.getPropertyCaption(propertyPath.getMetaProperty()));
        field.setPropertyDataSource(dsWrapper.getItemProperty(propertyPath));

        return field;
    }

    protected class LinkField extends CustomField {
        private static final long serialVersionUID = 5555318337278242796L;

        private Button component;

        private LinkField(final Datasource datasource, final FieldConfig fieldConf) {
            component = new Button();
            component.setStyleName("link");
            component.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    final Instance entity = datasource.getItem();
                    final Entity value = entity.getValueEx(fieldConf.getId());
                    String clickAction = fieldConf.getXmlDescriptor().attributeValue("clickAction");
                    if (!StringUtils.isEmpty(clickAction)) {
                        if (clickAction.startsWith("open:")) {
                            final com.haulmont.cuba.gui.components.IFrame frame = WebFieldGroup.this.getFrame();
                            String screenName = clickAction.substring("open:".length()).trim();
                            final com.haulmont.cuba.gui.components.Window window =
                                    frame.openEditor(
                                            screenName,
                                            value,
                                            WindowManager.OpenType.THIS_TAB);

                            window.addListener(new com.haulmont.cuba.gui.components.Window.CloseListener() {
                                @Override
                                public void windowClosed(String actionId) {
                                    if (com.haulmont.cuba.gui.components.Window.COMMIT_ACTION_ID.equals(actionId)
                                            && window instanceof com.haulmont.cuba.gui.components.Window.Editor) {
                                        Object item = ((com.haulmont.cuba.gui.components.Window.Editor) window).getItem();
                                        if (item instanceof Entity) {
                                            entity.setValueEx(fieldConf.getId(), item);
                                        }
                                    }
                                }
                            });
                        } else if (clickAction.startsWith("invoke:")) {
                            final com.haulmont.cuba.gui.components.IFrame frame = WebFieldGroup.this.getFrame();
                            String methodName = clickAction.substring("invoke:".length()).trim();
                            try {
                                IFrame controllerFrame = WebComponentsHelper.getControllerFrame(frame);
                                Method method = controllerFrame.getClass().getMethod(methodName, Object.class);
                                method.invoke(controllerFrame, value);
                            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                                throw new RuntimeException("Unable to invoke clickAction", e);
                            }

                        } else {
                            throw new UnsupportedOperationException(String.format("Unsupported clickAction format: %s", clickAction));
                        }
                    }
                }
            });
            setStyleName("linkfield");
            setCompositionRoot(component);
        }

        @Override
        public Class<?> getType() {
            return String.class;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            super.valueChange(event);
            component.setCaption(event.getProperty().getValue() == null
                    ? "" : event.getProperty().toString());
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
                public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                    if (newValue instanceof String)
                        newValue = ((String) newValue).trim();
                    super.setValue(newValue);
                }

                @Override
                public String toString() {
                    Object value = getValue();
                    if (value == null) return null;
                    FieldConfig field = fields.get(propertyPath.toString());
                    if (field.getFormatter() != null) {
                        if (value instanceof Instance) {
                            value = ((Instance) value).getInstanceName();
                        }
                        return field.getFormatter().format(value);
                    }
                    return super.toString();
                }
            };
        }
    }
}