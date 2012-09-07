//аннотируем класс как сущность предметной области
@Entity(name = "shop$Buyer")

//помечаем, что данный класс связан с таблицей в БД
@Table(name = "SHOP_BUYER")

//формируем текстовое имя объекта
@NamePattern("%s|fullName")

public class Buyer extends StandardEntity {

    //помечаем атрибут fullName, что он связан с колонкой SHOP_BUYER.FULL_NAME базы данных
    @Column(name = "FULL_NAME")
    private String fullName;

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

    //методы доступа:
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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