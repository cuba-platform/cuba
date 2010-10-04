/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 04.10.2010 14:26:30
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

public interface ChartDatasource<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

    Collection<ChartColumnInfo> getColumns();

    Number getColumnValue(Object rowId, ChartColumnInfo columnInfo);

    Collection<K> getRowIds();

    String getRowCaption(K rowId);
}
