/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

/**
 * @author hasanov
 * @version $Id$
 */
@SupportedByClient
public class DevelopmentException extends RuntimeException {

    public DevelopmentException(String message){
        super(message);
    }

}
