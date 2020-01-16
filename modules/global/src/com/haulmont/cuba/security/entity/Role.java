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

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.TenantEntity;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.TrackEditScreenHistory;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Set;

/**
 * User role.
 */
@Entity(name = "sec$Role")
@Table(name = "SEC_ROLE")
@NamePattern("%s [%s]|locName,name")
@TrackEditScreenHistory
public class Role extends StandardEntity implements TenantEntity {

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

    @Column(name = "SYS_TENANT_ID")
    protected String sysTenantId;

    @Column(name = "SECURITY_SCOPE")
    private String securityScope;

    @Column(name = "DEFAULT_SCREEN_ACCESS")
    private Integer defaultScreenAccess;

    @Column(name = "DEFAULT_ENTITY_CREATE_ACCESS")
    private Integer defaultEntityCreateAccess;

    @Column(name = "DEFAULT_ENTITY_READ_ACCESS")
    private Integer defaultEntityReadAccess;

    @Column(name = "DEFAULT_ENTITY_UPDATE_ACCESS")
    private Integer defaultEntityUpdateAccess;

    @Column(name = "DEFAULT_ENTITY_DELETE_ACCESS")
    private Integer defaultEntityDeleteAccess;

    @Column(name = "DEFAULT_ENTITY_ATTRIBUTE_ACCESS")
    private Integer defaultEntityAttributeAccess;

    @Column(name = "DEFAULT_SPECIFIC_ACCESS")
    private Integer defaultSpecificAccess;

    @OneToMany(mappedBy = "role")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private Set<Permission> permissions;

    @Transient
    private boolean isPredefined = false;

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

    public String getSecurityScope() {
        return securityScope;
    }

    public void setSecurityScope(String securityScope) {
        this.securityScope = securityScope;
    }

    @MetaProperty(related = "securityScope")
    public String getLocSecurityScope() {
        if (securityScope != null) {
            SecurityScope scope = new SecurityScope(securityScope);
            return scope.getLocName();
        }
        return null;
    }

    public Access getDefaultScreenAccess() {
        return Access.fromId(defaultScreenAccess);
    }

    public void setDefaultScreenAccess(Access defaultScreenAccess) {
        this.defaultScreenAccess = defaultScreenAccess == null ? null : defaultScreenAccess.getId();
    }

    public Access getDefaultEntityCreateAccess() {
        return Access.fromId(defaultEntityCreateAccess);
    }

    public void setDefaultEntityCreateAccess(Access defaultEntityCreateAccess) {
        this.defaultEntityCreateAccess = defaultEntityCreateAccess == null ? null : defaultEntityCreateAccess.getId();
    }

    public Access getDefaultEntityReadAccess() {
        return Access.fromId(defaultEntityReadAccess);
    }

    public void setDefaultEntityReadAccess(Access defaultEntityReadAccess) {
        this.defaultEntityReadAccess = defaultEntityReadAccess == null ? null : defaultEntityReadAccess.getId();
    }

    public Access getDefaultEntityUpdateAccess() {
        return Access.fromId(defaultEntityUpdateAccess);
    }

    public void setDefaultEntityUpdateAccess(Access defaultEntityUpdateAccess) {
        this.defaultEntityUpdateAccess = defaultEntityUpdateAccess == null ? null : defaultEntityUpdateAccess.getId();
    }

    public Access getDefaultEntityDeleteAccess() {
        return Access.fromId(defaultEntityDeleteAccess);
    }

    public void setDefaultEntityDeleteAccess(Access defaultEntityDeleteAccess) {
        this.defaultEntityDeleteAccess = defaultEntityDeleteAccess == null ? null : defaultEntityDeleteAccess.getId();
    }

    public EntityAttrAccess getDefaultEntityAttributeAccess() {
        return EntityAttrAccess.fromId(defaultEntityAttributeAccess);
    }

    public void setDefaultEntityAttributeAccess(EntityAttrAccess defaultEntityAttributeAccess) {
        this.defaultEntityAttributeAccess = defaultEntityAttributeAccess == null ? null : defaultEntityAttributeAccess.getId();
    }

    public Access getDefaultSpecificAccess() {
        return Access.fromId(defaultSpecificAccess);
    }

    public void setDefaultSpecificAccess(Access defaultSpecificAccess) {
        this.defaultSpecificAccess = defaultSpecificAccess == null ? null : defaultSpecificAccess.getId();
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

    public boolean isPredefined() {
        return isPredefined;
    }

    public void setPredefined(boolean predefined) {
        isPredefined = predefined;
    }

    public String getSysTenantId() {
        return sysTenantId;
    }

    public void setSysTenantId(String sysTenantId) {
        this.sysTenantId = sysTenantId;
    }
}