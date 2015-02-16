/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

    /**
     * @return current input prompt.
     */
    String getInputPrompt();
    /**
     * Sets the input prompt - a textual prompt that is displayed when the field
     * would otherwise be empty, to prompt the user for input.
     *
     * @param inputPrompt input prompt
     */
    void setInputPrompt(String inputPrompt);
}