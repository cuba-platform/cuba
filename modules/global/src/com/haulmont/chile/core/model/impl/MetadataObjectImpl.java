/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.model.MetadataObject;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author abramov
 * @version $Id$
 */
@SuppressWarnings({"TransientFieldNotInitialized"})
public abstract class MetadataObjectImpl<T extends MetadataObject> implements MetadataObject<T>, Serializable {

    private static final long serialVersionUID = 5179324236413815312L;

    private transient T ancestor;

    protected transient Collection<T> ancestors = new LinkedHashSet<>(1);
    protected transient Collection<T> descendants = new LinkedHashSet<>(1);

    protected String name;

    private transient String caption;
    private transient String description;

    private transient UUID uuid = UUID.randomUUID();

    private transient Map<String, Object> annotations = new HashMap<>();

    @Override
    public T getAncestor() {
        if (ancestor == null) {
            if (ancestors.size() == 0) {
                return null;
            } else if (ancestors.size() == 1) {
                ancestor = (T) CollectionUtils.get(ancestors, 0);
                return ancestor;
            } else {
                throw new IllegalStateException(
                        String.format("%s has more than one ancestor (%s)", this, ancestors));
            }
        } else {
            return ancestor;
        }
    }

    @Override
    public Collection<T> getAncestors() {
        return ancestors;
    }

    @Override
    public Collection<T> getDescendants() {
        return descendants;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Map<String, Object> getAnnotations() {
        return annotations;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void addAncestor(T ancestorClass) {
        ancestors.add(ancestorClass);
        ((MetadataObjectImpl) ancestorClass).descendants.add(this);
    }
}