/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.google.common.base.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.TimeSource;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for persistent entities.
 * <p>When choosing a base class for your entity, consider more specific base classes defining the primary key type:
 * <ul>
 * <li>{@link BaseUuidEntity}</li>
 * <li>{@link BaseLongIdEntity}</li>
 * <li>{@link BaseIntegerIdEntity}</li>
 * <li>{@link BaseStringIdEntity}</li>
 * </ul>
 * or most commonly used {@link StandardEntity}.
 * </p>
 *
 * @author krivopustov
 * @version $Id$
 */
@MappedSuperclass
public abstract class BaseGenericIdEntity<T> extends AbstractInstance implements BaseEntity<T> {

    private static final long serialVersionUID = -8400641366148656528L;

    @Transient
    protected boolean __new = true;

    @Transient
    protected boolean __detached;

    protected transient boolean __managed;

    @Transient
    protected boolean __removed;

    @Transient
    protected String[] __inaccessibleAttributes;

    @Transient
    protected Map<String, CategoryAttributeValue> dynamicAttributes = null;

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = LOGIN_FIELD_LEN)
    protected String createdBy;

    public abstract void setId(T id);

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (__managed)
            __detached = true;
        out.defaultWriteObject();
    }

    /** INTERNAL. */
    public boolean __new() {
        return __new;
    }

    /** INTERNAL. */
    public void __new(boolean cubaNew) {
        this.__new = cubaNew;
    }

    /** INTERNAL. */
    public boolean __managed() {
        return __managed;
    }

    /** INTERNAL. */
    public void __managed(boolean cubaManaged) {
        this.__managed = cubaManaged;
    }

    /** INTERNAL. */
    public boolean __detached() {
        return __detached;
    }

    /** INTERNAL. */
    public void __detached(boolean detached) {
        this.__detached = detached;
    }

    /** INTERNAL. */
    public boolean __removed() {
        return __removed;
    }

    /** INTERNAL. */
    public void __removed(boolean removed) {
        this.__removed = removed;
    }

    /** INTERNAL */
    public String[] __inaccessibleAttributes() {
        return __inaccessibleAttributes;
    }

    /** INTERNAL */
    public void __inaccessibleAttributes(String[] __inaccessibleAttributes) {
        this.__inaccessibleAttributes = __inaccessibleAttributes;
    }

    @Override
    public MetaClass getMetaClass() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getSession().getClassNN(getClass());
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

    @Override
    public void setValue(String property, Object obj, boolean checkEquals) {
        if (DynamicAttributesUtils.isDynamicAttribute(property)) {
            Preconditions.checkState(dynamicAttributes != null, "Dynamic attributes should be loaded explicitly");
            String attributeCode = DynamicAttributesUtils.decodeAttributeCode(property);
            CategoryAttributeValue categoryAttributeValue = dynamicAttributes.get(attributeCode);
            if (categoryAttributeValue != null) {
                if (obj != null) {
                    categoryAttributeValue.setValue(obj);
                    categoryAttributeValue.setDeleteTs(null);
                } else {
                    categoryAttributeValue.setValue(null);
                    categoryAttributeValue.setDeleteTs(AppBeans.get(TimeSource.class).currentTimestamp());
                }
            } else if (obj != null) {
                categoryAttributeValue = new CategoryAttributeValue();
                categoryAttributeValue.setValue(obj);
                categoryAttributeValue.setEntityId(getUuid());
                categoryAttributeValue.setCode(attributeCode);
                DynamicAttributes dynamicAttributesBean = AppBeans.get(DynamicAttributes.NAME);
                categoryAttributeValue.setCategoryAttribute(
                        dynamicAttributesBean.getAttributeForMetaClass(getMetaClass(), attributeCode));
                dynamicAttributes.put(attributeCode, categoryAttributeValue);
            }

            propertyChanged(property, null, obj);
        } else {
            super.setValue(property, obj, checkEquals);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(String property) {
        if (DynamicAttributesUtils.isDynamicAttribute(property)) {
            if (PersistenceHelper.isNew(this) && dynamicAttributes == null) {
                    dynamicAttributes = new HashMap<>();
            }

            Preconditions.checkState(dynamicAttributes != null, "Dynamic attributes should be loaded explicitly");
            CategoryAttributeValue categoryAttributeValue = dynamicAttributes.get(DynamicAttributesUtils.decodeAttributeCode(property));
            if (categoryAttributeValue != null) {
                return (T) categoryAttributeValue.getValue();
            } else {
                return null;
            }
        } else {
            return super.getValue(property);
        }
    }

    public void setDynamicAttributes(Map<String, CategoryAttributeValue> dynamicAttributes) {
        this.dynamicAttributes = dynamicAttributes;
    }

    @Nullable
    public Map<String, CategoryAttributeValue> getDynamicAttributes() {
        return dynamicAttributes;
    }
}