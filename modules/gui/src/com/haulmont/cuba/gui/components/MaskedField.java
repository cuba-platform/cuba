/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;


import com.haulmont.chile.core.datatypes.impl.EnumClass;

/**
 * @author devyatkin
 * @version $Id$
 */
public interface MaskedField extends TextField {

    public enum ValueMode implements EnumClass<String> {

        MASKED("masked"),
        CLEAR("clear");

        private String id;

        ValueMode(String id) {
            this.id = id;
        }

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

    void setValueMode(ValueMode mode);

    ValueMode getValueMode();
}
