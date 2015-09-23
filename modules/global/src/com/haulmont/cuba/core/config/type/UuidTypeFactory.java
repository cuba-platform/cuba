/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config.type;

import com.haulmont.cuba.core.global.UuidProvider;

/**
 * @author krivopustov
 * @version $Id$
 */
public class UuidTypeFactory extends TypeFactory {
    @Override
    public Object build(String string) {
        if (string == null) {
            return null;
        }

        return UuidProvider.fromString(string);
    }
}