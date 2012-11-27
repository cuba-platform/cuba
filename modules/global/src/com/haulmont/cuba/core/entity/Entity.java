/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.Instance;

import java.io.Serializable;

/**
 * Interface to be implemented by domain model objects with identifiers.
 * @param <T> identifier type
 *
 * @author abramov
 * @version $Id$
 */
public interface Entity<T> extends Instance, Serializable {
    T getId();
}
