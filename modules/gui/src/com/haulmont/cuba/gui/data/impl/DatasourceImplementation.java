/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.Collection;
import java.util.Set;

/**
 * @author abramov
 * @version $Id$
 */
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

    /**
     * Enables or disables datasource listeners.
     * @param enable    true to enable, false to disable
     * @return          previous state
     */
    boolean enableListeners(boolean enable);
}