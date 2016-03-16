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
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.PickerField;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Named;
import java.util.Map;

/**
 */
public class GroupEditor extends AbstractEditor {

    @Named("fieldGroup.parent")
    protected PickerField parentField;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        if (BooleanUtils.isTrue((Boolean) params.get("edit"))) {
            parentField.setVisible(true);
            PickerField.LookupAction lookupAction = new PickerField.LookupAction(parentField);
            lookupAction.setLookupScreenParams(ParamsMap.of("exclude", params.get("ITEM")));
            parentField.addAction(lookupAction);
        }
    }
}