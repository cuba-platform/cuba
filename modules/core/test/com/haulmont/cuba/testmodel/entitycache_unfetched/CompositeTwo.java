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

package com.haulmont.cuba.testmodel.entitycache_unfetched;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Table(name = "TEST_COMPOSITE_TWO")
@Entity(name = "test$CompositeTwo")
public class CompositeTwo extends StandardEntity {
    private static final long serialVersionUID = 592301436566418507L;

    @Column(name = "NAME")
    protected String name;

    @OneToMany(mappedBy = "compositeTwo")
    protected List<CompositePropertyTwo> compositePropertyTwo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CompositePropertyTwo> getCompositePropertyTwo() {
        return compositePropertyTwo;
    }

    public void setCompositePropertyTwo(List<CompositePropertyTwo> compositePropertyTwo) {
        this.compositePropertyTwo = compositePropertyTwo;
    }
}