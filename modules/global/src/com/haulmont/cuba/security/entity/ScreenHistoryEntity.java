/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.openjpa.persistence.Persistent;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.UUID;

/**
 * Screen history item.
 *
 * @author novikov
 * @version $Id$
 */
@Entity(name = "sec$ScreenHistory")
@Table(name = "SEC_SCREEN_HISTORY")
@SystemLevel
public class ScreenHistoryEntity extends BaseUuidEntity {

    private static final long serialVersionUID = 1L;

    @PostConstruct
    protected void init() {
        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
        setUser(userSession.getUser());
        setSubstitutedUser(userSession.getSubstitutedUser());
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBSTITUTED_USER_ID")
    protected User substitutedUser;

    @Column(name = "CAPTION", length = 255)
    protected String caption;

    @Column(name = "URL", length = 4000)
    protected String url;

    @Column(name = "ENTITY_ID")
    @Persistent
    protected UUID entityId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public User getSubstitutedUser() {
        return substitutedUser;
    }

    public void setSubstitutedUser(User substitutedUser) {
        this.substitutedUser = substitutedUser;
    }
}
