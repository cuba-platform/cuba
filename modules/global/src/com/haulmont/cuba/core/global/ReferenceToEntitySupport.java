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

@Component(ReferenceToEntitySupport.NAME)
public class ReferenceToEntitySupport {

    public static final String NAME = "cuba_ReferenceToEntitySupport";

    @Inject
    protected Metadata metadata;

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

    public String getPrimaryKeyForLoadingEntity(MetaClass metaClass) {
        if (metadata.getTools().hasCompositePrimaryKey(metaClass) && HasUuid.class.isAssignableFrom(metaClass.getJavaClass())) {
            return "uuid";
        } else {
            return metadata.getTools().getPrimaryKeyName(metaClass);
        }
    }
}
