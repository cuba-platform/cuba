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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupPickerField;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class LookupPickerFieldLoader extends LookupFieldLoader {

    @Override
    public void createComponent() {
        resultComponent = (LookupPickerField) factory.createComponent(LookupPickerField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        LookupPickerField lookupPickerField = (LookupPickerField) resultComponent;

        final String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            Metadata metadata = AppBeans.get(Metadata.NAME);
            lookupPickerField.setMetaClass(metadata.getSession().getClass(metaClass));
        }

        loadActions(lookupPickerField, element);
        if (lookupPickerField.getActions().isEmpty()) {
            lookupPickerField.addLookupAction();
            lookupPickerField.addOpenAction();
        }

        String refreshOptionsOnLookupClose = element.attributeValue("refreshOptionsOnLookupClose");
        if (refreshOptionsOnLookupClose != null) {
            lookupPickerField.setRefreshOptionsOnLookupClose(Boolean.valueOf(refreshOptionsOnLookupClose));
        }
    }

    @Override
    protected Action loadDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        return loadPickerDeclarativeAction(actionsHolder, element);
    }
}