/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.2009 16:23:18
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity(name = "sec$LoggedEntity")
@Table(name = "SEC_LOGGED_ENTITY")
public class LoggedEntity extends BaseUuidEntity
{
    private static final long serialVersionUID = 2189206984294705835L;

    @Column(name = "NAME")
    private String entity;

    @OneToMany(mappedBy = "entity")
    private Set<LoggedAttribute> attributes;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Set<LoggedAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<LoggedAttribute> attributes) {
        this.attributes = attributes;
    }
}
