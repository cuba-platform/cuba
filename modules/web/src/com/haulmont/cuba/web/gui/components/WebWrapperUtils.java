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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.TextInputField;
import com.vaadin.ui.AbstractTextField;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public final class WebWrapperUtils {
    private WebWrapperUtils() {
    }

    public static TextInputField.TextChangeEventMode toTextChangeEventMode(AbstractTextField.TextChangeEventMode mode) {
        switch (mode) {
            case EAGER:
                return TextInputField.TextChangeEventMode.EAGER;
            case LAZY:
                return TextInputField.TextChangeEventMode.LAZY;
            case TIMEOUT:
                return TextInputField.TextChangeEventMode.TIMEOUT;
            default:
                throw new IllegalStateException("Unsupported Vaadin TextChangeEventMode");
        }
    }

    public static AbstractTextField.TextChangeEventMode toVaadinTextChangeEventMode(TextInputField.TextChangeEventMode mode) {
        checkNotNullArgument(mode);

        AbstractTextField.TextChangeEventMode vMode = AbstractTextField.TextChangeEventMode.LAZY;
        switch (mode) {
            case EAGER:
                vMode = AbstractTextField.TextChangeEventMode.EAGER;
                break;
            case LAZY:
                vMode = AbstractTextField.TextChangeEventMode.LAZY;
                break;
            case TIMEOUT:
                vMode = AbstractTextField.TextChangeEventMode.TIMEOUT;
                break;
        }

        return vMode;
    }
}