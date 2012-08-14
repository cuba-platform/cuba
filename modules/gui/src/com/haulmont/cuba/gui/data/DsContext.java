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
import com.haulmont.cuba.gui.WindowContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    /**
     * Commit all changed datasources.
     * @return true if there were changes and commit has been done
     */
    boolean commit();

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

    @Nullable
    DsContext getParent();
    
    @Nonnull
    List<DsContext> getChildren();

    /**
     * This listener allows to intercept commit events.
     * <br>Can be used to augment CommitContext with entities which must be committed in the
     * same transaction as datasources content.
     */
    public interface CommitListener extends Serializable {
        /**
         * Invoked before sending data to the middleware
         * @param context   commit context
         */
        void beforeCommit(CommitContext<Entity> context);

        /**
         * Invoked after succesfull commit to middleware
         * @param context   commit context
         * @param result    set of committed entities returning from the middleware service
         */
        void afterCommit(CommitContext<Entity> context, Set<Entity> result);
    }
}

