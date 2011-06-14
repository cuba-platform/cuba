/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public interface XYChartRowDatasource<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

    Collection<Object> getPointIds();

    Object getXValue(Object pointId);
    Object getYValue(Object pointId);
}
