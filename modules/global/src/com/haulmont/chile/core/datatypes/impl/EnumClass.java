/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.chile.core.datatypes.impl;

/**
 * Interface to be implemented by enums that serve as entity attribute types.
 * @param <T> type of value stored in the database
 *
 * @author abramov
 * @version $Id$
 */
public interface EnumClass<T> {
    T getId();
}
