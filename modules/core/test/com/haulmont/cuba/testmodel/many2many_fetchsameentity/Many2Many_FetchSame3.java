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
@Table(name = "TEST_MANY2_MANY_FETCH_SAME3")
@Entity(name = "test$Many2Many_FetchSame3")
public class Many2Many_FetchSame3 extends StandardEntity {
    private static final long serialVersionUID = 9128262506497031204L;

    @Column(name = "NAME")
    protected String name;

    @OneToMany(mappedBy = "many3")
    protected List<Many2Many_FetchSame2> oneToMany2;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Many2Many_FetchSame2> getOneToMany2() {
        return oneToMany2;
    }

    public void setOneToMany2(List<Many2Many_FetchSame2> oneToMany2) {
        this.oneToMany2 = oneToMany2;
    }
}