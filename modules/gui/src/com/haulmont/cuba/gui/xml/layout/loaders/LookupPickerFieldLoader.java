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

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Actions;
import com.haulmont.cuba.gui.components.ActionsHolder;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.components.actions.GuiActionSupport;
import com.haulmont.cuba.gui.components.actions.pickerfield.ClearAction;
import com.haulmont.cuba.gui.components.actions.pickerfield.LookupAction;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class LookupPickerFieldLoader extends LookupFieldLoader {

    @Override
    public void createComponent() {
        resultComponent = factory.create(LookupPickerField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        LookupPickerField lookupPickerField = (LookupPickerField) resultComponent;

        String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            lookupPickerField.setMetaClass(getMetadata().getClass(metaClass));
        }

        loadActions(lookupPickerField, element);

        if (lookupPickerField.getActions().isEmpty()) {
            GuiActionSupport guiActionSupport = getGuiActionSupport();

            boolean actionsByMetaAnnotations = guiActionSupport.createActionsByMetaAnnotations(lookupPickerField);
            if (!actionsByMetaAnnotations) {

                if (isLegacyFrame()) {
                    lookupPickerField.addLookupAction();
                    lookupPickerField.addClearAction();
                } else {
                    Actions actions = getActions();

                    lookupPickerField.addAction(actions.create(LookupAction.ID));
                    lookupPickerField.addAction(actions.create(ClearAction.ID));
                }
            }
        }

        String refreshOptionsOnLookupClose = element.attributeValue("refreshOptionsOnLookupClose");
        if (refreshOptionsOnLookupClose != null) {
            lookupPickerField.setRefreshOptionsOnLookupClose(Boolean.parseBoolean(refreshOptionsOnLookupClose));
        }
    }

    protected GuiActionSupport getGuiActionSupport() {
        return beanLocator.get(GuiActionSupport.NAME);
    }

    protected Actions getActions() {
        return beanLocator.get(Actions.NAME);
    }

    protected Metadata getMetadata() {
        return beanLocator.get(Metadata.NAME);
    }

    @Override
    protected Action loadDeclarativeAction(ActionsHolder actionsHolder, Element element) {
        return loadPickerDeclarativeAction(actionsHolder, element);
    }
}