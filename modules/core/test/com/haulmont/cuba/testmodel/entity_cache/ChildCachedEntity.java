/*
 * Copyright (c) 2008-2021 Haulmont.
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

package com.haulmont.cuba.testmodel.entity_cache;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;


@Entity(name = "test$ChildCachedEntity")
@Table(name = "TEST_CHILD_CACHED_ENTITY")
public class ChildCachedEntity extends StandardEntity {
    @Column(name = "SIMPLE_PROPERTY")
    private String simpleProperty;

    @Column(name = "TEST_ADDITIONAL")
    private String testAdditional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private ParentCachedEntity parent;

    public String getSimpleProperty() {
        return simpleProperty;
    }

    public void setSimpleProperty(String simpleProperty) {
        this.simpleProperty = simpleProperty;
    }

    public String getTestAdditional() {
        return testAdditional;
    }

    public void setTestAdditional(String testAdditional) {
        this.testAdditional = testAdditional;
    }

    public ParentCachedEntity getParent() {
        return parent;
    }

    public void setParent(ParentCachedEntity parent) {
        this.parent = parent;
    }

}
