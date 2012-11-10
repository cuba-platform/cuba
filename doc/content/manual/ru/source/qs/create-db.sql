create table SALES_CUSTOMER (
    -- system fields
    ID uuid not null ,             
    CREATE_TS timestamp,          
    CREATED_BY varchar(50),       
    VERSION integer,              
    UPDATE_TS timestamp,          
    UPDATED_BY varchar(50),       
    DELETE_TS timestamp,          
    DELETED_BY varchar(50),       
    -- app fields
    NAME varchar(100),            
    EMAIL varchar(100),           
    -- 
    primary key (ID)
)^
create table SALES_ORDER (
    -- system fields
    ID uuid not null,			  
    CREATE_TS timestamp,          
    CREATED_BY varchar(50),       
    VERSION integer,              
    UPDATE_TS timestamp,          
    UPDATED_BY varchar(50),       
    DELETE_TS timestamp,          
    DELETED_BY varchar(50),       
    -- app fields
    CUSTOMER_ID uuid,                
    DATE timestamp,          
    AMOUNT numeric(19,2),          
    --
    primary key (ID),
    constraint FK_SALES_ORDER_CUSTOMER foreign key (CUSTOMER_ID)
        references SALES_CUSTOMER(ID)
)^