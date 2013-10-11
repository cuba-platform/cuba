/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;

import javax.persistence.*;

/**
 * Search folder settings.
 *
 * @author krivopustov
 * @version $Id$
 */
@Entity(name = "sec$SearchFolder")
@Table(name = "SEC_SEARCH_FOLDER")
@PrimaryKeyJoinColumn(name = "FOLDER_ID", referencedColumnName = "ID")
@DiscriminatorValue("S")
@SystemLevel
public class SearchFolder extends AbstractSearchFolder {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRESENTATION_ID")
    protected Presentation presentation;

    @Column(name="IS_SET")
    protected Boolean isSet;

    @Column(name="ENTITY_TYPE")
    protected String entityType;

    @Override
    public void copyFrom(AbstractSearchFolder srcFolder) {
        super.copyFrom(srcFolder);

        setUser(AppBeans.get(UserSessionSource.class).getUserSession().getUser());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getCaption() {
        return AppBeans.get(Messages.class).getMainMessage(name);
    }

    public Boolean getIsSet(){
        return isSet;
    }

    public void setIsSet(Boolean isSet){
        this.isSet=isSet;
    }

    public String getEntityType(){
        return entityType;
    }

    public void setEntityType(String entityType){
        this.entityType=entityType;
    }

}
