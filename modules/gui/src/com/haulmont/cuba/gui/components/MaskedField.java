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

package com.haulmont.cuba.gui.components;

/**
 * Masked field component generic interface.
 * FieldConfig supports following format symbols:
 * <ul>
 * <li># - Digit</li>
 * <li>U - Uppercase letter</li>
 * <li>L - Lowercase letter</li>
 * <li>A - Letter or digit</li>
 * <li>* - Any symbol</li>
 * <li>H - Hex symbol</li>
 * <li>~ - + or -</li>
 * </ul>
 * Any other symbols in format will be treated as mask literals.
 */
public interface MaskedField<V>
        extends
            TextInputField<V>,
            HasDatatype<V>,
            TextInputField.TextSelectionSupported,
            TextInputField.CursorPositionSupported,
            TextInputField.EnterPressNotifier {

    String NAME = "maskedField";

    void setMask(String mask);
    String getMask();

    /**
     * Sets ValueMode for component
     * <p>
     * MASKED - value contain mask literals
     * CLEAR - value contain only user input.
     * </p>
     *
     * @param mode value mode
     */
    void setValueMode(ValueMode mode);
    ValueMode getValueMode();

    boolean isSendNullRepresentation();
    void setSendNullRepresentation(boolean sendNullRepresentation);

    /**
     * Returns a string representation of the value.
     */
    String getRawValue();

    enum ValueMode {
        MASKED,
        CLEAR
    }
}