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

import com.haulmont.chile.core.datatypes.impl.EnumClass;

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
 *
 */
public interface MaskedField extends TextField {

    enum ValueMode implements EnumClass<String> {
        MASKED("masked"),
        CLEAR("clear");

        private String id;

        ValueMode(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        public static ValueMode fromId(String id) {
            for (ValueMode mode : ValueMode.values()) {
                if (mode.getId().equals(id)) {
                    return mode;
                }
            }
            return null;
        }
    }

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
     * @param mode
     */
    void setValueMode(ValueMode mode);

    ValueMode getValueMode();

    boolean isSendNullRepresentation();
    void setSendNullRepresentation(boolean sendNullRepresentation);

    @SuppressWarnings("unchecked")
    @Override
    String getValue();
}