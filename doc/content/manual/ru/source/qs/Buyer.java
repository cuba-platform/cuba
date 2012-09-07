package com.haulmont.shop.core.entity;

//аннотируем класс как сущность предметной области
@Entity(name = "shop$Buyer")

//помечаем, что данный класс связан с таблицей в БД
@Table(name = "SHOP_BUYER")

//формируем текстовое имя объекта
@NamePattern("%s|fullName")

public class Buyer extends StandardEntity {

    //помечаем атрибут firstName, что он связан с колонкой SHOP_BUYER.FIRST_NAME базы данных
    @Column(name = "FIRST_NAME")
    private String firstName;

    //помечаем атрибут surName, что он связан с колонкой SHOP_BUYER.SURNAME базы данных
    @Column(name = "SURNAME")
    private String surName;

    //помечаем атрибут birthday, что он связан с колонкой SHOP_BUYER.BIRTHDAY базы данных
    //колонка хранит только дату, без времени
    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTHDAY")
    private Date birthday;

    //помечаем атрибут email, что он связан с колонкой SHOP_BUYER.EMAIL базы данных
    @Column(name = "EMAIL")
    private String email;

    //помечаем атрибут phone, что он связан с колонкой SHOP_BUYER.PHONE базы данных
    @Column(name = "PHONE")
    private String phone;

    //помечаем атрибут deliveryAddress, что он связан с колонкой SHOP_BUYER.DELIVERY_ADDRESS базы данных
    @Column(name = "DELIVERY_ADDRESS")
    private String deliveryAddress;

    @OneToMany(mappedBy = "buyer")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    private List<Discount> discounts;

    //методы доступа:

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