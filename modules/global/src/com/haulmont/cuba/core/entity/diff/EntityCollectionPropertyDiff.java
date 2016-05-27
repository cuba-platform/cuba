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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.ViewProperty;

import java.util.ArrayList;
import java.util.List;

@MetaClass(name = "sys$EntityCollectionPropertyDiff")
@SystemLevel
public class EntityCollectionPropertyDiff extends EntityPropertyDiff {

    private static final long serialVersionUID = -7472572229609999760L;

    private List<EntityPropertyDiff> addedEntities = new ArrayList<>();

    private List<EntityPropertyDiff> removedEntities = new ArrayList<>();

    private List<EntityPropertyDiff> modifiedEntities = new ArrayList<>();

    public EntityCollectionPropertyDiff(ViewProperty viewProperty,
                                        MetaProperty metaProperty) {
        super(viewProperty, metaProperty);
    }

    public List<EntityPropertyDiff> getAddedEntities() {
        return addedEntities;
    }

    public void setAddedEntities(List<EntityPropertyDiff> addedEntities) {
        this.addedEntities = addedEntities;
    }

    public List<EntityPropertyDiff> getRemovedEntities() {
        return removedEntities;
    }

    public void setRemovedEntities(List<EntityPropertyDiff> removedEntities) {
        this.removedEntities = removedEntities;
    }

    public List<EntityPropertyDiff> getModifiedEntities() {
        return modifiedEntities;
    }

    public void setModifiedEntities(List<EntityPropertyDiff> modifiedEntities) {
        this.modifiedEntities = modifiedEntities;
    }
}