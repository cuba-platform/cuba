/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.11.2008 16:38:30
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.PersistenceProvider;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;
import java.util.Date;

public class BaseLongIdEntity implements BaseEntity<Long>
{
    @Id
    @Column(name = "ID")
    private Long id;

    private UUID uuid;

    @Column(name = "CREATE_TS")
    private Date createTs;

    @Column(name = "CREATED_BY")
    private UUID createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }
}
