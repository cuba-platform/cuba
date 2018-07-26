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

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.EmbeddableEntity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NamePattern("%s|index")
@MetaClass(name = "test$AddressEmbedded")
@Embeddable
public class AddressEmbedded extends EmbeddableEntity {
    private static final long serialVersionUID = 728993860314123316L;

    @Column(name = "STREET")
    protected String street;

    @Column(name = "COUNTRY")
    protected String country;

    @Column(name = "INDEX_")
    protected Integer index;

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }
}