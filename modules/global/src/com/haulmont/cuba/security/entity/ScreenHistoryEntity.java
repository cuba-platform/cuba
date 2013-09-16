/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import javax.persistence.*;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.openjpa.persistence.Persistent;

import java.util.UUID;

@javax.persistence.Entity(name = "sec$ScreenHistory")
@Table(name = "SEC_SCREEN_HISTORY")
@SystemLevel
public class ScreenHistoryEntity extends BaseUuidEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "CAPTION", length = 255)
    private String caption;

    @Column(name = "URL", length = 4000)
    private String url;

    @Column(name = "ENTITY_ID")
    @Persistent
    private UUID entityId;

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
}
