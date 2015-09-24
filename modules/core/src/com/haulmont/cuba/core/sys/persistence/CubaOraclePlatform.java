/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import org.eclipse.persistence.exceptions.ConversionException;
import org.eclipse.persistence.platform.database.OraclePlatform;

import java.util.UUID;

/**
 * @author Konstantin Krivopustov
 * @version $Id$
 */
public class CubaOraclePlatform extends OraclePlatform {

    @Override
    public Object convertObject(Object sourceObject, Class javaClass) throws ConversionException {
        if (sourceObject instanceof UUID && javaClass == String.class) {
            return sourceObject.toString().replace("-", "");
        }
        return super.convertObject(sourceObject, javaClass);
    }
}
