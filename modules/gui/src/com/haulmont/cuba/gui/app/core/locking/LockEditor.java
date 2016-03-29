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
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;

import javax.inject.Inject;
import java.util.Map;
import java.util.TreeMap;

/**
 */
public class LockEditor extends AbstractEditor {

    @Inject
    protected Metadata metadata;

    @Inject
    protected LookupField nameLookupField;

    @Inject
    protected TextField timeoutField;

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams().setWidthAuto();

        Map<String, Object> options = new TreeMap<>();
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (metadata.getExtendedEntities().getExtendedClass(metaClass) == null) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                String originalName = originalMetaClass == null ? metaClass.getName() : originalMetaClass.getName();
                options.put(messages.getTools().getEntityCaption(metaClass) + " (" + metaClass.getName() + ")", originalName);
            }
        }
        nameLookupField.setOptionsMap(options);
        if (((LockDescriptor) params.get("item".toUpperCase())).getName() != null) {
            nameLookupField.setEditable(false);
            timeoutField.requestFocus();
        }
    }
}