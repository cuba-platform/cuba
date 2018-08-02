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

package com.haulmont.cuba.testmodel.embedded;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;

import javax.persistence.*;

@NamePattern("%s|name")
@Table(name = "TEST_ADDRESS_EMBEDDED_CONTAINER")
@Entity(name = "test$AddressEmbeddedContainer")
public class AddressEmbeddedContainer extends StandardEntity {
    private static final long serialVersionUID = -4916441077527921725L;

    @Column(name = "NAME")
    protected String name;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "ADDRESS_STREET")),
        @AttributeOverride(name = "country", column = @Column(name = "ADDRESS_COUNTRY")),
        @AttributeOverride(name = "index", column = @Column(name = "ADDRESS_INDEX_"))
    })
    protected AddressEmbedded address;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddress(AddressEmbedded address) {
        this.address = address;
    }

    public AddressEmbedded getAddress() {
        return address;
    }


}