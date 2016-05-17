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

package com.haulmont.cuba.gui.app.security.group.edit;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.PickerField;

import javax.inject.Named;

/**
 */
public class GroupEditor extends AbstractEditor {

    @Named("fieldGroup.parent")
    protected PickerField parentField;

    @Override
    protected void postInit() {
        super.postInit();
        if (!PersistenceHelper.isNew(getItem())) {
            parentField.setVisible(true);
            PickerField.LookupAction lookupAction = new PickerField.LookupAction(parentField);
            lookupAction.setLookupScreenParams(ParamsMap.of("exclude", getItem()));
            parentField.addAction(lookupAction);
        }
    }
}