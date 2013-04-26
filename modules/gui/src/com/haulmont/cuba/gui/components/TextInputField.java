/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author artamonov
 * @version $Id$
 */
public interface TextInputField extends Field {

    public interface TrimSupported extends TextInputField {
        boolean isTrimming();
        void setTrimming(boolean trimming);
    }

    public interface MaxLengthLimited extends TextInputField {
        int getMaxLength();
        void setMaxLength(int value);
    }
}