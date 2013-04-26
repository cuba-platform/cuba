/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;

/**
 * @author abramov
 * @version $Id$
 */
public interface TextField
        extends
            TextInputField,
            TextInputField.MaxLengthLimited,
            TextInputField.TrimSupported,
            Component.HasFormatter {

    String NAME = "textField";

    Datatype getDatatype();
    void setDatatype(Datatype datatype);
}