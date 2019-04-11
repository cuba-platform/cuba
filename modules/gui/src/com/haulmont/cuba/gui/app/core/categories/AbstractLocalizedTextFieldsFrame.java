/*
 * Copyright (c) 2008-2019 Haulmont.
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

import com.haulmont.cuba.core.entity.LocaleHelper;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractLocalizedTextFieldsFrame extends AbstractFrame {

    @Inject
    protected UiComponents uiComponents;

    protected Component createTextFieldComponent(Locale locale, String caption, Map<Locale, TextField> textFieldMap) {
        TextField valueField = uiComponents.create(TextField.TYPE_STRING);
        valueField.setWidth("100%");
        valueField.setCaption(caption);

        textFieldMap.put(locale, valueField);

        return valueField;
    }

    protected Component createTextAreaComponent(Locale locale, String caption, Map<Locale, TextArea> textFieldMap) {
        TextArea<String> valueField = uiComponents.create(TextArea.TYPE_STRING);
        valueField.setWidth("100%");
        valueField.setRows(3);
        valueField.setCaption(caption);

        textFieldMap.put(locale, valueField);

        return valueField;
    }

    protected Component createLabelComponent(String labelText) {
        Label<String> label = uiComponents.create(Label.TYPE_STRING);
        label.setValue(labelText);
        label.setWidth("100%");
        return label;
    }

    protected String getValue(Map<Locale, ? extends TextInputField> textFieldMap) {
        Properties properties = new Properties();
        for (Map.Entry<Locale, ? extends TextInputField> entry : textFieldMap.entrySet()) {
            if (!getTextInputFieldRawValue(entry.getValue()).isEmpty()) {
                properties.setProperty(entry.getKey().toString(), getTextInputFieldRawValue(entry.getValue()));
            }
        }

        return LocaleHelper.convertPropertiesToString(properties);
    }

    protected String getTextInputFieldRawValue(TextInputField textInputField) {
        if (textInputField instanceof TextField) {
            return ((TextField) textInputField).getRawValue();
        }
        if (textInputField instanceof TextArea) {
            return ((TextArea) textInputField).getRawValue();
        }
        return "";
    }

    protected void setValue(String localeBundle, Map<Locale, ? extends TextInputField> textFieldMap) {
        if (localeBundle == null || textFieldMap == null) {
            return;
        }

        Map<String, String> localizedNamesMap = LocaleHelper.getLocalizedValuesMap(localeBundle);
        for (Map.Entry<Locale, ? extends TextInputField> textFieldEntry : textFieldMap.entrySet()) {
            String keyLocale = textFieldEntry.getKey().toString();
            textFieldEntry.getValue().setValue(localizedNamesMap.get(keyLocale));
        }
    }
}
