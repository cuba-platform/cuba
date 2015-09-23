/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author devyatkin
 * @version $Id$
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
}