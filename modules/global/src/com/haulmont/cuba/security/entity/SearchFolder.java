/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
    protected Boolean isSet = false;

    @Column(name="ENTITY_TYPE")
    protected String entityType;

    @Override
    public void copyFrom(AbstractSearchFolder srcFolder) {
        super.copyFrom(srcFolder);

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        setUser(sessionSource.getUserSession().getUser());
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
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMainMessage(name);
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
