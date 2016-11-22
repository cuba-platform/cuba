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
 */

package com.haulmont.cuba.testmodel.softdelete_one_to_one;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Entity(name = "test$SoftDeleteOneToOneA")
@Table(name = "TEST_SOFT_DELETE_OTO_A")
@NamePattern("%s|name")
public class SoftDeleteOneToOneA extends StandardEntity {
    private static final long serialVersionUID = 3582114532586946446L;

    @Column(name = "NAME")
    protected String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "B_ID")
    protected SoftDeleteOneToOneB b;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SoftDeleteOneToOneB getB() {
        return b;
    }

    public void setB(SoftDeleteOneToOneB b) {
        this.b = b;
    }
}
