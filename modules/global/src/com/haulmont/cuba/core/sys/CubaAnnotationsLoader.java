/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.jpa.loader.JPAAnnotationsLoader;

import java.lang.reflect.Field;

/**
* @author krivopustov
* @version $Id$
*/
public class CubaAnnotationsLoader extends JPAAnnotationsLoader {

    public CubaAnnotationsLoader(Session session) {
        super(session);
    }

    @Override
    protected boolean isMetaPropertyField(Field field) {
        String name = field.getName();
        return super.isMetaPropertyField(field)
                && !name.equals("pcVersionInit")
                && !name.equals("pcStateManager")
                && !name.equals("pcDetachedState")
                && !name.equals("__valueListeners");
    }
}