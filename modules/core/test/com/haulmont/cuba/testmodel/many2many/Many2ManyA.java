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

import javax.persistence.*;
import java.util.Set;

/**
 */
@Entity(name = "test$Many2ManyA")
@Table(name = "TEST_MANY2MANY_A")
public class Many2ManyA extends StandardEntity {

    @ManyToMany
    @JoinTable(name = "TEST_MANY2MANY_AB_LINK",
        joinColumns = @JoinColumn(name = "A_ID"),
        inverseJoinColumns = @JoinColumn(name = "B_ID"))
    @OnDeleteInverse(DeletePolicy.UNLINK)
    protected Set<Many2ManyB> collectionOfB;

    @ManyToMany
    @JoinTable(name = "TEST_MANY2MANY_AB_LINK2",
            joinColumns = @JoinColumn(name = "A_ID"),
            inverseJoinColumns = @JoinColumn(name = "B_ID"))
    protected Set<Many2ManyB> collectionOfB2;


    public Set<Many2ManyB> getCollectionOfB() {
        return collectionOfB;
    }

    public void setCollectionOfB(Set<Many2ManyB> collectionOfB) {
        this.collectionOfB = collectionOfB;
    }

    public Set<Many2ManyB> getCollectionOfB2() {
        return collectionOfB2;
    }

    public void setCollectionOfB2(Set<Many2ManyB> collectionOfB2) {
        this.collectionOfB2 = collectionOfB2;
    }
}
