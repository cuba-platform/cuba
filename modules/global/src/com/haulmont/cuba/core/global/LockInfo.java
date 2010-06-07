/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.02.2010 11:15:43
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.security.entity.User;

import java.util.Date;

@MetaClass(name = "core$LockInfo")
public class LockInfo extends AbstractNotPersistentEntity {

    private static final long serialVersionUID = -1991047219638006414L;

    private final String entityName;
    private final String entityId;
    private final Date since;
    private final User user;

    public LockInfo(User user, String entityName, String entityId) {
        this.entityName = entityName;
        this.entityId = entityId;
        this.since = TimeProvider.currentTimestamp();
        this.user = user;
    }

    @MetaProperty
    public String getEntityId() {
        return entityId;
    }

    @MetaProperty
    public String getEntityName() {
        return entityName;
    }

    @MetaProperty
    public Date getSince() {
        return since;
    }

    @MetaProperty
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return entityName + "/" + entityId + ", user=" + user + ", since=" + since;
    }
}
