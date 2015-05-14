/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.CategoryAttributeValue;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.SetValueEntity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.validators.DateValidator;
import com.haulmont.cuba.gui.components.validators.DoubleValidator;
import com.haulmont.cuba.gui.components.validators.IntegerValidator;
import com.haulmont.cuba.gui.components.validators.LongValidator;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.haulmont.cuba.gui.components.PickerField.LookupAction;

/**
 * Universal frame for editing dynamic attributes
 * of any {@link com.haulmont.cuba.core.entity.Categorized} implementations.
 *
 * @author devyatkin
 * @version $Id$
 */
public class RuntimePropertiesFrame extends AbstractWindow {

    public static final String NAME = "runtimeProperties";
    public static final String DEFAULT_FIELD_WIDTH = "100%";

    protected RuntimePropsDatasource rds;
    protected CollectionDatasource categoriesDs;

    protected boolean requiredControlEnabled = true;

    @Inject
    protected BoxLayout categoryFieldBox;

    @Inject
    protected LookupField categoryField;

    @Inject
    protected ComponentsFactory componentsFactory;

    @WindowParam
    protected String rows;

    @WindowParam
    protected String cols;

    @WindowParam
    protected String fieldWidth;

    @WindowParam
    protected Boolean borderVisible;

    @Override
    public void init(Map<String, Object> params) {
        initDatasources(params);

        if (StringUtils.isEmpty(fieldWidth)) {
            fieldWidth = DEFAULT_FIELD_WIDTH;
        }

        initCategoryField();
        loadComponent(rds);
    }

    protected void initDatasources(Map<String, Object> params) {
        String dsId = (String) params.get("runtimeDs");
        if (dsId == null)
            throw new DevelopmentException("runtimeProperties initialization error: runtimeDs is not provided");
        rds = getDsContext().get(dsId);
        if (rds == null)
            throw new DevelopmentException("runtimeProperties initialization error: runtimeDs '" + dsId + "' does not exists");

        String categoriesDsId = (String) params.get("categoriesDs");
        if (categoriesDsId == null)
            throw new DevelopmentException("runtimeProperties initialization error: categoriesDs is not provided");
        categoriesDs = getDsContext().get(categoriesDsId);
        if (categoriesDs == null)
            throw new DevelopmentException("runtimeProperties initialization error: categoriesDs '" + categoriesDsId + "' does not exists");
    }

    protected void initCategoryField() {
        categoryField.setDatasource(rds.getMainDs(), "category");
        categoryField.setOptionsDatasource(categoriesDs);
    }

