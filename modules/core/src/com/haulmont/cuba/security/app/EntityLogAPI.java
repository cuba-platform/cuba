/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.entity.BaseEntity;

/**
 * Allows to log entity lifecycle events: create, modify, delete.
 * <p/>
 * Configured by {@link com.haulmont.cuba.security.entity.LoggedEntity} and
 * {@link com.haulmont.cuba.security.entity.LoggedAttribute} entities.
 * See also {@link com.haulmont.cuba.security.app.EntityLogConfig} configuration interface.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface EntityLogAPI {

    String NAME = "cuba_EntityLog";

    boolean isEnabled();
    void setEnabled(boolean enabled);

    void registerCreate(BaseEntity entity);

    void registerCreate(BaseEntity entity, boolean auto);

    void registerModify(BaseEntity entity);

    void registerModify(BaseEntity entity, boolean auto);

    void registerDelete(BaseEntity entity);

    void registerDelete(BaseEntity entity, boolean auto);

    /**
     * Invalidates configuration cache.
     * The configuration will be recreated from the database on next lifecycle event.
     */
    void invalidateCache();

    /**
     * Disables/enables entity logging for current thread.
     * Enabled by default.
     *
     * @param enabled entity logging disabled if false, enabled otherwise.
     */
    void processLoggingForCurrentThread(Boolean enabled);
}
