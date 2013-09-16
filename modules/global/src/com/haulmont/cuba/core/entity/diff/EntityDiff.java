/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity.diff;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Diff object for Entity
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@MetaClass(name = "sys$EntityDiff")
@SystemLevel
public class EntityDiff extends AbstractNotPersistentEntity implements Serializable {

    private static final long serialVersionUID = -3884249873393845439L;

    private View diffView;

    private EntitySnapshot beforeSnapshot;

    private EntitySnapshot afterSnapshot;

    private BaseEntity beforeEntity;

    private BaseEntity afterEntity;

    private List<EntityPropertyDiff> propertyDiffs = new ArrayList<EntityPropertyDiff>();

    public EntityDiff(View diffView) {
        this.diffView = diffView;
    }

    public View getDiffView() {
        return diffView;
    }

    public void setDiffView(View diffView) {
        this.diffView = diffView;
    }

    public EntitySnapshot getBeforeSnapshot() {
        return beforeSnapshot;
    }

    public void setBeforeSnapshot(EntitySnapshot beforeSnapshot) {
        this.beforeSnapshot = beforeSnapshot;
    }

    public EntitySnapshot getAfterSnapshot() {
        return afterSnapshot;
    }

    public void setAfterSnapshot(EntitySnapshot afterSnapshot) {
        this.afterSnapshot = afterSnapshot;
    }

    public BaseEntity getBeforeEntity() {
        return beforeEntity;
    }

    public void setBeforeEntity(BaseEntity beforeEntity) {
        this.beforeEntity = beforeEntity;
    }

    public BaseEntity getAfterEntity() {
        return afterEntity;
    }

    public void setAfterEntity(BaseEntity afterEntity) {
        this.afterEntity = afterEntity;
    }

    public List<EntityPropertyDiff> getPropertyDiffs() {
        return propertyDiffs;
    }

    public void setPropertyDiffs(List<EntityPropertyDiff> propertyDiffs) {
        this.propertyDiffs = propertyDiffs;
    }

    @MetaProperty
    public String getLabel(){
        String label = "";
        if (beforeSnapshot != null)
            label += beforeSnapshot.getLabel() + " : ";
        else
            label += "";

        if (afterSnapshot != null)
            label += afterSnapshot.getLabel();

        return label;
    }
}
