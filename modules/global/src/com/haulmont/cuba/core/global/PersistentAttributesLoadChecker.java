/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

/**
 * @author degtyarjov
 * @version $Id$
 */
public interface PersistentAttributesLoadChecker {
    String NAME = "cuba_PersistentAttributesLoadChecker";

    boolean isLoaded(Object entity, String property);
}
