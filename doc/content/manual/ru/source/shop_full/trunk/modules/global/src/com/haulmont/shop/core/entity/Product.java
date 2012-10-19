/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.shop.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author sukhova
 * @version $Id$
 */
@Entity(name = "shop$Product")

// помечаем, что данный класс связан с таблицей в БД
@Table(name = "SHOP_PRODUCT")

//формируем текстовое имя объекта
@NamePattern("%s|name")

public class Product extends StandardEntity {
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PRICE", length = 15)
    private BigDecimal price;

    //помечаем атрибут unit, что он связан с колонкой SHOP_PRODUCT.UNIT
    //и длина текстового поля <= 100
    @Column(name = "UNIT", length = 100)
    private String unit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return Unit.fromId(unit);
    }

    public void setUnit(Unit unit) {
        this.unit = unit == null ? null: unit.getId();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
