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

import com.haulmont.cuba.core.global.GlobalConfig;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Intermediate cache for generated ids of entities with long/integer PK.
 * The cache size is determined by the {@code cuba.numberIdCacheSize} app property.
 */
@Component(NumberIdCache.NAME)
public class NumberIdCache {

    public static final String NAME = "cuba_NumberIdCache";

    protected class Generator {
        protected long counter;
        protected long sequenceValue;
        protected String entityName;
        protected NumberIdSequence sequence;

        public Generator(String entityName, NumberIdSequence sequence) {
            this.entityName = entityName;
            this.sequence = sequence;
            createCounter();
        }

        protected void createCounter() {
            sequenceValue = sequence.createLongId(entityName);
            counter = sequenceValue;
        }

        public synchronized long getNext() {
            long next = ++counter;
            if (next > sequenceValue + config.getNumberIdCacheSize()) {
                createCounter();
                next = ++counter;
            }
            return next;
        }
    }

    protected ConcurrentMap<String, Generator> cache = new ConcurrentHashMap<>();

    @Inject
    protected GlobalConfig config;

    /**
     * Generates next id.
     *
     * @param entityName    entity name
     * @param sequence      sequence provider
     * @return  next id
     */
    public Long createLongId(String entityName, NumberIdSequence sequence) {
        Generator gen = cache.computeIfAbsent(entityName, s -> new Generator(entityName, sequence));
        return gen.getNext();
    }

    /**
     * INTERNAL. Used by tests.
     */
    public void reset() {
        cache.clear();
    }
}