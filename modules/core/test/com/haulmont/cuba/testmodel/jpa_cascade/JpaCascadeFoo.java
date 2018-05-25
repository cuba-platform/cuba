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
import java.util.List;

@Entity(name = "test$JpaCascadeFoo")
@Table(name = "TEST_JPA_CASCADE_FOO")
    public class JpaCascadeFoo extends StandardEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "BAR_ID")
    private JpaCascadeBar bar;

    @OneToMany(mappedBy = "foo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaCascadeItem> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JpaCascadeBar getBar() {
        return bar;
    }

    public void setBar(JpaCascadeBar bar) {
        this.bar = bar;
    }

    public List<JpaCascadeItem> getItems() {
        return items;
    }

    public void setItems(List<JpaCascadeItem> items) {
        this.items = items;
    }
}
