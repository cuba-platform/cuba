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

package com.haulmont.cuba.testmodel.many2many_fetchsameentity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.List;

@NamePattern("%s|name")
@Table(name = "TEST_MANY2_MANY_FETCH_SAME1")
@Entity(name = "test$Many2Many_FetchSame1")
public class Many2Many_FetchSame1 extends StandardEntity {
    private static final long serialVersionUID = 9128262506497031204L;

    @Column(name = "NAME")
    protected String name;

    @JoinTable(name = "TEST_MANY2_MANY_FETCH_SAME1_MANY2_MANY_FETCH_SAME2_LINK",
        joinColumns = @JoinColumn(name = "MANY2_MANY__FETCH_SAME1_ID"),
        inverseJoinColumns = @JoinColumn(name = "MANY2_MANY__FETCH_SAME2_ID"))
    @ManyToMany
    protected List<Many2Many_FetchSame2> many2;

    public void setMany2(List<Many2Many_FetchSame2> many2) {
        this.many2 = many2;
    }

    public List<Many2Many_FetchSame2> getMany2() {
        return many2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}