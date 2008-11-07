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
import java.util.UUID;

@MappedSuperclass
public class BaseUuidEntity implements BaseEntity<UUID>
{
    @Id
    @Column(name = "ID")
    @Persistent
    private UUID id;

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

    public void setUuid(UUID id) {
        this.id = id;
    }
}
