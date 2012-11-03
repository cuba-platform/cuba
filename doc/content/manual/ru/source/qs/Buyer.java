package com.haulmont.shop.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity(name = "shop$Buyer")        // класс является сущностью с именем shop$Buyer
@Table(name = "SHOP_BUYER")         // класс связан с таблицей SHOP_BUYER
@NamePattern("%s|fullName")         // формируем текстовое имя объекта
public class Buyer extends StandardEntity {
    // базовый класс StandardEntity обеспечивает стандартные свойства сущности

    @Column(name = "FIRST_NAME")    // атрибут связан с колонкой SHOP_BUYER.FIRST_NAME
    private String firstName;

    @Column(name = "SURNAME")       // атрибут связан с колонкой SHOP_BUYER.SURNAME
    private String surName;

    @Column(name = "BIRTHDAY")      // атрибут связан с колонкой SHOP_BUYER.BIRTHDAY
    @Temporal(TemporalType.DATE)    // колонка хранит только дату, без времени
    private Date birthday;

    @Column(name = "EMAIL")         // атрибут связан с колонкой SHOP_BUYER.EMAIL
    private String email;

    @Column(name = "PHONE")         // атрибут связан с колонкой SHOP_BUYER.PHONE
    private String phone;

    @Column(name = "DELIVERY_ADDRESS")  // атрибут связан с колонкой SHOP_BUYER.DELIVERY_ADDRESS
    private String deliveryAddress;

    @OneToMany(mappedBy = "buyer")      // связь с БД через атрибут Discount.buyer
    @Composition                        // коллекция скидок является композитным атрибутом, т.е.
                                        // существует только вместе с экземпляром покупателя
    @OnDelete(DeletePolicy.CASCADE)     // при удалении покупателя будут удалены все его скидки
    private List<Discount> discounts;

    // методы доступа:

	public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }
	
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }


    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}