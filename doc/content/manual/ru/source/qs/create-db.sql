create table SHOP_BUYER (
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
    BIRTHDAY timestamp,           
    EMAIL varchar(100),           
    PHONE varchar(100),           
    DELIVERY_ADDRESS varchar(255),
    -- 
    primary key (ID)
)^
create table SHOP_DISCOUNT (
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
    BUYER_ID uuid,                
    MIN_QUANTITY integer,         
    PRICE numeric(19,2),          
    FROM_DATE timestamp,          
    TILL_DATE timestamp,          
    --
    primary key (ID),
    constraint REF_DISCOUNT_BUYER foreign key (BUYER_ID)
        references SHOP_BUYER(ID)
)^