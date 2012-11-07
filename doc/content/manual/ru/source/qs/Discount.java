package com.haulmont.shop.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "shop$Discount")
@Table(name = "SHOP_DISCOUNT")
public class Discount extends StandardEntity {

    @Column(name = "MIN_QUANTITY")
    private Integer minQuantity;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Temporal(TemporalType.DATE)
    @Column(name = "FROM_DATE")
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "TILL_DATE")
    private Date tillDate;

    public Integer getMinQuantity() {
        return minQuantity;
    }
    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getFromDate() {
        return fromDate;
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getTillDate() {
        return tillDate;
    }
    public void setTillDate(Date tillDate) {
        this.tillDate = tillDate;
    }
}