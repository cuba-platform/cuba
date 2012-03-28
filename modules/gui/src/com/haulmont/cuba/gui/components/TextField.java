/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:10:30
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;

public interface TextField extends Field, Component.HasFomatter {

    String NAME = "textField";

    int getRows();
    void setRows(int rows);

    int getColumns();
    void setColumns(int columns);

    boolean isSecret();
    void setSecret(boolean secret);

    int getMaxLength();
    void setMaxLength(int value);

    boolean isTrimming();
    void setTrimming(boolean trimming);

    Datatype getDatatype();
    void setDatatype(Datatype datatype);
}
