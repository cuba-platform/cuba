/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 12:40:31
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity(name = "sec$ProfileRole")
@Table(name = "SEC_PROFILE_ROLE")
public class ProfileRole extends StandardEntity
{
    @ManyToOne(optional = false)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
