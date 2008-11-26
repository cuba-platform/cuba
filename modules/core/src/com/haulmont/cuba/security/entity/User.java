/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.11.2008 13:04:23
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.PersistenceProvider;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "security$User")
@Table(name = "SEC_USER")
public class User extends StandardEntity
{
    @Column(name = "LOGIN", length = PersistenceProvider.LOGIN_FIELD_LEN)
    private String login;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "user")
    private Set<Profile> profiles;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }
}
