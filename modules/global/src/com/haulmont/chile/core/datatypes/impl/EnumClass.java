/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
