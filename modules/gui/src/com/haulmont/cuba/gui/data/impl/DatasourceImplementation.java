/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 21.01.2009 19:31:49
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Map;
import java.util.Collection;

public interface DatasourceImplementation<T extends Entity> {
    void initialized();

    Collection<T> getItemsToCreate();
    Collection<T> getItemsToUpdate();
    Collection<T> getItemsToDelete();

    void commited(Map<Entity, Entity> map);
}
