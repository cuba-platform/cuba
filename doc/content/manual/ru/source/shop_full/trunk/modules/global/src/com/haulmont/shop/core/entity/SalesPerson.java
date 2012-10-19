/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.shop.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import java.util.Date;

/**
 * @author sukhova
 * @version $Id$
 */
@Entity(name = "shop$SalesPerson")
@Table(name = "SHOP_SALES_PERSON")
@NamePattern("%s|personalData")
public class SalesPerson extends StandardEntity {

    @Temporal(value= TemporalType.DATE)
    @Column(name = "WORKS_FROM")
    private Date worksFrom;

    @Column(name = "ADDRESS")
    private String address;

    @Embedded
    private PersonalData personalData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getWorksFrom() {
        return worksFrom;
    }

    public void setWorksFrom(Date worksFrom) {
        this.worksFrom = worksFrom;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }
}
