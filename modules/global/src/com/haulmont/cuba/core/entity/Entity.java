/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.Instance;

/**
 * Interface to be implemented by domain model objects with identifiers.
 * @param <T> identifier type
 *
 * @author abramov
 * @version $Id$
 */
public interface Entity<T> extends Instance {
    T getId();
}
