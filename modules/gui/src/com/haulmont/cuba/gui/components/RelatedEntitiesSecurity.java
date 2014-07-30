/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;

/**
 * @author artamonov
 * @version $Id$
 */
public final class RelatedEntitiesSecurity {

    private RelatedEntitiesSecurity() {
    }

    public static boolean isSuitableProperty(MetaProperty metaProperty, MetaClass effectiveMetaClass) {
        if (metaProperty.getRange().isClass()
                && !Category.class.isAssignableFrom(metaProperty.getJavaType())) {

            Security security = AppBeans.get(Security.NAME);
            // check security
            if (security.isEntityAttrPermitted(effectiveMetaClass, metaProperty.getName(), EntityAttrAccess.VIEW)
                    && security.isEntityOpPermitted(metaProperty.getRange().asClass(), EntityOp.READ)) {
                return true;
            }
        }
        return false;
    }
}