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
import java.util.List;

@Entity(name = "test$ParentCachedEntity")
@Table(name = "TEST_PARENT_CACHED_ENTITY")
public class ParentCachedEntity extends StandardEntity {
    @Column(name = "TITLE")
    private String title;

    @Column(name = "TEST_ADDITIONAL")
    private String testAdditional;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<ChildCachedEntity> children;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ChildCachedEntity> getChildren() {
        return children;
    }

    public void setChildren(List<ChildCachedEntity> children) {
        this.children = children;
    }

    public String getTestAdditional() {
        return testAdditional;
    }

    public void setTestAdditional(String testAdditional) {
        this.testAdditional = testAdditional;
    }
}
