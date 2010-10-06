/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 05.10.2010 16:35:13
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

public interface LazyCollectionDatasourceListener<T extends Entity> extends CollectionDatasourceListener<T> {

    void completelyLoaded(CollectionDatasource.Lazy ds);
}
