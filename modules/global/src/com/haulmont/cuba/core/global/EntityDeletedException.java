/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

/**
 * This exception is raised on attempt to load a deleted object.
 *
 * <p>$Id$</p>
 *
 * @author pavlov
 */
public class EntityDeletedException extends RuntimeException {
    public static final String ERR_MESSAGE = "Unable to load entiny because it has been deleted";

    public EntityDeletedException() {
        super(ERR_MESSAGE);
    }
}
