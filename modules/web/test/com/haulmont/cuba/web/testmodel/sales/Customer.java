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

package com.haulmont.cuba.web.testmodel.sales;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.datatypes.impl.EnumUtils;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;

import javax.persistence.*;

@Entity(name = "test$Customer")
@Table(name = "TEST_CUSTOMER")
@NamePattern("%s|name")
public class Customer extends StandardEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private String status;

    @Embedded
    @EmbeddedParameters(nullAllowed = false)
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "ADDRESS_CITY")),
            @AttributeOverride(name = "zip", column = @Column(name = "ADDRESS_ZIP"))
    })
    protected Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return EnumUtils.fromId(Status.class, status, null);
    }

    public void setStatus(Status status) {
        this.status = status.getId();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                "}@" + Integer.toHexString(System.identityHashCode(this));
    }
}
