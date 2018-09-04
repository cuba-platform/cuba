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
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "test$Product")
@Table(name = "TEST_PRODUCT")
@NamePattern("%s|name")
public class Product extends StandardEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private BigDecimal price;

    @ManyToMany
    @JoinTable(name = "PRODUCT_TAG_LINK",
            joinColumns = @JoinColumn(name = "PRODUCT_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID"))
    private List<ProductTag> tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<ProductTag> getTags() {
        return tags;
    }

    public void setTags(List<ProductTag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", price=" + price +
                "}@" + Integer.toHexString(System.identityHashCode(this));
    }
}
