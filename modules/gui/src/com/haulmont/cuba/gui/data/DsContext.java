/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:15:07
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;

import java.util.Collection;
import java.util.Map;

/**
 * Datasources context. Provides access to datasources
 * and automatic coordination between them on load/commit time.
 */
public interface DsContext {

    WindowContext getWindowContext();
    void setWindowContext(WindowContext context);

    DataService getDataService();

    /** Get datasource by name */
    <T extends Datasource> T get(String name);

    /** Get all datasources */
    Collection<Datasource> getAll();

    /** True if any of datasources is modified */
    boolean isModified();

    /** Refresh all datasources */
    void refresh();

    /** Commit all changed datasources */
    void commit();

    /**
     * Register dependency between datasources.
     * <br>Dependent datasource refreshed if one of the events occurs on master datasource:
     * <ul>
     * <li>itemChanged
     * <li>collectionChanged with Operation.REFRESH
     * <li>valueChanged with the specified property
     * </ul>
     */
    void registerDependency(Datasource ds, Datasource dependFrom, String property);

    void addListener(CommitListener listener);
    void removeListener(CommitListener listener);

    /**
     * This listener allows to intercept commit events.
     * <br>Can be used to augment CommitContext with entities which must be committed in the
     * same transaction as datasources content.
     */
    public interface CommitListener {
        /**
         * Invoked before sending data to the middleware
         */
        void beforeCommit(CommitContext<Entity> context);

        /**
         * Invoked after succesfull commit by middleware
         * @param context initial context
         * @param result map from initial to resulting committed entity
         */
        void afterCommit(CommitContext<Entity> context, Map<Entity, Entity> result);
    }
}

