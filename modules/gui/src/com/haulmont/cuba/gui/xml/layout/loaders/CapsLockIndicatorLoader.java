/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.components.CapsLockIndicator;
import com.haulmont.cuba.gui.components.Component;

public class CapsLockIndicatorLoader extends AbstractComponentLoader<CapsLockIndicator> {

    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(CapsLockIndicator.class);

        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        loadAlign(resultComponent, element);
        loadStyleName(resultComponent, element);
        loadVisible(resultComponent, element);

        loadWidth(resultComponent, element, Component.AUTO_SIZE);
        loadHeight(resultComponent, element, Component.AUTO_SIZE);

        String capsLockOnMessage = element.attributeValue("capsLockOnMessage");
        if (capsLockOnMessage != null) {
            capsLockOnMessage = loadResourceString(capsLockOnMessage);
            resultComponent.setCapsLockOnMessage(capsLockOnMessage);
        } else {
            resultComponent.setCapsLockOnMessage(getMessages().getMainMessage("capsLockIndicator.capsLockOnMessage"));
        }

        String capsLockOffMessage = element.attributeValue("capsLockOffMessage");
        if (!Strings.isNullOrEmpty(capsLockOffMessage)) {
            capsLockOffMessage = loadResourceString(capsLockOffMessage);
            resultComponent.setCapsLockOffMessage(capsLockOffMessage);
        }
    }
}