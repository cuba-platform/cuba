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

package com.haulmont.cuba.testmodel.many2many;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 */
@Entity(name = "test$Many2ManyB")
@Table(name = "TEST_MANY2MANY_B")
public class Many2ManyB extends StandardEntity {

    @ManyToMany(mappedBy = "collectionOfB")
    protected Set<Many2ManyA> collectionOfA;

    @ManyToMany(mappedBy = "collectionOfB2")
    @OnDeleteInverse(DeletePolicy.DENY)
    protected Set<Many2ManyA> collectionOfA2;

    public Set<Many2ManyA> getCollectionOfA() {
        return collectionOfA;
    }

    public void setCollectionOfA(Set<Many2ManyA> collectionOfA) {
        this.collectionOfA = collectionOfA;
    }

    public Set<Many2ManyA> getCollectionOfA2() {
        return collectionOfA2;
    }

    public void setCollectionOfA2(Set<Many2ManyA> collectionOfA2) {
        this.collectionOfA2 = collectionOfA2;
    }
}
