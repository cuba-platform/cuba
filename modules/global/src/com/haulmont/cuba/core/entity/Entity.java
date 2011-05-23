/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 12.02.2009 13:10:51
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.Instance;

import java.io.Serializable;

/**
 * This interface must be implemented by all domain model objects
 * @param <T> identifier type
 */
public interface Entity<T> extends Instance, Serializable {
    T getId();
}
