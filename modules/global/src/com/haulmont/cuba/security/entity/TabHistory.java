/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Valery Novikov
 * Created: 02.11.2010 10:10:56
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import javax.persistence.*;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@javax.persistence.Entity(name = "sec$TabHistory")
@Table(name = "SEC_TAB_HISTORY")
public class TabHistory extends BaseUuidEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATOR_ID")
    private User creator;

    @Column(name = "CAPTION", length = 255)
    private String caption;

    @Column(name = "URL", length = 4000)
    private String url;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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
}
