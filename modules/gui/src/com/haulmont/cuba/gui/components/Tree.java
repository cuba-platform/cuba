/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.01.2009 12:58:48
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;

public interface Tree extends Component, Component.BelongToWindow
{
    <T> T getSelected();

    void expandTree();

    CollectionDatasource getDatasource();
    void setDatasource(CollectionDatasource datasource, String showProperty, String parentProperty);
}
