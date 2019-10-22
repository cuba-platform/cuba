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

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseDbGeneratedIdEntity;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Creatable;
import com.haulmont.cuba.core.entity.ReferenceToEntity;
import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Record containing information about entity lifecycle event.
 * Created by <code>EntityLog</code> bean.
 */
@Entity(name = "sec$EntityLog")
@Table(name = "SEC_ENTITY_LOG")
@Listeners("cuba_EntityLogItemDetachListener")
@SystemLevel
public class EntityLogItem extends BaseUuidEntity implements Creatable {

    private static final long serialVersionUID = 5859030306889056606L;

    public enum Type implements EnumClass<String> {
        CREATE("C"),
        MODIFY("M"),
        DELETE("D"),
        RESTORE("R");

        private String id;

        Type(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        public static Type fromId(String value) {
            if ("C".equals(value))
                return CREATE;
            else if ("M".equals(value))
                return MODIFY;
            else if ("D".equals(value))
                return DELETE;
            else if ("R".equals(value))
                return RESTORE;
            else
                return null;
        }
    }

    @Column(name = "CREATE_TS")
    private Date createTs;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Column(name = "EVENT_TS")
    private Date eventTs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "CHANGE_TYPE", length = 1)
    private String type;

    @Column(name = "ENTITY", length = 100)
    private String entity;

    @Embedded
    @EmbeddedParameters(nullAllowed = false)
    private ReferenceToEntity entityRef;

    @Column(name = "ENTITY_INSTANCE_NAME")
    private String entityInstanceName;

    @Transient
    private transient BaseDbGeneratedIdEntity dbGeneratedIdEntity;

    @Transient
    @MetaProperty
    private Set<EntityLogAttr> attributes;

    @Column(name = "CHANGES")
    private String changes;

    @PostConstruct
    public void init() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        entityRef = metadata.create(ReferenceToEntity.class);
    }

    @Override
    public Date getCreateTs() {
        return createTs;
    }

    @Override
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @MetaProperty
    public String getDisplayedEntityName() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        Messages messages = AppBeans.get(Messages.NAME);
        MetaClass metaClass = metadata.getSession().getClass(entity);
        if (metaClass != null) {
            metaClass = metadata.getExtendedEntities().getEffectiveMetaClass(metaClass);
            return messages.getTools().getEntityCaption(metaClass);
        }
        return entity;
    }

    public Date getEventTs() {
        return eventTs;
    }

    public void setEventTs(Date eventTs) {
        this.eventTs = eventTs;
    }

    public Type getType() {
        return Type.fromId(type);
    }

    public void setType(Type type) {
        this.type = type.getId();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<EntityLogAttr> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<EntityLogAttr> attributes) {
        this.attributes = attributes;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public ReferenceToEntity getEntityRef() {
        return entityRef;
    }

    public void setEntityRef(ReferenceToEntity entityRef) {
        this.entityRef = entityRef;
    }

    public BaseDbGeneratedIdEntity getDbGeneratedIdEntity() {
        return dbGeneratedIdEntity;
    }

    public void setDbGeneratedIdEntity(BaseDbGeneratedIdEntity dbGeneratedIdEntity) {
        this.dbGeneratedIdEntity = dbGeneratedIdEntity;
    }

    public String getEntityInstanceName() {
        return entityInstanceName;
    }

    public void setEntityInstanceName(String entityInstanceName) {
        this.entityInstanceName = entityInstanceName;
    }

    public void setObjectEntityId(Object entity) {
        if (entityRef == null) {
            entityRef = AppBeans.get(Metadata.class).create(ReferenceToEntity.class);
        }
        entityRef.setObjectEntityId(entity);
    }

    public Object getObjectEntityId() {
        return entityRef == null ? null : entityRef.getObjectEntityId();
    }
}