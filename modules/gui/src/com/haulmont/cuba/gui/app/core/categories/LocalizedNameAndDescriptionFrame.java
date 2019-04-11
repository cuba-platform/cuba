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
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.components.*;

import javax.inject.Inject;
import java.util.*;

public class LocalizedNameAndDescriptionFrame extends AbstractLocalizedTextFieldsFrame {

    private static final String MESSAGE_PACK = "msg://com.haulmont.cuba.core.entity/";

    @Inject
    protected ScrollBoxLayout localesScrollBox;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected MessageTools messageTools;


    protected Map<Locale, TextField> namesTextFieldMap = new HashMap<>();
    protected Map<Locale, TextArea> descriptionsTextFieldMap = new HashMap<>();

    @Override
    public void init(Map<String, Object> params) {
        Map<String, Locale> map = globalConfig.getAvailableLocales();
        for (Map.Entry<String, Locale> entry : map.entrySet()) {
            localesScrollBox.add(createLabelComponent(entry.getKey() + "|" + entry.getValue().toString()));
            localesScrollBox.add(createTextFieldComponent(entry.getValue(),
                    messageTools.loadString(MESSAGE_PACK + "CategoryAttribute.name"), namesTextFieldMap));
            localesScrollBox.add(createTextAreaComponent(entry.getValue(),
                    messageTools.loadString(MESSAGE_PACK + "CategoryAttribute.description"), descriptionsTextFieldMap));
        }
    }

    public String getNamesValue() {
        return getValue(namesTextFieldMap);
    }

    public String getDescriptionsValue() {
        return getValue(descriptionsTextFieldMap);
    }

    public void setNamesValue(String localeBundle) {
        setValue(localeBundle, namesTextFieldMap);
    }

    public void setDescriptionsValue(String localeBundle) {
        setValue(localeBundle, descriptionsTextFieldMap);
    }
}
