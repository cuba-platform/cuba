--begin SALES_CUSTOMER
create table SALES_CUSTOMER (
    -- system fields
    ID varchar(36) not null,
    CREATE_TS timestamp,          
    CREATED_BY varchar(50),       
    VERSION integer,              
    UPDATE_TS timestamp,          
    UPDATED_BY varchar(50),       
    DELETE_TS timestamp,          
    DELETED_BY varchar(50),       
    -- app fields
    NAME varchar(100) not null,            
    EMAIL varchar(50) not null,           
    -- 
    primary key (ID)
)^
--end SALES_CUSTOMER
--begin SALES_ORDER
create table SALES_ORDER (
    -- system fields
    ID varchar(36) not null,
    CREATE_TS timestamp,          
    CREATED_BY varchar(50),       
    VERSION integer,              
    UPDATE_TS timestamp,          
    UPDATED_BY varchar(50),       
    DELETE_TS timestamp,          
    DELETED_BY varchar(50),       
    -- app fields
    CUSTOMER_ID varchar(36),
    DATE_ timestamp not null,          
    AMOUNT decimal(19,2),          
    --
    primary key (ID),
    constraint FK_SALES_ORDER_CUSTOMER foreign key (CUSTOMER_ID)
        references SALES_CUSTOMER(ID)

)^
--end SALES_ORDER