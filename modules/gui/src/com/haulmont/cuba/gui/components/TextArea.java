/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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