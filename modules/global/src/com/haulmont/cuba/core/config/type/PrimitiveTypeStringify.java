/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config.type;

/**
 * @author krivopustov
 * @version $Id$
 */
public class PrimitiveTypeStringify extends TypeStringify {
    @Override
    public String stringify(Object value) {
        return String.valueOf(value);
    }
}