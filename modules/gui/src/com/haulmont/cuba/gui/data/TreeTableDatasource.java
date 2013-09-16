/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

/**
 * HierarchicalDatasource which supports highlighting some items as captions 
 * @param <T> type of entity
 * @param <K> type of entity ID
 *
 * @author Gorodnov
 * @version $Id$
 */
public interface TreeTableDatasource <T extends Entity<K>, K>
        extends HierarchicalDatasource<T, K> {

    boolean isCaption(K itemId);

    String getCaption(K itemId);
}
