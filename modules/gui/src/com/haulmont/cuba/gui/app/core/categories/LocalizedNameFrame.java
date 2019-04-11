/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.app.core.categories;

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.gui.components.*;

import javax.inject.Inject;
import java.util.*;

public class LocalizedNameFrame extends AbstractLocalizedTextFieldsFrame {

    @Inject
    protected ScrollBoxLayout localesScrollBox;

    @Inject
    protected GlobalConfig globalConfig;

    protected Map<Locale, TextField> textFieldMap = new HashMap<>();

    @Override
    public void init(Map<String, Object> params) {
        Map<String, Locale> map = globalConfig.getAvailableLocales();
        for (Map.Entry<String, Locale> entry : map.entrySet()) {
            localesScrollBox.add(createTextFieldComponent(entry.getValue(),
                    entry.getKey() + "|" + entry.getValue(), textFieldMap));
        }
    }

    public String getValue() {
        return getValue(textFieldMap);
    }

    public void setValue(String localeBundle) {
        setValue(localeBundle, textFieldMap);
    }

    public void clearFields() {
        for (TextField textField : textFieldMap.values()) {
            textField.setValue("");
        }
    }

    public void setEditableFields(boolean editable) {
        for (TextField textField : textFieldMap.values()) {
            textField.setEditable(editable);
        }
    }
}
