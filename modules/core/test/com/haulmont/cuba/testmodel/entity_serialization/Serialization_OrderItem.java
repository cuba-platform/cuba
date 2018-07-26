/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.testmodel.entity_serialization;

import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.*;

@Table(name = "TEST_SERIALIZATION_ORDER_ITEM")
@Entity(name = "test$Serialization_OrderItem")
public class Serialization_OrderItem extends BaseUuidEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    protected Serialization_Order order;

    @Column(name = "NAME")
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RELATED_ORDER_ITEM_ID")
    protected Serialization_OrderItem relatedItem;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Serialization_Order getOrder() {
        return order;
    }

    public void setOrder(Serialization_Order order) {
        this.order = order;
    }

    public Serialization_OrderItem getRelatedItem() {
        return relatedItem;
    }

    public void setRelatedItem(Serialization_OrderItem relatedItem) {
        this.relatedItem = relatedItem;
    }
}
