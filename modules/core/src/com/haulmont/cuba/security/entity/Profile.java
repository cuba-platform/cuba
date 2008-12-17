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

import javax.persistence.*;
import java.util.Set;
import java.io.Serializable;

@Entity(name = "sec$Profile")
@Table(name = "SEC_PROFILE")
@Listeners({"com.haulmont.cuba.security.listener.ProfileEntityListener"})
public class Profile extends StandardEntity
{
    private static final long serialVersionUID = -9008053062363137148L;

    @Column(name = "NAME")
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "profile")
    private Set<ProfileRole> profileRoles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ProfileRole> getProfileRoles() {
        return profileRoles;
    }

    public void setProfileRoles(Set<ProfileRole> profileRoles) {
        this.profileRoles = profileRoles;
    }
}
