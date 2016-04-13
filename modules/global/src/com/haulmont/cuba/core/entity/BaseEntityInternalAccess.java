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
 */

package com.haulmont.cuba.core.entity;

/**
 * For internal use only.
 */
public final class BaseEntityInternalAccess {

    private BaseEntityInternalAccess() {
    }

    /**
     * INTERNAL.
     */
    public static boolean isNew(BaseGenericIdEntity entity) {
        return entity.__new;
    }

    /**
     * INTERNAL.
     */
    public static void setNew(BaseGenericIdEntity entity, boolean cubaNew) {
        entity.__new = cubaNew;
    }

    /**
     * INTERNAL.
     */
    public static boolean isManaged(BaseGenericIdEntity entity) {
        return entity.__managed;
    }

    /**
     * INTERNAL.
     */
    public static void setManaged(BaseGenericIdEntity entity, boolean cubaManaged) {
        entity.__managed = cubaManaged;
    }

    /**
     * INTERNAL.
     */
    public static boolean isDetached(BaseGenericIdEntity entity) {
        return entity.__detached;
    }

    /**
     * INTERNAL.
     */
    public static void setDetached(BaseGenericIdEntity entity, boolean detached) {
        entity.__detached = detached;
    }

    /**
     * INTERNAL.
     */
    public static boolean isRemoved(BaseGenericIdEntity entity) {
        return entity.__removed;
    }

    /**
     * INTERNAL.
     */
    public static void setRemoved(BaseGenericIdEntity entity, boolean removed) {
        entity.__removed = removed;
    }

    /**
     * INTERNAL
     */
    public static String[] getInaccessibleAttributes(BaseGenericIdEntity entity) {
        return entity.__inaccessibleAttributes;
    }

    /**
     * INTERNAL
     */
    public static void setInaccessibleAttributes(BaseGenericIdEntity entity, String[] __inaccessibleAttributes) {
        entity.__inaccessibleAttributes = __inaccessibleAttributes;
    }
}