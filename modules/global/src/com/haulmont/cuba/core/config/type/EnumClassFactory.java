/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author kozlov
 * @version $Id$
 */
public class EnumClassFactory extends TypeFactory {

    private TypeFactory idFactory;
    private Method fromIdMethod;

    public EnumClassFactory(TypeFactory idFactory, Method fromIdMethod) {
        this.idFactory = idFactory;
        this.fromIdMethod = fromIdMethod;
    }

    @Override
    public Object build(String string) {
        try {
            Object id = idFactory.build(string);
            return fromIdMethod.invoke(null, id);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("TypeFactory build error", e);
        }
    }
}
