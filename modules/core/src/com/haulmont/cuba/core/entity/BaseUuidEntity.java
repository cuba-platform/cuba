/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.11.2008 16:40:37
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import org.apache.openjpa.persistence.Persistent;

import javax.persistence.MappedSuperclass;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import java.util.UUID;
import java.util.Date;

import com.haulmont.cuba.core.CubaProperties;
import com.haulmont.cuba.core.persistence.BaseEntityListener;

@MappedSuperclass
@EntityListeners({BaseEntityListener.class})
public class BaseUuidEntity implements BaseEntity<UUID>
{
    @Id
    @Column(name = "ID")
    @Persistent
    private UUID id;

    @Column(name = "CREATE_TS")
    private Date createTs;

    @Column(name = "CREATED_BY", length = CubaProperties.LOGIN_FIELD_LEN)
    private String createdBy;

    public BaseUuidEntity() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUuid() {
        return id;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUuid(UUID id) {
        this.id = id;
    }
}
