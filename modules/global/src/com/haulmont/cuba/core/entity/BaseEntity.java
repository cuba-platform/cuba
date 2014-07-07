/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import java.util.Date;
import java.util.UUID;

/**
 * Base interface for persistent entities.
 * @param <T> identifier type
 *
 * @author krivopustov
 * @version $Id$
 */
public interface BaseEntity<T> extends Entity<T> {

    int LOGIN_FIELD_LEN = 50;

    String[] PROPERTIES = {"id", "createTs", "createdBy"};

    /**
     * For internal use.
     * If you need to check instance state, use {@link com.haulmont.cuba.core.global.PersistenceHelper} methods.
     * @return true if the entity is detached from persistence context
     */
    boolean isDetached();

    /**
     * Called by the framework when the entity is detached or attached to a persistence context.
     */
    void setDetached(boolean detached);

    UUID getUuid();

    Date getCreateTs();

    void setCreateTs(Date date);

    String getCreatedBy();

    void setCreatedBy(String createdBy);
}
