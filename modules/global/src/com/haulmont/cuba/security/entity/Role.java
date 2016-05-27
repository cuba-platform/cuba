/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.EnableRestore;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.TrackEditScreenHistory;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * User role.
 */
@Entity(name = "sec$Role")
@Table(name = "SEC_ROLE")
@NamePattern("%s [%s]|locName,name")
@TrackEditScreenHistory
@EnableRestore
public class Role extends StandardEntity {

    private static final long serialVersionUID = -4889116218059626402L;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "LOC_NAME")
    private String locName;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "ROLE_TYPE")
    private Integer type;

    @Column(name = "IS_DEFAULT_ROLE")
    private Boolean defaultRole;

    @OneToMany(mappedBy = "role")
    @OnDelete(DeletePolicy.CASCADE)
    private Set<Permission> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoleType getType() {
        return RoleType.fromId(type);
    }

    public void setType(RoleType type) {
        this.type = type == null ? null : type.getId();
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(Boolean defaultRole) {
        this.defaultRole = defaultRole;
    }
}