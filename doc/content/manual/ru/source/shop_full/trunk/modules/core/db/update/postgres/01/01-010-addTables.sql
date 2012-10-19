create table SHOP_PRODUCT (
    ID uuid not null,       --Первичный ключ (системное поле)
    CREATE_TS timestamp,    --Когда создано (системное поле)
    CREATED_BY varchar(50), --Кем  создано (системное поле)
    VERSION integer,        --Версия (системное поле)
    UPDATE_TS timestamp,    --Когда было последнее изменение (системное поле)
    UPDATED_BY varchar(50), --Кто последний раз изменил сущность(системное поле)
    DELETE_TS timestamp,    --Когда удалено (системное поле)
    DELETED_BY varchar(50), --Кем удалено (системное поле)

    NAME varchar(255),      --Наименование
    PRICE numeric(19,2),    --Стоимость единицы
    UNIT varchar(100),      --Название единицы

    primary key (ID)
)^
create table SHOP_BUYER (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    FULL_NAME varchar(255),
    BIRTHDAY timestamp,
    EMAIL varchar(100),
    PHONE varchar(100),
    DELIVERY_ADDRESS varchar(255),

    primary key (ID)
)^
create table SHOP_SALES_PERSON (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    FULL_NAME varchar(255),
    BIRTHDAY timestamp,
    EMAIL varchar(100),
    PHONE varchar(100),
    WORKS_FROM timestamp,
    ADDRESS varchar(255),
    USER_ID uuid,

    primary key (ID)
)^

alter table SHOP_SALES_PERSON add constraint FK_SHOP_SALES_PERSON_USER foreign key (USER_ID) references SEC_USER (ID)^

create table SHOP_SALE (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    ORDER_DATE timestamp,
    DELIVERY_DATE timestamp,
    SALES_PERSON_ID uuid,
    BUYER_ID uuid,
    PRODUCT_ID uuid,
    QUANTITY integer,
    PRICE numeric(19,2),
    PRICE_DESCRIPTION varchar(1000),
    DELIVERY_ADDRESS varchar(255),

    primary key (ID),
    constraint REF_SALE_SALES_PERSON foreign key (SALES_PERSON_ID) references SHOP_SALES_PERSON(ID),
    constraint REF_SALE_BUYER foreign key (BUYER_ID) references SHOP_BUYER(ID),
    constraint REF_SALE_PRODUCT foreign key (PRODUCT_ID) references SHOP_PRODUCT(ID)
)^
create table SHOP_DISCOUNT (
    ID uuid not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),

    FROM_DATE timestamp,
    TILL_DATE timestamp,
    BUYER_ID uuid,
    PRODUCT_ID uuid,
    MIN_QUANTITY integer,
    PRICE numeric(19,2),

    primary key (ID),
    constraint REF_DISCOUNT_BUYER foreign key (BUYER_ID) references SHOP_BUYER(ID),
    constraint REF_DISCOUNT_PRODUCT foreign key (PRODUCT_ID) references SHOP_PRODUCT(ID)
)^