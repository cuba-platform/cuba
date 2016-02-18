/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;

/**
 * @author abramov
 */
public interface TextField
        extends
            TextInputField,
            TextInputField.MaxLengthLimited,
            TextInputField.TrimSupported,
            TextInputField.CursorPositionSupported,
            Component.HasFormatter,
            Component.HasInputPrompt {

    String NAME = "textField";

    Datatype getDatatype();
    void setDatatype(Datatype datatype);
}