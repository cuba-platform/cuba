/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 17.03.2011 16:52:32
 *
 * $Id$
 */
package com.haulmont.cuba.gui.security;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;

public abstract class SecurityHelper {
    public static boolean isEditPermitted(MetaProperty metaProperty) {
        MetaClass metaClass = metaProperty.getDomain();

        UserSession userSession = UserSessionClient.getUserSession();
        return (userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                                || userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE))
                && userSession.isEntityAttrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);

    }
}
