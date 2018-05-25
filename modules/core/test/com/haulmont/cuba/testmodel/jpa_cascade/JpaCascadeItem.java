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

package com.haulmont.cuba.testmodel.jpa_cascade;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Entity(name = "test$JpaCascadeItem")
@Table(name = "TEST_JPA_CASCADE_ITEM")
    public class JpaCascadeItem extends StandardEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOO_ID")
    private JpaCascadeFoo foo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JpaCascadeFoo getFoo() {
        return foo;
    }

    public void setFoo(JpaCascadeFoo foo) {
        this.foo = foo;
    }
}
