/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity.diff;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.ViewProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
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
