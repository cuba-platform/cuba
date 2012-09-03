/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
