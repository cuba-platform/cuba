/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.serialization;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class EntitySerializationException extends RuntimeException {

    public EntitySerializationException() {
    }

    public EntitySerializationException(String message) {
        super(message);
    }

    public EntitySerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntitySerializationException(Throwable cause) {
        super(cause);
    }

    public EntitySerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
