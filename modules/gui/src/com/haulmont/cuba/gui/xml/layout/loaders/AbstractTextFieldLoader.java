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
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.TextInputField;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public abstract class AbstractTextFieldLoader<T extends TextInputField> extends AbstractFieldLoader<T> {

    protected void loadTrimming(TextInputField.TrimSupported component, Element element) {
        String trim = element.attributeValue("trim");
        if (StringUtils.isNotEmpty(trim)) {
            component.setTrimming(Boolean.parseBoolean(trim));
        }
    }

    protected void loadMaxLength(TextInputField.MaxLengthLimited component, Element element) {
        final String maxLength = element.attributeValue("maxLength");
        if (!StringUtils.isEmpty(maxLength)) {
            component.setMaxLength(Integer.parseInt(maxLength));
        }
    }
}