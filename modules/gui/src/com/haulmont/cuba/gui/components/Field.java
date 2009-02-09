/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.01.2009 16:33:50
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.Datasource;

public interface Field
    extends
        Component, Component.HasCaption, Component.Field, Component.Editable, Component.BelongToWindow
{
    Datasource getDatasource();
    String getProperty();

    void setDatasource(Datasource datasource, String property);
}
