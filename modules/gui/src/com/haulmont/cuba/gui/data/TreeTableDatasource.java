/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 02.06.2009
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

/**
 * HierarchicalDatasource which supports highlighting some items as captions 
 * @param <T> type of entity
 * @param <K> type of entity ID
 */
public interface TreeTableDatasource <T extends Entity<K>, K>
        extends HierarchicalDatasource<T, K>
{
    boolean isCaption(K itemId);

    String getCaption(K itemId);
}
