/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.security.role;

import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.annotation.Nullable;

public class EntityPermissionsContainer extends PermissionsContainer {
    private static final long serialVersionUID = 1810208523892912417L;

    private Access defaultEntityCreateAccess;
    private Access defaultEntityReadAccess;
    private Access defaultEntityUpdateAccess;
    private Access defaultEntityDeleteAccess;

    public Access getDefaultEntityCreateAccess() {
        return defaultEntityCreateAccess;
    }

    public void setDefaultEntityCreateAccess(@Nullable Access defaultEntityCreateAccess) {
        this.defaultEntityCreateAccess = defaultEntityCreateAccess;
    }

    public Access getDefaultEntityReadAccess() {
        return defaultEntityReadAccess;
    }

    public void setDefaultEntityReadAccess(@Nullable Access defaultEntityReadAccess) {
        this.defaultEntityReadAccess = defaultEntityReadAccess;
    }

    public Access getDefaultEntityUpdateAccess() {
        return defaultEntityUpdateAccess;
    }

    public void setDefaultEntityUpdateAccess(@Nullable Access defaultEntityUpdateAccess) {
        this.defaultEntityUpdateAccess = defaultEntityUpdateAccess;
    }

    public Access getDefaultEntityDeleteAccess() {
        return defaultEntityDeleteAccess;
    }

    public void setDefaultEntityDeleteAccess(@Nullable Access defaultEntityDeleteAccess) {
        this.defaultEntityDeleteAccess = defaultEntityDeleteAccess;
    }

    public Access getDefaultAccessByTarget(String target) {
        if (target.endsWith(":" + EntityOp.CREATE.getId())) {
            return getDefaultEntityCreateAccess();
        } else if (target.endsWith(":" + EntityOp.READ.getId())) {
            return getDefaultEntityReadAccess();
        } else if (target.endsWith(":" + EntityOp.UPDATE.getId())) {
            return getDefaultEntityUpdateAccess();
        } else if (target.endsWith(":" + EntityOp.DELETE.getId())) {
            return getDefaultEntityDeleteAccess();
        } else {
            throw new IllegalArgumentException("Unable to evaluate entity operation from target " + target);
        }
    }
}
