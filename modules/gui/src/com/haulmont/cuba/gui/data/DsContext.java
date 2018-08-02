/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
 * <br>
 * Implementation of this interface serves also for automatic coordination between datasources on load/commit time.
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
    Datasource get(String name);

    /**
     * Get datasource by name. Name may contain path to a datasource in nested frame
     * e.g. {@code someFrameId.someDatasourceId}.
     * <p>Never returns null.</p>
     *
     * @param name  datasource name
     * @return      datasource instance
     * @throws java.lang.IllegalArgumentException if not found
     */
    Datasource getNN(String name);

    /**
     * @return all datasources contained in this context
     */
    Collection<Datasource> getAll();

    /**
     * Add alias for datasource.
     * @param aliasDatasourceId     additional datasource id
     * @param originalDatasourceId  original datasource id
     */
    void addAlias(String aliasDatasourceId, String originalDatasourceId);

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
     * Indicates that the DsContext will be discarded right after commit hence it doesn't need committed instances.
     */
    boolean isDiscardCommitted();

    /**
     * Set to true if the DsContext will be discarded right after commit hence it doesn't need committed instances.
     */
    void setDiscardCommitted(boolean discardCommitted);

    /**
     * Register dependency between datasources.
     * <br>Dependent datasource is refreshed if one of the following events occurs on master datasource:
     * <ul>
     * <li>itemChanged
     * <li>collectionChanged with Operation.REFRESH
     * <li>valueChanged with the specified property
     * </ul>
     * @param ds            dependent datasource
     * @param dependFrom    master datasource
     * @param property      property of master datasource. If specified, the dependent datasource will be refreshed
     *                      also on this property value change
     */
    void registerDependency(Datasource ds, Datasource dependFrom, @Nullable String property);

    /**
     * Add commit events listener.
     */
    @Deprecated
    void addListener(CommitListener listener);

    /**
     * Remove commit events listener.
     */
    @Deprecated
    void removeListener(CommitListener listener);

    void addBeforeCommitListener(BeforeCommitListener listener);
    void removeBeforeCommitListener(BeforeCommitListener listener);

    void addAfterCommitListener(AfterCommitListener listener);
    void removeAfterCommitListener(AfterCommitListener listener);

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
     * <br> Should be used to augment {@link CommitContext} with entities which must be committed in the
     * same transaction as datasources content. To do this, add needed entity instances to
     * {@link com.haulmont.cuba.core.global.CommitContext#getCommitInstances()} or
     * {@link com.haulmont.cuba.core.global.CommitContext#getRemoveInstances()} collections.
     *
     * @deprecated Use {@link com.haulmont.cuba.gui.data.DsContext.BeforeCommitListener} and {@link com.haulmont.cuba.gui.data.DsContext.AfterCommitListener}
     *
     * @see DsContext#addListener(com.haulmont.cuba.gui.data.DsContext.CommitListener)
     * @see DsContext#removeListener(com.haulmont.cuba.gui.data.DsContext.CommitListener)
     * @see CommitListenerAdapter
     */
    @Deprecated
    interface CommitListener {
        /**
         * Called before sending data to the middleware.
         * @param context   commit context
         */
        void beforeCommit(CommitContext context);

        /**
         * Called after a successful commit to the middleware.
         *
         * @param context commit context
         * @param result  set of committed entities returning from the middleware service
         */
        void afterCommit(CommitContext context, Set<Entity> result);
    }

    /**
     * An abstract adapter class for {@link CommitListener}. Use it if you need to implement only one of two methods.
     *
     * @see DsContext#addListener(com.haulmont.cuba.gui.data.DsContext.CommitListener)
     * @see DsContext#removeListener(com.haulmont.cuba.gui.data.DsContext.CommitListener)
     */
    @Deprecated
    abstract class CommitListenerAdapter implements CommitListener {
        @Override
        public void beforeCommit(CommitContext context) {
        }
        @Override
        public void afterCommit(CommitContext context, Set<Entity> result) {
        }
    }

    /**
     * This listener allows to intercept commit events.
     * <br> Should be used to augment {@link CommitContext} with entities which must be committed in the
     * same transaction as datasources content. To do this, add needed entity instances to
     * {@link com.haulmont.cuba.core.global.CommitContext#getCommitInstances()} or
     * {@link com.haulmont.cuba.core.global.CommitContext#getRemoveInstances()} collections.
     */
    interface BeforeCommitListener {
        /**
         * Called before sending data to the middleware.
         * @param context   commit context
         */
        void beforeCommit(CommitContext context);
    }

    interface AfterCommitListener {

        /**
         * Called after a successful commit to the middleware.
         *
         * @param context commit context
         * @param result  set of committed entities returning from the middleware service
         */
        void afterCommit(CommitContext context, Set<Entity> result);
    }
}