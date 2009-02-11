/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.02.2009 17:22:38
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Date;

import org.apache.commons.lang.BooleanUtils;

@Entity(name = "sec$Subject")
@Table(name = "SEC_SUBJECT")
public class Subject extends StandardEntity
{
    private static final long serialVersionUID = 837722143973534603L;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Column(name = "IS_DEFAULT")
    private Boolean defaultSubject;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    public boolean isDefaultSubject() {
        return BooleanUtils.isTrue(defaultSubject);
    }

    public void setDefaultSubject(boolean defaultSubject) {
        this.defaultSubject = defaultSubject;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
