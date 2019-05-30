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

package com.haulmont.cuba.web.testsupport;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;

import java.util.Map;

/**
 * Convenient factory for creating entity instances for tests. The factory can be obtained via {@link TestContainer}.
 */
public class TestEntityFactory<E extends Entity> {

    private final Metadata metadata;
    private final Class<E> entityClass;
    private TestEntityState entityState;

    public TestEntityFactory(Metadata metadata, Class<E> entityClass, TestEntityState entityState) {
        this.metadata = metadata;
        this.entityClass = entityClass;
        this.entityState = entityState;
    }

    public E create(Map<String, Object> properties) {
        E instance = metadata.create(entityClass);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            instance.setValueEx(entry.getKey(), entry.getValue());
        }
        entityState.setState(instance);
        return instance;
    }

    public E create(String propertyName, Object propertyValue) {
        return create(ParamsMap.of(propertyName, propertyValue));
    }

    public E create(String propertyName1, Object propertyValue1,
                    String propertyName2, Object propertyValue2) {
        return create(ParamsMap.of(propertyName1, propertyValue1, propertyName2, propertyValue2));
    }

    public E create(String propertyName1, Object propertyValue1,
                    String propertyName2, Object propertyValue2,
                    String propertyName3, Object propertyValue3) {
        return create(ParamsMap.of(
                propertyName1, propertyValue1,
                propertyName2, propertyValue2,
                propertyName3, propertyValue3));
    }

    public E create(String propertyName1, Object propertyValue1,
                    String propertyName2, Object propertyValue2,
                    String propertyName3, Object propertyValue3,
                    String propertyName4, Object propertyValue4) {
        return create(ParamsMap.of(
                propertyName1, propertyValue1,
                propertyName2, propertyValue2,
                propertyName3, propertyValue3,
                propertyName4, propertyValue4));
    }

    public E create(String propertyName1, Object propertyValue1,
                    String propertyName2, Object propertyValue2,
                    String propertyName3, Object propertyValue3,
                    String propertyName4, Object propertyValue4,
                    String propertyName5, Object propertyValue5) {
        return create(ParamsMap.of(
                propertyName1, propertyValue1,
                propertyName2, propertyValue2,
                propertyName3, propertyValue3,
                propertyName4, propertyValue4,
                propertyName5, propertyValue5));
    }

    public E create(String propertyName1, Object propertyValue1,
                    String propertyName2, Object propertyValue2,
                    String propertyName3, Object propertyValue3,
                    String propertyName4, Object propertyValue4,
                    String propertyName5, Object propertyValue5,
                    String propertyName6, Object propertyValue6) {
        return create(ParamsMap.of(
                propertyName1, propertyValue1,
                propertyName2, propertyValue2,
                propertyName3, propertyValue3,
                propertyName4, propertyValue4,
                propertyName5, propertyValue5,
                propertyName6, propertyValue6));
    }
}