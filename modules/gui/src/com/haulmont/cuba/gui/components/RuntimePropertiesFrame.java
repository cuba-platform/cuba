/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.CategoryAttributeValue;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.SetValueEntity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.validators.DateValidator;
import com.haulmont.cuba.gui.components.validators.DoubleValidator;
import com.haulmont.cuba.gui.components.validators.IntegerValidator;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Universal frame for editing Runtime properties
 * of any {@link com.haulmont.cuba.core.entity.CategorizedEntity} subclass.
 *
 * @author devyatkin
 * @version $Id$
 */
public class RuntimePropertiesFrame extends AbstractWindow {

    protected RuntimePropsDatasource rds;
    private CollectionDatasource categoriesDs;

    public static final String NAME = "runtimeProperties";

    private static final String DEFAULT_FIELD_WIDTH = "100%";
    protected String rows;
    protected String cols;
    private String fieldWidth;
    protected Boolean borderVisible;
    protected BoxLayout contentPane;
    private FieldGroup categoryFieldGroup;
    private boolean requiredControlEnabled = true;

    @Override
    public void init(Map<String, Object> params) {
        String dsId = (String) params.get("runtimeDs");
        String categoriesDsId = (String) params.get("categoriesDs");
        rows = (String) params.get("rows");
        cols = (String) params.get("cols");
        fieldWidth = (String) params.get("fieldWidth");
        borderVisible = Boolean.valueOf((String) params.get("borderVisible"));

        if (StringUtils.isEmpty(fieldWidth)) {
            fieldWidth = DEFAULT_FIELD_WIDTH;
        }
        rds = getDsContext().get(dsId);
        categoriesDs = getDsContext().get(categoriesDsId);

        contentPane = getComponent("contentPane");
        initCategoryField();
        loadComponent(rds);
    }

