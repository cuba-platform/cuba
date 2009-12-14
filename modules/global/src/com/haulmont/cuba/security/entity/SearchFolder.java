/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2009 15:47:04
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.AbstractSearchFolder;

import javax.persistence.*;

@Entity(name = "sec$SearchFolder")
@Table(name = "SEC_SEARCH_FOLDER")
@PrimaryKeyJoinColumn(name="FOLDER_ID", referencedColumnName = "ID")
@DiscriminatorValue("S")
public class SearchFolder extends AbstractSearchFolder {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
