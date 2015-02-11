/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * DEPRECATED! Use {@link com.haulmont.cuba.core.global.DataManager} directly.
 *
 * @author krivopustov
 * @version $Id$
 */
@Deprecated
public interface DataWorker {

    String NAME = "cuba_DataWorker";

    /**
     * Commit a collection of new or detached entity instances to the database.
     * @param context   {@link com.haulmont.cuba.core.global.CommitContext} object, containing committing entities and other information
     * @return          set of committed instances
     */
    Set<Entity> commit(CommitContext context);

    /**
     * Load a single entity instance.
     * <p>The depth of object graphs, starting from loaded instances, defined by {@link com.haulmont.cuba.core.global.View}
     * object passed in {@link com.haulmont.cuba.core.global.LoadContext}.</p>
     * @param context   {@link com.haulmont.cuba.core.global.LoadContext} object, defining what and how to load
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
