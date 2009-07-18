/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 18:10:44
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

public interface CollectionDatasourceListener<T extends Entity> extends DatasourceListener<T> {

    public enum Operation {
        REFRESH,
        ADD,
        REMOVE
    }

    void collectionChanged(CollectionDatasource ds, Operation operation);
}
