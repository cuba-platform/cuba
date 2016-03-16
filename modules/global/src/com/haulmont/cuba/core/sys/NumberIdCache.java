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
import java.util.concurrent.atomic.AtomicLong;

/**
 */
@Component(NumberIdCache.NAME)
public class NumberIdCache {

    public static final String NAME = "cuba_NumberIdCache";

    protected class Generator {
        protected AtomicLong counter;
        protected long sequenceValue;

        protected Generator(String entityName, NumberIdSequence sequence) {
            sequenceValue = sequence.createLongId(entityName);
            counter = new AtomicLong(sequenceValue);
        }

        protected long getNext() {
            long next = counter.incrementAndGet();
            if (next > sequenceValue + cacheSize)
                return -1;
            return next;
        }
    }

    protected ConcurrentMap<String, Generator> cache = new ConcurrentHashMap<>();

    protected int cacheSize;

    @Inject
    protected void setConfig(GlobalConfig config) {
        cacheSize = config.getNumberIdCacheSize();
    }

    public Long createLongId(String entityName, NumberIdSequence sequence) {
        Generator gen = getGenerator(entityName, sequence);
        long next = gen.getNext();
        if (next == -1) {
            cache.remove(entityName);
            gen = getGenerator(entityName, sequence);
            next = gen.getNext();
        }
        return next;
    }

    protected Generator getGenerator(String entityName, NumberIdSequence sequence) {
        Generator gen = cache.get(entityName);
        if (gen == null) {
            gen = new Generator(entityName, sequence);
            Generator existingGen = cache.putIfAbsent(entityName, gen);
            if (existingGen != null) {
                gen = existingGen;
            }
        }
        return gen;
    }
}
