/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.SetValueEntity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.validators.DateValidator;
import com.haulmont.cuba.gui.components.validators.DoubleValidator;
import com.haulmont.cuba.gui.components.validators.IntegerValidator;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.gui.data.impl.RuntimePropsDatasourceImpl;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */

public class RuntimePropertiesFrame extends AbstractWindow {

    private RuntimePropsDatasource rds;
    private CollectionDatasource categoriesDs;

    public static final String NAME = "runtimePropertiesFrame";
    private static final String DEFAULT_FIELD_WIDTH = "100%";
    private String rows;
    private String cols;
    private String fieldWidth;
    private BoxLayout contentPane;
    private FieldGroup categoryFieldGroup;

    public RuntimePropertiesFrame(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        String dsId = (String) params.get("runtimeDs");
        String categoriesDsId = (String) params.get("categoriesDs");
        rows = (String) params.get("rows");
        cols = (String) params.get("cols");
        fieldWidth = (String) params.get("fieldWidth");
        if (StringUtils.isEmpty(fieldWidth))
            fieldWidth = DEFAULT_FIELD_WIDTH;
        rds = getDsContext().get(dsId);
        categoriesDs = getDsContext().<CollectionDatasource>get(categoriesDsId);

        contentPane = getComponent("contentPane");
        initCategoryField();
        loadComponent(rds);
    }

    private void initCategoryField() {
        categoryFieldGroup = AppConfig.getFactory().createComponent(FieldGroup.NAME);
        categoryFieldGroup.setId("categoryFieldGroup");
        categoryFieldGroup.setFrame(this.<IFrame>getFrame());

        contentPane.add(categoryFieldGroup);
        registerComponent(categoryFieldGroup);

        FieldGroup.Field field = new FieldGroup.Field("category");

        field.setCustom(true);
        categoryFieldGroup.addField(field);
        categoryFieldGroup.setDatasource(rds.getMainDs());
        categoryFieldGroup.addCustomField("category", new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, Object propertyId) {
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

    private void loadComponent(Datasource ds) {

        ds.addListener(new DsListenerAdapter() {
            @Override
            public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
                if (!Datasource.State.VALID.equals(state)) {
                    return;
                }
                Component runtime = getComponent("runtime");
                final FieldGroup newRuntime = AppConfig.getFactory().createComponent(FieldGroup.NAME);
                newRuntime.setBorderVisible(false);
                newRuntime.setId("runtime");
                if (runtime != null)
                    contentPane.remove(runtime);
                newRuntime.setFrame(getFrame());
                contentPane.add(newRuntime);
                registerComponent(newRuntime);

                final java.util.List<FieldGroup.Field> fields = newRuntime.getFields();
                for (FieldGroup.Field field : fields)
                    newRuntime.removeField(field);

                int rowsPerColumn;
                int propertiesCount = rds.getMetaClass().getProperties().size();
                if (StringUtils.isNotBlank(cols)) {
                    int propertiesSize = propertiesCount;
                    if (propertiesSize % Integer.valueOf(cols) == 0)
                        rowsPerColumn = propertiesSize / Integer.valueOf(cols);
                    else
                        rowsPerColumn = propertiesSize / Integer.valueOf(cols) + 1;
                } else if (StringUtils.isNotBlank(rows)) {
                    rowsPerColumn = Integer.valueOf(rows);
                } else {
                    rowsPerColumn = propertiesCount;
                }

                int columnNo = 0;
                int fieldsCount = 0;
                final java.util.List<FieldGroup.Field> rootFields = loadFields(newRuntime, ds);
                for (final FieldGroup.Field field : rootFields) {
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

                for (final FieldGroup.Field field : newRuntime.getFields()) {
                    loadValidators(newRuntime, field);
                    //loadRequired(component, field);
                    //loadEditable(component, field);
                    //loadEnabled(component, field);
                }
            }
        });
    }

    protected void addCustomFields(FieldGroup component, java.util.List<FieldGroup.Field> fields, Datasource ds) {
        MetaClass meta = ds.getMetaClass();
        Collection<MetaProperty> metaProperties = meta.getProperties();
        for (MetaProperty property : metaProperties) {
            Range range = property.getRange();
            if (!range.isDatatype() && range.asClass().getJavaClass().equals(SetValueEntity.class)) {
                for (FieldGroup.Field field : fields) {
                    if (field.getId().equals(property.getName())) {
                        field.setCustom(true);
                        component.addCustomField(property.getName(), new FieldGroup.CustomFieldGenerator() {

                            @Override
                            public Component generateField(Datasource datasource, Object propertyId) {
                                LookupField field = AppConfig.getFactory().createComponent(LookupField.NAME);
                                field.setFrame(RuntimePropertiesFrame.this);
                                field.setDatasource(rds, (String) propertyId);
                                field.setOptionsDatasource(getDsContext().<CollectionDatasource>get((String) propertyId));
//                                field.setHeight("-1px");
                                field.setWidth("100%");
                                return field;
                            }
                        });
                    }
                }
            }
        }
    }

    protected java.util.List<FieldGroup.Field> loadFields(FieldGroup component, Datasource ds) {
        MetaClass meta = ds.getMetaClass();
        Collection<MetaProperty> metaProperties = meta.getProperties();

        java.util.List<FieldGroup.Field> fields = new ArrayList<FieldGroup.Field>();
        for (MetaProperty property : metaProperties) {
            FieldGroup.Field field = new FieldGroup.Field(property.getName());
            field.setCaption(property.getName());
            field.setWidth(fieldWidth);
            fields.add(field);
            Range range = property.getRange();
            if (!range.isDatatype() && range.asClass().getJavaClass().equals(SetValueEntity.class))
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
                        MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(),
                                "validation.invalidNumber"));
            } else if (dt.equals(Datatypes.get(DoubleDatatype.NAME)) || dt.equals(Datatypes.get(BigDecimalDatatype.NAME))) {
                validator = new DoubleValidator(
                        MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(),
                                "validation.invalidNumber"));
            } else if (dt.equals(Datatypes.get(DateDatatype.NAME))) {
                validator = new DateValidator(MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(),
                        "validation.invalidDate"));
            }
        }
        return validator;
    }

    protected void loadValidators(FieldGroup newRuntime, FieldGroup.Field field) {
        MetaPropertyPath metaPropertyPath = rds.getMetaClass().getPropertyPath(field.getId());
        if (metaPropertyPath != null) {
            MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
            Field.Validator validator = null;
            validator = getValidator(metaProperty);

            if (validator != null) {
                newRuntime.addValidator(field, validator);
            }
        }
    }

    public void setCategoryFieldVisible(boolean visible) {
        categoryFieldGroup.setVisible(visible);
    }
}
