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

package com.haulmont.cuba.core.entity.diff;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Diff object for Entity
 *
 */
@MetaClass(name = "sys$EntityDiff")
@SystemLevel
public class EntityDiff extends AbstractNotPersistentEntity implements Serializable {

    private static final long serialVersionUID = -3884249873393845439L;

    private View diffView;

    private EntitySnapshot beforeSnapshot;

    private EntitySnapshot afterSnapshot;

    private Entity beforeEntity;

    private Entity afterEntity;

    private List<EntityPropertyDiff> propertyDiffs = new ArrayList<>();

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

    public Entity getBeforeEntity() {
        return beforeEntity;
    }

    public void setBeforeEntity(Entity beforeEntity) {
        this.beforeEntity = beforeEntity;
    }

    public Entity getAfterEntity() {
        return afterEntity;
    }

    public void setAfterEntity(Entity afterEntity) {
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