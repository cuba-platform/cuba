/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

/**
 * <p>$Id$</p>
 *
 * @author pavlov
 */

/**
 * This exception is raised on attempt to load deleted object
 */
public class EntityDeletedException extends RuntimeException {
    public static final String ERR_MESSAGE = "Unable to load entiny because it has been deleted";

    public EntityDeletedException() {
        super(ERR_MESSAGE);
    }
}
