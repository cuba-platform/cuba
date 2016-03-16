/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.security.entity.User;

import java.util.Date;

/**
 * Describes a pessimistic lock of an object.
 *
 */
@MetaClass(name = "sys$LockInfo")
@SystemLevel
public class LockInfo extends AbstractNotPersistentEntity {

    private static final long serialVersionUID = -1991047219638006414L;

    private final String entityName;
    private final String entityId;
    private final Date since;
    private final User user;

    public LockInfo(User user, String entityName, String entityId) {
        this.entityName = entityName;
        this.entityId = entityId;
        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        this.since = timeSource.currentTimestamp();
        this.user = user;
    }

    /**
     * @return locked object id
     */
    @MetaProperty
    public String getEntityId() {
        return entityId;
    }

    /**
     * @return locked object type
     */
    @MetaProperty
    public String getEntityName() {
        return entityName;
    }

    /**
     * @return when the lock occured
     */
    @MetaProperty
    public Date getSince() {
        return since;
    }

    /**
     * @return a user which holds the lock
     */
    @MetaProperty
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return entityName + "/" + entityId + ", user=" + user + ", since=" + since;
    }
}
