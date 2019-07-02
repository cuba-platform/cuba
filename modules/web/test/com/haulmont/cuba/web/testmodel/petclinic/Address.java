/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.testmodel.petclinic;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.EmbeddableEntity;

import javax.persistence.*;

@MetaClass(name = "pc_Address")
@Embeddable
public class Address extends EmbeddableEntity {
    private static final long serialVersionUID = 1425424514535608806L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_ID")
    protected Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY_ID")
    protected City city;

    @Column(name = "POSTCODE", length = 20)
    protected String postcode;

    @Column(name = "LINE1")
    protected String line1;

    @Column(name = "LINE2")
    protected String line2;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }
}