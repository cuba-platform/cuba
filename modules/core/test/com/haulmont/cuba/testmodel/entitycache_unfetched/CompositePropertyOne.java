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
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "TEST_COMPOSITE_PROPERTY_ONE")
@Entity(name = "test$CompositePropertyOne")
public class CompositePropertyOne extends StandardEntity {
    private static final long serialVersionUID = -1221181517017725152L;

    @Column(name = "NAME")
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPOSITE_ONE_ID")
    protected CompositeOne compositeOne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPOSITE_TWO_ID")
    protected CompositeTwo compositeTwo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompositeOne getCompositeOne() {
        return compositeOne;
    }

    public void setCompositeOne(CompositeOne compositeOne) {
        this.compositeOne = compositeOne;
    }

    public CompositeTwo getCompositeTwo() {
        return compositeTwo;
    }

    public void setCompositeTwo(CompositeTwo compositeTwo) {
        this.compositeTwo = compositeTwo;
    }
}