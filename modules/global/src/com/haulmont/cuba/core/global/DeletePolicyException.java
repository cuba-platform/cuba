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

/**
 * Exception that is raised on attempt to soft delete an object,
 * which has linked objects marked with {@link com.haulmont.cuba.core.entity.annotation.OnDelete} annotation
 * with {@link com.haulmont.cuba.core.global.DeletePolicy#DENY} value.
 *
 */
public class DeletePolicyException extends RuntimeException {

    private static final long serialVersionUID = -1359432367630173077L;

    private String entity;
    private String refEntity;

    public static final String ERR_MESSAGE = "Unable to delete %s because there are references from %s";

    public DeletePolicyException(String entity, String refEntity) {
        super(String.format(ERR_MESSAGE, entity, refEntity));
        this.entity = entity;
        this.refEntity = refEntity;
    }

    public String getEntity() {
        return entity;
    }

    public String getRefEntity() {
        return refEntity;
    }
}
