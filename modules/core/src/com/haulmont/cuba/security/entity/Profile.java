/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 12:37:01
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Set;
import java.io.Serializable;

import org.apache.commons.lang.BooleanUtils;

@Entity(name = "sec$Profile")
@Table(name = "SEC_PROFILE")
public class Profile extends StandardEntity
{
    private static final long serialVersionUID = 8037692798864039665L;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "profile")
    @OnDelete(DeletePolicy.CASCADE)
    private Set<ProfileRole> profileRoles;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProfileRole> getProfileRoles() {
        return profileRoles;
    }

    public void setProfileRoles(Set<ProfileRole> profileRoles) {
        this.profileRoles = profileRoles;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
