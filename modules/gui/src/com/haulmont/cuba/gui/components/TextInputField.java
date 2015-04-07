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

    interface TrimSupported extends TextInputField {
        boolean isTrimming();
        void setTrimming(boolean trimming);
    }

    interface MaxLengthLimited extends TextInputField {
        int getMaxLength();
        void setMaxLength(int value);
    }

    interface CursorPositionSupported extends TextInputField {
        /**
         * Sets the cursor position in the field.
         *
         * @param position new cursor position
         */
        void setCursorPosition(int position);
    }
}