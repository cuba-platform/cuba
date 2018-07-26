/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.testmodel.entity_serialization;

import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Table(name = "TEST_SERIALIZATION_ORDER")
@Entity(name = "test$Serialization_Order")
public class Serialization_Order extends BaseUuidEntity {

    @Column(name = "NUMBER")
    protected String number;

    @OneToMany(mappedBy = "order")
    protected List<Serialization_OrderItem> items;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Serialization_OrderItem> getItems() {
        return items;
    }

    public void setItems(List<Serialization_OrderItem> items) {
        this.items = items;
    }
}
