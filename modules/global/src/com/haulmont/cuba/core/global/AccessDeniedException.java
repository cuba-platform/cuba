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

import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;

/**
 * Exception that is raised on attempt to violate a security constraint.
 * <p>
 * You can throw this exception in application code if you want a standard notification about "access denied"
 * to be shown to the user and the event to be logged.
 */
@SupportedByClient
@Logging(Logging.Type.BRIEF)
public class AccessDeniedException extends RuntimeException
{
    private static final long serialVersionUID = -3097861878301424338L;

    private PermissionType type;

    private EntityOp entityOp;

    private String target;

    /**
     * Constructor.
     *
     * @param type      permission type
     * @param target    permission target object, e.g. a screen id or entity operation name. When throwing the exception
     *                  in application code, can be any string suitable to describe the situation in the log.
     */
    public AccessDeniedException(PermissionType type, String target) {
        super(type.toString() + " " + target);
        this.type = type;
        this.target = target;
    }

    /**
     * Constructor.
     *
     * @param type      permission type
     * @param entityOp  type of operation on entity
     * @param target    permission target object, e.g. a screen id or entity operation name. When throwing the exception
     *                  in application code, can be any string suitable to describe the situation in the log.
     */
    public AccessDeniedException(PermissionType type, EntityOp entityOp, String target) {
        super(type.toString() + ":" + entityOp.toString() + " " + target);
        this.type = type;
        this.target = target;
        this.entityOp = entityOp;
    }

    public PermissionType getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public EntityOp getEntityOp() {
        return entityOp;
    }
}
