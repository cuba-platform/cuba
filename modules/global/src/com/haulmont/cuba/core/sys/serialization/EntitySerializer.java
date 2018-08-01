/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.core.sys.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class EntitySerializer<T extends Entity> extends KryoSerialization.CubaFieldSerializer<T> {
    protected String primaryKey;

    private static final Logger log = LoggerFactory.getLogger(EntitySerializer.class);

    public EntitySerializer(Kryo kryo, Class type) {
        super(kryo, type);
    }

    public EntitySerializer(Kryo kryo, Class type, Class[] generics) {
        super(kryo, type, generics);
    }

    @Override
    protected void rebuildCachedFields() {
        extractPrimaryKeyName(getType());
        super.rebuildCachedFields();
    }

    @Override
    public int compare(CachedField o1, CachedField o2) {
        if (primaryKey != null) {
            String name1 = getCachedFieldName(o1);
            String name2 = getCachedFieldName(o2);
            if (Objects.equals(primaryKey, name1)) {
                return -1;
            }
            if (Objects.equals(primaryKey, name2)) {
                return 1;
            }
        }
        return super.compare(o1, o2);
    }

    protected void extractPrimaryKeyName(Class type) {
        Metadata metadata = AppBeans.get(Metadata.class);
        MetaClass metaClass = metadata.getClass(type);
        if (metaClass != null) {
            primaryKey = metadata.getTools().getPrimaryKeyName(metaClass);
            if (primaryKey == null) {
                log.debug("Unable to resolve primary key for type {}", type.getSimpleName());
            }
        } else {
            log.debug("Unable to resolve metaClass for type {}", type.getSimpleName());
        }
    }
}
