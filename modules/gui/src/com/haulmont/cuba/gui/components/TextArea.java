/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author abramov
 * @version $Id$
 */
public interface TextArea extends TextInputField, TextInputField.MaxLengthLimited, TextInputField.TrimSupported {

    String NAME = "textArea";

    int getRows();
    void setRows(int rows);

    int getColumns();
    void setColumns(int columns);
}