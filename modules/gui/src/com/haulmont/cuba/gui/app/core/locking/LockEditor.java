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

public class LockEditor extends AbstractEditor {

    @Inject
    protected Metadata metadata;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected Datasource<LockDescriptor> lockDescriptorDs;

    @Inject
    protected OptionsGroup<LockDescriptorNameType, LockDescriptorNameType> nameTypeOptionsGroup;

    @Inject
    protected FieldGroup fieldGroup;

    @Named("fieldGroup.name")
    protected TextField nameField;

    @Named("fieldGroup.timeoutSec")
    protected TextField timeoutSecField;

    protected LookupField entityNameLookupField;

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

        entityNameLookupField = componentsFactory.createComponent(LookupField.class);
        entityNameLookupField.setDatasource(lockDescriptorDs, "name");
        entityNameLookupField.setOptionsMap(options);
        entityNameLookupField.setCaption(messages.getMessage(LockDescriptor.class, "LockDescriptor.name"));

        fieldGroup.getFieldNN("entity").setComponent(entityNameLookupField);

        if (((LockDescriptor) WindowParams.ITEM.getEntity(params)).getName() != null) {
            nameTypeOptionsGroup.setVisible(false);
            entityNameLookupField.setVisible(false);
            nameField.setEditable(false);
            timeoutSecField.focus();
        } else {
            nameTypeOptionsGroup.setOptionsEnum(LockDescriptorNameType.class);
            nameTypeOptionsGroup.addValueChangeListener(e -> {
                if (LockDescriptorNameType.ENTITY.equals(e.getValue())) {
                    nameField.setVisible(false);
                    entityNameLookupField.setVisible(true);
                    entityNameLookupField.focus();
                } else {
                    nameField.setVisible(true);
                    nameField.focus();
                    entityNameLookupField.setVisible(false);
                }
            });

            nameTypeOptionsGroup.setValue(LockDescriptorNameType.ENTITY);
        }
    }
}