    protected void loadComponent(Datasource ds) {
        ds.addListener(new DsListenerAdapter() {
            @Override
            public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
                if (!Datasource.State.VALID.equals(state)) {
                    return;
                }

                Component runtime = getComponent("runtime");
                if (runtime != null) {
                    remove(runtime);
                }

                final FieldGroup newRuntime = componentsFactory.createComponent(FieldGroup.NAME);
                newRuntime.setBorderVisible(Boolean.TRUE.equals(borderVisible));
                newRuntime.setWidth("100%");
                newRuntime.setId("runtime");

                newRuntime.setFrame(getFrame());
                add(newRuntime);

                final List<FieldGroup.FieldConfig> fields = newRuntime.getFields();
                for (FieldGroup.FieldConfig field : fields) {
                    newRuntime.removeField(field);
                }

                int rowsPerColumn;
                int propertiesCount = rds.getPropertiesFilteredByCategory().size();
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
                final java.util.List<FieldGroup.FieldConfig> rootFields = loadFields();
                for (final FieldGroup.FieldConfig field : rootFields) {
                    fieldsCount++;
                    newRuntime.addField(field, columnNo);
                    if (fieldsCount % rowsPerColumn == 0) {
                        columnNo++;
                        newRuntime.setColumns(columnNo + 1);
                    }
                }
                if (!rootFields.isEmpty()) {
                    newRuntime.setDatasource(ds);
                }

                addCustomFields(newRuntime, rootFields, ds);

                for (FieldGroup.FieldConfig fieldConfig : newRuntime.getFields()) {
                    loadValidators(newRuntime, fieldConfig);
                    loadRequired(newRuntime, fieldConfig);
                }
            }
        });
    }

    protected void addCustomFields(FieldGroup component, java.util.List<FieldGroup.FieldConfig> fields, final Datasource ds) {
        Collection<MetaProperty> metaProperties = rds.getPropertiesFilteredByCategory();
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
                                    LookupField field = componentsFactory.createComponent(LookupField.NAME);
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
                            Boolean lookup = ((DynamicAttributesEntity) datasource.getItem()).getCategoryValue(property.getName()).getCategoryAttribute().getLookup();
                            if (lookup != null && lookup) {
                                pickerField = AppConfig.getFactory().createComponent(LookupPickerField.NAME);

                                CollectionDatasource optionsDs = new DsBuilder(datasource.getDsContext())
                                        .setMetaClass(property.getRange().asClass())
                                        .setViewName(View.MINIMAL)
                                        .buildCollectionDatasource();
                                optionsDs.refresh();
                                Action action = pickerField.getAction(LookupAction.NAME);
                                if (action != null)
                                    pickerField.removeAction(action);

                                ((LookupPickerField) pickerField).setOptionsDatasource(optionsDs);
                            } else {
                                pickerField = componentsFactory.createComponent(PickerField.NAME);
                                pickerField.addLookupAction();
                            }
                            pickerField.setMetaClass(ds.getMetaClass());
                            pickerField.setFrame(RuntimePropertiesFrame.this);
                            pickerField.setDatasource(ds, propertyId);
                            LookupAction lookupAction = (LookupAction) pickerField.getAction(LookupAction.NAME);
                            if (lookupAction != null) {
                                DynamicAttributesEntity dynamicAttributesEntity = (DynamicAttributesEntity) ds.getItem();
                                CategoryAttributeValue categoryAttributeValue = dynamicAttributesEntity.getCategoryValue(property.getName());
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

    protected java.util.List<FieldGroup.FieldConfig> loadFields() {
        Collection<MetaProperty> metaProperties = rds.getPropertiesFilteredByCategory();
        java.util.List<FieldGroup.FieldConfig> fields = new ArrayList<>();
        for (MetaProperty property : metaProperties) {
            FieldGroup.FieldConfig field = new FieldGroup.FieldConfig(property.getName());
            CategoryAttribute attribute = AppBeans.get(DynamicAttributes.class)
                    .getAttributeForMetaClass(rds.getMainDs().getMetaClass(), property.getName().substring(1));
            field.setCaption(attribute != null ? attribute.getName() : property.getName());
            field.setWidth(fieldWidth);
            fields.add(field);
            Range range = property.getRange();
            if (!range.isDatatype()) {
                field.setCustom(true);
            }
        }
        return fields;
    }

    protected Field.Validator getValidator(MetaProperty property) {
        Field.Validator validator = null;
        if (property.getRange().isDatatype()) {
            Datatype<Object> dt = property.getRange().asDatatype();

            if (dt.equals(Datatypes.get(IntegerDatatype.NAME))) {
                validator = new IntegerValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (dt.equals(Datatypes.get(LongDatatype.NAME))) {
                validator = new LongValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (dt.equals(Datatypes.get(DoubleDatatype.NAME)) || dt.equals(Datatypes.get(BigDecimalDatatype.NAME))) {
                validator = new DoubleValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (dt.equals(Datatypes.get(DateDatatype.NAME))) {
                validator = new DateValidator(messages.getMainMessage("validation.invalidDate"));
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
        DynamicAttributesEntity dynamicAttributesEntity = (DynamicAttributesEntity) rds.getItem();
        CategoryAttributeValue categoryAttributeValue = dynamicAttributesEntity.getCategoryValue(field.getId());
        String requiredMessage = messages.formatMessage(
                AppConfig.getMessagesPack(),
                "validation.required.defaultMsg",
                categoryAttributeValue.getCategoryAttribute().getName()
        );
        fieldGroup.setRequired(field, categoryAttributeValue.getCategoryAttribute().getRequired() && requiredControlEnabled, requiredMessage);
    }

    public void setCategoryFieldVisible(boolean visible) {
        categoryFieldBox.setVisible(visible);
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
        categoryField.setEditable(editable);
        FieldGroup newRuntime = getComponent("runtime");
        if (newRuntime != null) {
            newRuntime.setEditable(editable);
        }
    }
}