/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.appproperties;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.BooleanDatatype;
import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.config.AppPropertyEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Controller of the {@code appproperties-edit.xml} screen
 */
public class AppPropertiesEdit extends AbstractWindow {

    @WindowParam
    private AppPropertyEntity item;

    @Inject
    private Datasource<AppPropertyEntity> appPropertyDs;

    @Inject
    private ConfigStorageService configStorageService;

    @Inject
    private Label cannotEditValueLabel;

    @Inject
    private Metadata metadata;

    @Inject
    private FieldGroup fieldGroup;

    @Inject
    private ComponentsFactory componentsFactory;

    @Override
    public void init(Map<String, Object> params) {
        cannotEditValueLabel.setVisible(item.getOverridden());

        fieldGroup.addCustomField("currentValue", (datasource, propertyId) -> {
            if (item.getOverridden()) {
                TextField textField = componentsFactory.createComponent(TextField.class);
                textField.setValue(item.getCurrentValue());
                textField.setEditable(false);
                return textField;
            }
            if (item.getEnumValues() != null) {
                return createLookupField(Arrays.asList(item.getEnumValues().split(",")), item.getCurrentValue());
            } else {
                Datatype datatype = Datatypes.get(item.getDataTypeName());
                if (datatype instanceof BooleanDatatype) {
                    return createLookupField(Arrays.asList(Boolean.TRUE.toString(), Boolean.FALSE.toString()), item.getCurrentValue());
                } else {
                    TextField textField = componentsFactory.createComponent(TextField.class);
                    textField.setValue(item.getCurrentValue());
                    textField.setDatatype(datatype);
                    textField.addValueChangeListener(e -> {
                        appPropertyDs.getItem().setCurrentValue(e.getValue() == null ? null : e.getValue().toString());
                    });
                    return textField;
                }
            }
        });

        appPropertyDs.setItem(metadata.getTools().copy(item));
    }

    private Component createLookupField(List<String> values, String currentValue) {
        LookupField lookupField = componentsFactory.createComponent(LookupField.class);
        lookupField.setOptionsList(values);
        lookupField.setValue(currentValue);
        lookupField.addValueChangeListener(e -> {
            appPropertyDs.getItem().setCurrentValue((String) e.getValue());
        });
        return lookupField;
    }

    public void ok() {
        AppPropertyEntity appPropertyEntity = appPropertyDs.getItem();
        configStorageService.setDbProperty(appPropertyEntity.getName(), appPropertyEntity.getCurrentValue());
        close(COMMIT_ACTION_ID);
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }
}
