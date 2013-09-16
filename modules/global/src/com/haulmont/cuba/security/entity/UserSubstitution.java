/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "sec$UserSubstitution")
@Table(name = "SEC_USER_SUBSTITUTION")
@SystemLevel
public class UserSubstitution extends StandardEntity {

    private static final long serialVersionUID = -1260499554824220311L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBSTITUTED_USER_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    protected User substitutedUser;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    protected Date startDate;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    protected Date endDate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSubstitutedUser() {
        return substitutedUser;
    }

    public void setSubstitutedUser(User substitutedUser) {
        this.substitutedUser = substitutedUser;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
