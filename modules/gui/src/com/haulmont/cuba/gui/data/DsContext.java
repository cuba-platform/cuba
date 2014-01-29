/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.gui.FrameContext;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Interface providing access to datasources defined in a screen.
 * <p/>
 * Implementation of this interface serves also for automatic coordination between datasources on load/commit time.
 *
 * @author Abramov
 * @version $Id$
 */
public interface DsContext {

    /**
     * @return context of a frame that owns this DsContext.
     * If the DsContext belongs to {@code Window}, its {@code WindowContext} is returned.
     */
    FrameContext getFrameContext();
    void setFrameContext(FrameContext context);

    /**
     * @return a reference to DataSupplier
     */
    DataSupplier getDataSupplier();

    /**
     * Get datasource by name. Name may contain path to a datasource in nested frame
     * e.g. {@code someFrameId.someDatasourceId}
     *
     * @param name  datasource name
     * @return      datasource instance or null if not found
     */
    @Nullable
    <T extends Datasource> T get(String name);

    /**
     * @return all datasources contained in this context
     */
    Collection<Datasource> getAll();

    /**
     * @return true if any contained datasource is modified
     */
    boolean isModified();

    /**
     * Refresh all datasources.
     */
    void refresh();

    /**
     * Commit all changed datasources.
     * @return true if there were changes and commit has been done
     */
    boolean commit();

    /**
     * Register dependency between datasources.
     * <br>Dependent datasource is refreshed if one of the following events occurs on master datasource:
     * <ul>
     * <li>itemChanged
     * <li>collectionChanged with Operation.REFRESH
     * <li>valueChanged with the specified property
     * </ul>
     */
    void registerDependency(Datasource ds, Datasource dependFrom, String property);

    /**
     * Add commit events listener.
     */
    void addListener(CommitListener listener);

    /**
     * Remove commit events listener.
     */
    void removeListener(CommitListener listener);

    /**
     * @return a parent DsContext if this DsContext is defined in a frame
     */
    @Nullable
    DsContext getParent();

    /**
     * @return list of DsContext's of frames included in the current screen, if any
     */
    List<DsContext> getChildren();

    /**
     * This listener allows to intercept commit events.
     * <p/> Should be used to augment {@link CommitContext} with entities which must be committed in the
     * same transaction as datasources content. To do this, add needed entity instances to
     * {@link com.haulmont.cuba.core.global.CommitContext#getCommitInstances()} or
     * {@link com.haulmont.cuba.core.global.CommitContext#getRemoveInstances()} collections.
     *
     * @see DsContext#addListener(com.haulmont.cuba.gui.data.DsContext.CommitListener)
     * @see DsContext#removeListener(com.haulmont.cuba.gui.data.DsContext.CommitListener)
     * @see CommitListenerAdapter
     */
    public interface CommitListener {
        /**
         * Called before sending data to the middleware.
         * @param context   commit context
         */
        void beforeCommit(CommitContext context);

        /**
         * Called after a succesfull commit to the middleware.
         * @param context   commit context
         * @param result    set of committed entities returning from the middleware service
         */
        void afterCommit(CommitContext context, Set<Entity> result);
    }

    /**
     * An abstract adapter class for {@link CommitListener}. Use it if you need to implement only one of two methods.
     *
     * @see DsContext#addListener(com.haulmont.cuba.gui.data.DsContext.CommitListener)
     * @see DsContext#removeListener(com.haulmont.cuba.gui.data.DsContext.CommitListener)
     */
    public abstract class CommitListenerAdapter implements CommitListener {
        @Override
        public void beforeCommit(CommitContext context) {
        }
        @Override
        public void afterCommit(CommitContext context, Set<Entity> result) {
        }
    }
}

