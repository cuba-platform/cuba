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

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.SearchField;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

public class SearchFieldLoader extends LookupFieldLoader {
    @Override
    public void createComponent() {
        resultComponent = (SearchField) factory.createComponent(SearchField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        SearchField searchField = (SearchField) resultComponent;

        String minSearchStringLength = element.attributeValue("minSearchStringLength");
        if (StringUtils.isNotEmpty(minSearchStringLength)) {
            searchField.setMinSearchStringLength(Integer.parseInt(minSearchStringLength));
        }

        String modeString = element.attributeValue("mode");
        if (StringUtils.isNotEmpty(modeString)) {
            SearchField.Mode mode;
            try {
                mode = SearchField.Mode.valueOf(StringUtils.upperCase(modeString));
            } catch (IllegalArgumentException e) {
                throw new GuiDevelopmentException("Unable to parse mode for search",
                        context.getFullFrameId(), "mode", modeString);
            }
            searchField.setMode(mode);
        }

        String escapeValueForLike = element.attributeValue("escapeValueForLike");
        if (StringUtils.isNotEmpty(escapeValueForLike)) {
            searchField.setEscapeValueForLike(Boolean.parseBoolean(escapeValueForLike));
        }
    }

    @Override
    protected void loadTextInputAllowed() {
        // do nothing
    }
}