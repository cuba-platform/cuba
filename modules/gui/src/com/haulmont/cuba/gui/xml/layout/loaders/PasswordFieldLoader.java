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
import com.haulmont.cuba.gui.components.CapsLockIndicator;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PasswordField;
import org.apache.commons.lang.StringUtils;

public class PasswordFieldLoader extends AbstractTextFieldLoader<PasswordField> {
    @Override
    public void createComponent() {
        resultComponent = (PasswordField) factory.createComponent(PasswordField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadMaxLength(resultComponent, element);

        String autocomplete = element.attributeValue("autocomplete");
        if (StringUtils.isNotEmpty(autocomplete)) {
            resultComponent.setAutocomplete(Boolean.parseBoolean(autocomplete));
        }

        String capsLockIndicator = element.attributeValue("capsLockIndicator");
        if (StringUtils.isNotEmpty(capsLockIndicator)) {
            context.addPostWrapTask((context, window) -> {
                if (resultComponent.getCapsLockIndicator() == null) {
                    Component bindComponent = resultComponent.getFrame().getComponent(capsLockIndicator);
                    if (!(bindComponent instanceof CapsLockIndicator)) {
                        throw new GuiDevelopmentException("Specify 'capsLockIndicator' attribute: id of " +
                                "CapsLockIndicator component", context.getFullFrameId(), "componentId", resultComponent
                                .getId());
                    }
                    resultComponent.setCapsLockIndicator((CapsLockIndicator) bindComponent);
                }
            });
        }
    }
}