/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DbDialect;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.NotDetachedCommitContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Middleware service interface to provide standard CRUD functionality.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface DataService {

    String NAME = "cuba_DataService";

    /**
     * Get current database dialect
     * @return  DbDialect instance
     */
    DbDialect getDbDialect();

    /**
     * Commit a collection of new or detached entity instances to the database.
     * @param context   {@link CommitContext} object, containing committing entities and other information
     * @return          set of committed instances
     */
    Set<Entity> commit(CommitContext context);

    /**
     * Commit a collection of entity instances to the database. This method is used for clients, not supporting
     * transfer of detached state, e.g. REST API. In this case new entity identificators passed explicitly in
     * {@link NotDetachedCommitContext} to differentiate what entities have to be persisted and what have to be merged.
     *
     * @param context   {@link NotDetachedCommitContext} object, containing committing entities and other information
     * @return          map of passed instances to committed instances
     */
    Map<Entity, Entity> commitNotDetached(NotDetachedCommitContext context);

    /**
     * Load a single entity instance.
     * <p>The depth of object graphs, starting from loaded instances, defined by {@link com.haulmont.cuba.core.global.View}
     * object passed in {@link LoadContext}.</p>
     * @param context   {@link LoadContext} object, defining what and how to load
     * @return          the loaded detached object, or null if not found
     */
    @Nullable
    <A extends Entity> A load(LoadContext context);

    /**
     * Load collection of entity instances.
     * <p>The depth of object graphs, starting from loaded instances, defined by {@link com.haulmont.cuba.core.global.View}
     * object passed in {@link LoadContext}.</p>
     * @param context   {@link LoadContext} object, defining what and how to load
     * @return          a list of detached instances, or empty list if nothing found
     */
    @Nonnull
    <A extends Entity> List<A> loadList(LoadContext context);
}
