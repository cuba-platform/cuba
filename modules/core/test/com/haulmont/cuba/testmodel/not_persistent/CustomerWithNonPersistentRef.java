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

package com.haulmont.cuba.testmodel.not_persistent;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.testmodel.primary_keys.EntityKey;
import com.haulmont.cuba.testmodel.sales.Customer;

import javax.persistence.*;

@Entity(name = "test$CustomerWithNonPersistentRef")
@Table(name = "TEST_CUSTOMER_W_NPERS_REF")
@NamePattern("%s|name")
public class CustomerWithNonPersistentRef extends BaseEntityWithNonPersistentProperty {

    @Column(name = "NAME")
    private String name;

    @Embedded
    private EntityKey entityKey;

    @MetaProperty
    @Transient
    private TestNotPersistentEntity notPersistentEntity;

    @MetaProperty
    @Transient
    private Customer customer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityKey getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(EntityKey entityKey) {
        this.entityKey = entityKey;
    }

    public TestNotPersistentEntity getNotPersistentEntity() {
        return notPersistentEntity;
    }

    public void setNotPersistentEntity(TestNotPersistentEntity notPersistentEntity) {
        this.notPersistentEntity = notPersistentEntity;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