    private void initCategoryField() {
        categoryFieldGroup = AppConfig.getFactory().createComponent(FieldGroup.NAME);
        categoryFieldGroup.setId("categoryFieldGroup");
        categoryFieldGroup.setFrame(this.<IFrame>getFrame());
        categoryFieldGroup.setBorderVisible(borderVisible);

        contentPane.add(categoryFieldGroup);
        registerComponent(categoryFieldGroup);

        FieldGroup.FieldConfig field = new FieldGroup.FieldConfig("category");

        field.setCustom(true);
        categoryFieldGroup.addField(field);
        categoryFieldGroup.setDatasource(rds.getMainDs());
        categoryFieldGroup.addCustomField("category", new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                LookupField field = AppConfig.getFactory().createComponent(LookupField.NAME);
                field.setDatasource(rds.getMainDs(), "category");
                field.setOptionsDatasource(categoriesDs);
//                field.setHeight("-1px");
                field.setWidth(fieldWidth);
                field.setCaptionProperty("name");
                field.setCaption(getMessage("runtimeProperties.category"));
                return field;
            }
        });
    }

    protected void loadComponent(Datasource ds) {

        ds.addListener(new DsListenerAdapter() {
            @Override
            public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
                if (!Datasource.State.VALID.equals(state)) {
                    return;
                }
                Component runtime = getComponent("runtime");
                final FieldGroup newRuntime = AppConfig.getFactory().createComponent(FieldGroup.NAME);
                newRuntime.setBorderVisible(borderVisible);
                newRuntime.setId("runtime");
                if (runtime != null) {
                    contentPane.remove(runtime);
                    Component cfg = contentPane.getComponent("categoryFieldGroup");
                    if (cfg != null) {
                        contentPane.remove(cfg);
                        contentPane.add(cfg);
                    }
                }
                newRuntime.setFrame(getFrame());
                contentPane.add(newRuntime);
                registerComponent(newRuntime);

                final java.util.List<FieldGroup.FieldConfig> fields = newRuntime.getFields();
                for (FieldGroup.FieldConfig field : fields)
                    newRuntime.removeField(field);

                int rowsPerColumn;
                int propertiesCount = rds.getMetaClass().getProperties().size();
                if (StringUtils.isNotBlank(cols)) {
                    int propertiesSize = propertiesCount;
                    if (propertiesSize % Integer.valueOf(cols) == 0)
                        rowsPerColumn = propertiesSize / Integer.parseInt(cols);
                    else
                        rowsPerColumn = propertiesSize / Integer.parseInt(cols) + 1;
                } else if (StringUtils.isNotBlank(rows)) {
                    rowsPerColumn = Integer.parseInt(rows);
                } else {
                    rowsPerColumn = propertiesCount;
                }

                int columnNo = 0;
                int fieldsCount = 0;
                final java.util.List<FieldGroup.FieldConfig> rootFields = loadFields(newRuntime, ds);
                for (final FieldGroup.FieldConfig field : rootFields) {
                    fieldsCount++;
                    newRuntime.addField(field, columnNo);
                    if (fieldsCount % rowsPerColumn == 0) {
                        columnNo++;
                        newRuntime.setColumns(columnNo + 1);
                    }
                }
                if (!rootFields.isEmpty())
                    newRuntime.setDatasource(ds);

                addCustomFields(newRuntime, rootFields, ds);

                for (final FieldGroup.FieldConfig field : newRuntime.getFields()) {
                    loadValidators(newRuntime, field);
                    loadRequired(newRuntime, field);
                    //loadEditable(component, field);
                    //loadEnabled(component, field);
                }
            }
        });
    }

    protected void addCustomFields(FieldGroup component, java.util.List<FieldGroup.FieldConfig> fields, final Datasource ds) {
        MetaClass meta = ds.getMetaClass();
        Collection<MetaProperty> metaProperties = meta.getProperties();
        for (final MetaProperty property : metaProperties) {
            Range range = property.getRange();
            if (!range.isDatatype()) {
                if (range.asClass().getJavaClass().equals(SetValueEntity.class)) {
                    for (FieldGroup.FieldConfig field : fields) {
                        if (field.getId().equals(property.getName())) {
                            field.setCustom(true);
                            component.addCustomField(property.getName(), new FieldGroup.CustomFieldGenerator() {
                                @Override
                                public Component generateField(Datasource datasource, String propertyId) {
                                    LookupField field = AppConfig.getFactory().createComponent(LookupField.NAME);
                                    field.setFrame(RuntimePropertiesFrame.this);
                                    CollectionDatasource fieldDs = getDsContext().get(propertyId);
                                    if (fieldDs == null) {
                                        DsContext parentDsContext = getDsContext().getParent();
                                        if (parentDsContext != null) {
                                            for (Datasource ds : parentDsContext.getAll()) {
                                                if (ds.getId().equals(propertyId)) {
                                                    fieldDs = (CollectionDatasource) ds;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    field.setOptionsDatasource(fieldDs);
                                    field.setDatasource(rds, propertyId);
                                    //field.setHeight("-1px");
                                    field.setWidth(fieldWidth);
                                    return field;
                                }
                            });
                        }
                    }
                } else {
                    component.addCustomField(property.getName(), new FieldGroup.CustomFieldGenerator() {
                        @Override
                        public Component generateField(Datasource datasource, String propertyId) {
                            //todo move field generation to generator previous to this block (upper)
                            final PickerField pickerField;
                            Boolean lookup = ((RuntimePropertiesEntity) datasource.getItem()).getCategoryValue(property.getName()).getCategoryAttribute().getLookup();
                            if (lookup != null && lookup){
                                pickerField = AppConfig.getFactory().createComponent(LookupPickerField.NAME);

                                CollectionDatasource optionsDs = new DsBuilder(datasource.getDsContext())
                                        .setMetaClass(property.getRange().asClass())
                                        .setViewName(View.MINIMAL)
                                        .buildCollectionDatasource();
                                optionsDs.refresh();
                                Action action = pickerField.getAction(PickerField.LookupAction.NAME);
                                if (action != null)
                                    pickerField.removeAction(action);

                                        ((LookupPickerField) pickerField).setOptionsDatasource(optionsDs);
                            } else {
                                pickerField = AppConfig.getFactory().createComponent(PickerField.NAME);
                                pickerField.addLookupAction();
                            }
                            pickerField.setMetaClass(ds.getMetaClass());
                            pickerField.setFrame(RuntimePropertiesFrame.this);
                            pickerField.setDatasource(ds, propertyId);
                            PickerField.LookupAction lookupAction = (PickerField.LookupAction) pickerField.getAction(PickerField.LookupAction.NAME);
                            if (lookupAction != null) {
                                RuntimePropertiesEntity runtimePropertiesEntity = (RuntimePropertiesEntity) ds.getItem();
                                CategoryAttributeValue categoryAttributeValue = runtimePropertiesEntity.getCategoryValue(property.getName());
                                if (categoryAttributeValue != null) {
                                    String screen = categoryAttributeValue.getCategoryAttribute().getScreen();
                                    if (StringUtils.isBlank(screen)) {
                                        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
                                        screen = windowConfig.getBrowseScreenId(pickerField.getMetaClass());
                                    }
                                    lookupAction.setLookupScreen(screen);
                                }
                            }
                            pickerField.addOpenAction();
                            pickerField.setWidth(fieldWidth);
                            return pickerField;
                        }
                    });
                }
            }
        }
    }

    protected java.util.List<FieldGroup.FieldConfig> loadFields(FieldGroup component, Datasource ds) {
        MetaClass meta = ds.getMetaClass();
        Collection<MetaProperty> metaProperties = meta.getProperties();
        java.util.List<FieldGroup.FieldConfig> fields = new ArrayList<>();
        for (MetaProperty property : metaProperties) {
            FieldGroup.FieldConfig field = new FieldGroup.FieldConfig(property.getName());
            field.setCaption(property.getName());
            field.setWidth(fieldWidth);
            fields.add(field);
            Range range = property.getRange();
//            if (!range.isDatatype() && range.asClass().getJavaClass().equals(SetValueEntity.class))
            if (!range.isDatatype())
                field.setCustom(true);
        }
        return fields;
    }

    protected Field.Validator getValidator(MetaProperty property) {
        Field.Validator validator = null;
        if (property.getRange().isDatatype()) {
            Datatype<Object> dt = property.getRange().asDatatype();

            if (dt.equals(Datatypes.get(IntegerDatatype.NAME)) || dt.equals(Datatypes.get(LongDatatype.NAME))) {
                validator = new IntegerValidator(
                        messages.getMessage(AppConfig.getMessagesPack(),
                                "validation.invalidNumber"));
            } else if (dt.equals(Datatypes.get(DoubleDatatype.NAME)) || dt.equals(Datatypes.get(BigDecimalDatatype.NAME))) {
                validator = new DoubleValidator(
                        messages.getMessage(AppConfig.getMessagesPack(),
                                "validation.invalidNumber"));
            } else if (dt.equals(Datatypes.get(DateDatatype.NAME))) {
                validator = new DateValidator(messages.getMessage(AppConfig.getMessagesPack(),
                        "validation.invalidDate"));
            }
        }
        return validator;
    }

    protected void loadValidators(FieldGroup newRuntime, FieldGroup.FieldConfig field) {
        MetaPropertyPath metaPropertyPath = rds.getMetaClass().getPropertyPath(field.getId());
        if (metaPropertyPath != null) {
            MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
            Field.Validator validator = getValidator(metaProperty);

            if (validator != null) {
                newRuntime.addValidator(field, validator);
            }
        }
    }

    protected void loadRequired(FieldGroup fieldGroup, FieldGroup.FieldConfig field) {
        RuntimePropertiesEntity runtimePropertiesEntity = (RuntimePropertiesEntity) rds.getItem();
        CategoryAttributeValue categoryAttributeValue = runtimePropertiesEntity.getCategoryValue(field.getId());
        String requiredMessage = messages.formatMessage(
                AppConfig.getMessagesPack(),
                "validation.required.defaultMsg",
                field.getId()
        );
        fieldGroup.setRequired(field, categoryAttributeValue.getCategoryAttribute().getRequired() && requiredControlEnabled, requiredMessage);
    }

    public void setCategoryFieldVisible(boolean visible) {
        categoryFieldGroup.setVisible(visible);
    }

    public boolean isRequiredControlEnabled() {
        return requiredControlEnabled;
    }

    public void setRequiredControlEnabled(boolean requiredControlEnabled) {
        this.requiredControlEnabled = requiredControlEnabled;
        FieldGroup newRuntime = getComponent("runtime");
        if (newRuntime != null) {
            for (final FieldGroup.FieldConfig field : newRuntime.getFields()) {
                loadRequired(newRuntime, field);
            }
        }
    }

    public void setCategoryFieldEditable(boolean editable) {
        categoryFieldGroup.setEditable(editable);
        FieldGroup newRuntime = getComponent("runtime");
        if (newRuntime != null) {
            newRuntime.setEditable(editable);
        }
    }
}