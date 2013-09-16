/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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