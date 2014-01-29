/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.IllegalEntityStateException;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UuidProvider;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.kernel.DetachedStateManager;
import org.apache.openjpa.persistence.Persistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.BitSet;
import java.util.Date;
import java.util.UUID;

/**
 * Base class for persistent entities with UUID identifier.<br>
 * Inherit from it if you need an entity without optimistic locking, update and soft deletion info.
 *
 * @author krivopustov
 * @version $Id$
 */
@MappedSuperclass
public abstract class BaseUuidEntity extends AbstractInstance implements BaseEntity<UUID> {

    private static final long serialVersionUID = -2217624132287086972L;

    @Id
    @Column(name = "ID")
    @Persistent
    protected UUID id;

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = LOGIN_FIELD_LEN)
    protected String createdBy;

    public BaseUuidEntity() {
        id = UuidProvider.createUuid();
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getUuid() {
        return id;
    }

    @Override
    public MetaClass getMetaClass() {
        return AppBeans.get(Metadata.class).getSession().getClassNN(getClass());
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

    public void setUuid(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseUuidEntity that = (BaseUuidEntity) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getName() + "-" + id;
    }

    /**
     * Called from enhanced setters before property is changed.
     *
     * @param name          property name
     * @param fieldIndex    corresponding OpenJPA field index
     * @param value         new value
     */
    protected void propertyChanging(String name, int fieldIndex, Object value) {
        if (!allowSetNotLoadedAttributes
                && fieldIndex > -1
                && this instanceof PersistenceCapable
                && ((PersistenceCapable) this).pcGetStateManager() instanceof DetachedStateManager) {
            BitSet loaded = ((DetachedStateManager) ((PersistenceCapable) this).pcGetStateManager()).getLoaded();
            if (!loaded.get(fieldIndex)) {
                throw new IllegalEntityStateException("Property '" +
                        getClass().getCanonicalName() + "." + name + "' is not loaded");
            }
        }
    }

    /** For internal use only. */
    public static boolean allowSetNotLoadedAttributes;
}
