/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@MetaClass(name = "sys$EntityCollectionPropertyDiff")
public class EntityCollectionPropertyDiff extends EntityPropertyDiff {

    private static final long serialVersionUID = -7472572229609999760L;

    private List<EntityPropertyDiff> addedEntities = new ArrayList<EntityPropertyDiff>();

    private List<EntityPropertyDiff> removedEntities = new ArrayList<EntityPropertyDiff>();

    private List<EntityPropertyDiff> modifiedEntities = new ArrayList<EntityPropertyDiff>();

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
