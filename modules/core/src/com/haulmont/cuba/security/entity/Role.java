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
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import org.apache.commons.lang.BooleanUtils;

import java.util.Set;

@Entity(name = "sec$Role")
@Table(name = "SEC_ROLE")
public class Role extends StandardEntity
{
    private static final long serialVersionUID = -4889116218059626402L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "IS_SUPER")
    private Boolean superRole;

    @OneToMany(mappedBy = "role")
    @OnDelete(DeletePolicy.CASCADE)
    private Set<Permission> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuperRole() {
        return BooleanUtils.isTrue(superRole);
    }

    public void setSuperRole(boolean superRole) {
        this.superRole = superRole;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
