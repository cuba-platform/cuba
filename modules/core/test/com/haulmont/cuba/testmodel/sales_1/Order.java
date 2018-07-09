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

package com.haulmont.cuba.testmodel.sales_1;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity(name = "sales1$Order")
@Table(name = "SALES1_ORDER")
@NamePattern("No %s for %s|number,customer")
@PublishEntityChangedEvents
@Listeners("test_EntityChangedEventListener")
public class Order extends StandardEntity {

    @Column(name = "NUM")
    private String number;

    @Column(name = "DATE_")
    private Date date;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "order")
    protected List<OrderLine> orderLines;

    @PrePersist
    public void prePersist() {
        TestEntityChangedEventListener listener = AppBeans.get(TestEntityChangedEventListener.class);
        listener.allEvents.add(new TestEntityChangedEventListener.EventInfo("JPA PrePersist", this));
    }

    @PostPersist
    public void postPersist() {
        TestEntityChangedEventListener listener = AppBeans.get(TestEntityChangedEventListener.class);
        listener.allEvents.add(new TestEntityChangedEventListener.EventInfo("JPA PostPersist", this));
    }

    @PreUpdate
    public void preUpdate() {
        TestEntityChangedEventListener listener = AppBeans.get(TestEntityChangedEventListener.class);
        listener.allEvents.add(new TestEntityChangedEventListener.EventInfo("JPA PreUpdate", this));
    }

    @PostUpdate
    public void postUpdate() {
        TestEntityChangedEventListener listener = AppBeans.get(TestEntityChangedEventListener.class);
        listener.allEvents.add(new TestEntityChangedEventListener.EventInfo("JPA PostUpdate", this));
    }

    @PreRemove
    public void preRemove() {
        TestEntityChangedEventListener listener = AppBeans.get(TestEntityChangedEventListener.class);
        listener.allEvents.add(new TestEntityChangedEventListener.EventInfo("JPA PreRemove", this));
    }

    @PostRemove
    public void postRemove() {
        TestEntityChangedEventListener listener = AppBeans.get(TestEntityChangedEventListener.class);
        listener.allEvents.add(new TestEntityChangedEventListener.EventInfo("JPA PostRemove", this));
    }

    @PostLoad
    public void postLoad() {
        TestEntityChangedEventListener listener = AppBeans.get(TestEntityChangedEventListener.class);
        listener.allEvents.add(new TestEntityChangedEventListener.EventInfo("JPA PostLoad", this));
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}
