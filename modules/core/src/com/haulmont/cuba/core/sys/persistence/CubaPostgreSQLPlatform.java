/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.global.UuidProvider;
import org.eclipse.persistence.exceptions.ConversionException;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;

import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaPostgreSQLPlatform extends PostgreSQLPlatform {

    @Override
    public Object convertObject(Object sourceObject, Class javaClass) throws ConversionException {
        // Used when a UUID is passed inside a JPQL string (not as a parameter)
        if (javaClass == UUID.class && sourceObject instanceof String) {
            return UuidProvider.fromString((String) sourceObject);
        }
        return super.convertObject(sourceObject, javaClass);
    }
}
