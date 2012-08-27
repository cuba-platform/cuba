/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.chile.core.common;

/**
 * Interface to track changes in data model objects.
 *
 * @author abramov
 * @version $Id$
 */
public interface ValueListener {

    /**
     * Called by a data model object when an attribute changes.
     *
     * @param item          data model object instance
     * @param property      changed attribute name
     * @param prevValue     previous value
     * @param value         current value
     */
	void propertyChanged(Object item, String property, Object prevValue, Object value);
}
