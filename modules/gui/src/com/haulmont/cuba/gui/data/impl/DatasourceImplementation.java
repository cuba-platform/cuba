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
import com.haulmont.cuba.gui.data.Datasource;

import java.util.Map;
import java.util.Collection;
import java.util.Set;

public interface DatasourceImplementation<T extends Entity> {
    void initialized();
    void valid();
    void setModified(boolean modified);
    void setCommitMode(Datasource.CommitMode commitMode);

    Datasource getParent();
    void setParent(Datasource datasource);

    Collection<T> getItemsToCreate();
    Collection<T> getItemsToUpdate();
    Collection<T> getItemsToDelete();

    void modified(T item);
    void deleted(T item);

    void committed(Set<Entity> entities);
}
