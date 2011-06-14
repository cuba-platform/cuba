/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author zagumennikov
 */
public interface CategoryChartDatasource<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

    Collection<Object> getRowIds();
    Collection<Object> getCategoryIds();

    String getRowCaption(Object rowId);
    String getCategoryCaption(Object categoryId);

    Object getValue(Object rowId, Object categoryId);
}
