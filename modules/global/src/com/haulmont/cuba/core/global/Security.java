/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Security {

    String NAME = "cuba_Security";

    boolean isScreenPermitted(ClientType clientType, String windowAlias);

    boolean isEntityOpPermitted(MetaClass metaClass, EntityOp entityOp);

    boolean isEntityAttrPermitted(MetaClass metaClass, String property, EntityAttrAccess access);

    boolean isEntityAttrModificationPermitted(MetaProperty metaProperty);

    boolean isSpecificPermitted(String name);
}
