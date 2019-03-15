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

package com.haulmont.cuba.web.testmodel.sales;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TEST_ORDER_LINE_PARAM")
@Entity(name = "test$OrderLineParam")
public class OrderLineParam extends StandardEntity {
    private static final long serialVersionUID = 5682981871475199801L;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "VALUE_")
    protected String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_LINE_ID")
    protected OrderLine orderLine;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public OrderLine getOrderLine() {
        return orderLine;
    }

    public void setOrderLine(OrderLine orderLine) {
        this.orderLine = orderLine;
    }
}