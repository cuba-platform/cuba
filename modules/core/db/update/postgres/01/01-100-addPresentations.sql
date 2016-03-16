-- Description: added Presentation table and updated SearchFolder

create table SEC_PRESENTATION (
    ID uuid,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    COMPONENT varchar(200),
    NAME varchar(255),
    XML varchar(7000),
    USER_ID uuid,
    IS_AUTO_SAVE boolean,
    primary key (ID)
);

alter table SEC_PRESENTATION add constraint SEC_PRESENTATION_USER foreign key (USER_ID) references SEC_USER(ID);


alter table SEC_SEARCH_FOLDER  add column PRESENTATION_ID uuid;


alter table SEC_SEARCH_FOLDER add constraint FK_SEC_SEARCH_FOLDER_PRESENTATION foreign key (PRESENTATION_ID) references SEC_PRESENTATION(ID) on delete set null;
