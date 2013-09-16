/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

public interface LazyCollectionDatasourceListener<T extends Entity> extends CollectionDatasourceListener<T> {

    void completelyLoaded(CollectionDatasource.Lazy ds);
}
