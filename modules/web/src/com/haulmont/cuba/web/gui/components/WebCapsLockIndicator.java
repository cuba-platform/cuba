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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.CapsLockIndicator;
import com.haulmont.cuba.web.widgets.CubaCapsLockIndicator;

public class WebCapsLockIndicator extends WebAbstractComponent<CubaCapsLockIndicator> implements CapsLockIndicator {

    public WebCapsLockIndicator() {
        component = new CubaCapsLockIndicator();
    }

    @Override
    public void setCapsLockOnMessage(String capsLockOnMessage) {
        component.setCapsLockOnMessage(capsLockOnMessage);
    }

    @Override
    public String getCapsLockOnMessage() {
        return component.getCapsLockOnMessage();
    }

    @Override
    public void setCapsLockOffMessage(String capsLockOffMessage) {
        component.setCapsLockOffMessage(capsLockOffMessage);
    }

    @Override
    public String getCapsLockOffMessage() {
        return component.getCapsLockOffMessage();
    }
}
