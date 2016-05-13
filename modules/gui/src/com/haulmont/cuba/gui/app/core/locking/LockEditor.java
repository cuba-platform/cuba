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

package com.haulmont.cuba.gui.app.core.locking;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.LockDescriptor;
import com.haulmont.cuba.core.entity.LockDescriptorNameType;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.TreeMap;

/**
 */
public class LockEditor extends AbstractEditor {

    @Inject
    protected Metadata metadata;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected Datasource<LockDescriptor> lockDescriptorDs;

    @Inject
    protected OptionsGroup nameTypeOptionsGroup;

    @Inject
    protected FieldGroup fieldGroup;

    @Named("fieldGroup.name")
    protected TextField nameField;

    @Named("fieldGroup.timeoutSec")
    protected TextField timeoutSecField;

    @Override
    public void init(Map<String, Object> params) {
        Map<String, Object> options = new TreeMap<>();
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (metadata.getExtendedEntities().getExtendedClass(metaClass) == null) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                String originalName = originalMetaClass == null ? metaClass.getName() : originalMetaClass.getName();
                options.put(messages.getTools().getEntityCaption(metaClass) + " (" + metaClass.getName() + ")", originalName);
            }
        }

        fieldGroup.addCustomField("entity", (datasource, propertyId) -> {
            LookupField nameLookupField = componentsFactory.createComponent(LookupField.class);
            nameLookupField.setDatasource(lockDescriptorDs, "name");
            nameLookupField.setOptionsMap(options);

            return nameLookupField;
        });
        fieldGroup.setFieldCaption("entity", messages.getMessage(LockDescriptor.class, "LockDescriptor.name"));

        if (((LockDescriptor) WindowParams.ITEM.getEntity(params)).getName() != null) {
            nameTypeOptionsGroup.setVisible(false);
            fieldGroup.setVisible("entity", false);
            nameField.setEditable(false);
            timeoutSecField.requestFocus();
        } else {
            nameTypeOptionsGroup.setOptionsEnum(LockDescriptorNameType.class);
            nameTypeOptionsGroup.addValueChangeListener(e -> {
                if (LockDescriptorNameType.ENTITY.equals(e.getValue())) {
                    nameField.setVisible(false);
                    fieldGroup.setVisible("entity", true);
                    fieldGroup.getFieldComponent("entity").requestFocus();
                } else {
                    nameField.setVisible(true);
                    nameField.requestFocus();
                    fieldGroup.setVisible("entity", false);
                }
            });

            nameTypeOptionsGroup.setValue(LockDescriptorNameType.ENTITY);
        }
    }
}