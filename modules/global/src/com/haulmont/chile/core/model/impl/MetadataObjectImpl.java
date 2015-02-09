/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.model.MetadataObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
@SuppressWarnings({"TransientFieldNotInitialized"})
public abstract class MetadataObjectImpl implements MetadataObject, Serializable {

    private static final long serialVersionUID = 5179324236413815312L;

    protected String name;

    private transient Map<String, Object> annotations = new HashMap<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getAnnotations() {
        return annotations;
    }

    public void setName(String name) {
        this.name = name;
    }
}