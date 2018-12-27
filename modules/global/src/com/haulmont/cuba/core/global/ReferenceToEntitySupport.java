/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.HasUuid;
import com.haulmont.cuba.core.entity.IdProxy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Utility class to provide common functionality for entities with different type of primary keys
 */
@Component(ReferenceToEntitySupport.NAME)
public class ReferenceToEntitySupport {

    public static final String NAME = "cuba_ReferenceToEntitySupport";

    @Inject
    protected Metadata metadata;

    /**
     * @param entity entity
     * @return entity id to store in database
     */
    public Object getReferenceId(Entity entity) {
        if (entity instanceof HasUuid) {
            return ((HasUuid) entity).getUuid();
        }
        Object entityId = entity.getId();
        if (entityId instanceof IdProxy) {
            return ((IdProxy) entityId).get();
        }
        return entity.getId();
    }

    /**
     * @param entity entity
     * @return entity id for links
     */
    public Object getReferenceIdForLink(Entity entity) {
        Object entityId = entity.getId();
        if (entityId instanceof IdProxy) {
            entityId = ((IdProxy) entityId).get();
        }
        if (entityId == null)
            return null;
        if (!(entityId instanceof UUID || entityId instanceof String || entityId instanceof Integer || entityId instanceof Long)) {
            if (entity instanceof HasUuid)
                return ((HasUuid) entity).getUuid();
            else
                throw new IllegalArgumentException(
                        String.format("Unsupported primary key type: %s", entityId.getClass().getSimpleName()));
        }
        return entityId;
    }

    /**
     * @param metaClass of entity
     * @return metaProperty name for storing corresponding primary key in the database
     */
    public String getReferenceIdPropertyName(MetaClass metaClass) {
        if (HasUuid.class.isAssignableFrom(metaClass.getJavaClass())) {
            return "entityId";
        }
        MetaProperty primaryKey = metadata.getTools().getPrimaryKeyProperty(metaClass);

        if (primaryKey != null) {
            Class type = primaryKey.getJavaType();
            if (UUID.class.equals(type)) {
                return "entityId";
            } else if (Long.class.equals(type) || IdProxy.class.equals(type)) {
                return "longEntityId";
            } else if (Integer.class.equals(type)) {
                return "intEntityId";
            } else if (String.class.equals(type)) {
                return "stringEntityId";
            } else {
                throw new IllegalStateException(
                        String.format("Unsupported primary key type: %s for %s", type.getSimpleName(), metaClass.getName()));
            }
        } else {
            throw new IllegalStateException(
                    String.format("Primary key not found for %s", metaClass.getName()));
        }
    }

    /**
     * @param metaClass of entity
     * @return metaProperty name for loading entity from database by primary key stored in the database
     */
    public String getPrimaryKeyForLoadingEntity(MetaClass metaClass) {
        if (HasUuid.class.isAssignableFrom(metaClass.getJavaClass())) {
            MetaProperty primaryKeyProperty = metadata.getTools().getPrimaryKeyProperty(metaClass);
            if (primaryKeyProperty != null && !UUID.class.isAssignableFrom(primaryKeyProperty.getJavaType()))
                return "uuid";
        }
        return metadata.getTools().getPrimaryKeyName(metaClass);
    }

    /**
     * @param metaClass of entity
     * @return metaProperty name for loading entity from database by primary key for links
     */
    public String getPrimaryKeyForLoadingEntityFromLink(MetaClass metaClass) {
        MetaProperty primaryKey = metadata.getTools().getPrimaryKeyProperty(metaClass);
        if (primaryKey != null) {
            Class type = primaryKey.getJavaType();
            if (UUID.class.equals(type) || Long.class.equals(type) || IdProxy.class.equals(type)
                    || Integer.class.equals(type) || String.class.equals(type))
                return metadata.getTools().getPrimaryKeyName(metaClass);
        }
        if (HasUuid.class.isAssignableFrom(metaClass.getJavaClass())) {
            MetaProperty primaryKeyProperty = metadata.getTools().getPrimaryKeyProperty(metaClass);
            if (primaryKeyProperty != null && !UUID.class.isAssignableFrom(primaryKeyProperty.getJavaType()))
                return "uuid";
        }
        throw new IllegalStateException(
                String.format("Unsupported primary key type for %s", metaClass.getName()));
    }
}
