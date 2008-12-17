/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.11.2008 13:08:12
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity(name = "sec$Role")
@Table(name = "SEC_ROLE")
@Listeners({"com.haulmont.cuba.security.listener.RoleEntityListener"})
public class Role extends StandardEntity
{
    private static final long serialVersionUID = -4889116218059626402L;

    @Column(name = "NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
