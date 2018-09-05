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

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.annotation.IdSequence;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Metadata;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Intermediate cache for generated ids of entities with long/integer PK.
 * The cache size is determined by the {@code cuba.numberIdCacheSize} app property.
 */
@Component(NumberIdCache.NAME)
public class NumberIdCache {

    public static final String NAME = "cuba_NumberIdCache";

    @Inject
    protected Metadata metadata;

    protected class Generator {
        protected long counter;
        protected long sequenceValue;
        protected String entityName;
        protected String sequenceName;
        protected boolean cached;
        protected NumberIdSequence numberIdSequence;

        public Generator(String entityName,
                         String sequenceName,
                         boolean cached,
                         NumberIdSequence sequence) {
            this.entityName = entityName;
            this.sequenceName = sequenceName;
            this.cached = cached;
            this.numberIdSequence = sequence;
            if (useIdCache()) {
                createCachedCounter();
            }
        }

        protected boolean useIdCache() {
            return config.getNumberIdCacheSize() != 0 && cached;
        }

        protected void createCachedCounter() {
            sequenceValue = numberIdSequence.createCachedLongId(entityName, sequenceName);
            counter = sequenceValue;
        }

        public synchronized long getNext() {
            if (!useIdCache()) {
                return numberIdSequence.createLongId(entityName, sequenceName);
            } else {
                long next = ++counter;
                if (next > sequenceValue + config.getNumberIdCacheSize()) {
                    createCachedCounter();
                    next = ++counter;
                }
                return next;
            }
        }
    }

    protected ConcurrentMap<String, Generator> cache = new ConcurrentHashMap<>();

    @Inject
    protected GlobalConfig config;

    /**
     * Generates next id.
     *
     * @param entityName entity name
     * @param sequence   sequence provider
     * @return next id
     */
    public Long createLongId(String entityName, NumberIdSequence sequence) {
        MetaClass metaClass = metadata.getClass(entityName);
        final boolean cached;
        final String sequenceName;
        if (metaClass != null) {
            Map attributes = (Map) metaClass.getAnnotations().get(IdSequence.class.getName());
            if (attributes != null) {
                sequenceName = (String) attributes.get("name");
                cached = Boolean.TRUE.equals(attributes.get("cached"));
            } else {
                cached = true;
                sequenceName = null;
            }
        } else {
            cached = true;
            sequenceName = null;
        }

        Generator gen = cache.computeIfAbsent(getCacheKey(entityName, sequenceName), s -> new Generator(entityName, sequenceName, cached, sequence));
        return gen.getNext();
    }

    /**
     * INTERNAL. Used by tests.
     */
    public void reset() {
        cache.clear();
    }

    protected String getCacheKey(String entityName, String sequenceName) {
        return sequenceName == null ? entityName : sequenceName;
    }
}