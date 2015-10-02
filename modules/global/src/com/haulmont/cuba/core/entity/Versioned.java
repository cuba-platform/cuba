/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

/**
 * Interface to be implemented by optimistically locked entities.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Versioned {

    Integer getVersion();

    /**
     * Do not set version if you are not sure - it must be null for a new entity or loaded from the database
     * for a persistent one.
     */
    void setVersion(Integer version);
}
