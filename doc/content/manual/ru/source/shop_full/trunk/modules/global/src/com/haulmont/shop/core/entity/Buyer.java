/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.shop.core.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.List;

/**
 * @author sukhova
 * @version $Id$
 */
@Entity(name = "shop$Buyer")
@Table(name = "SHOP_BUYER")
@NamePattern("%s|personalData")
public class Buyer extends StandardEntity {
    @Column(name = "DELIVERY_ADDRESS")
    private String deliveryAddress;

    @Embedded
    private PersonalData personalData;

    @OneToMany(mappedBy = "buyer")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    private List<Discount> discounts;

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    /*public String getFullName() {
        return personalData.getFullName();
    }    */

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }
}
