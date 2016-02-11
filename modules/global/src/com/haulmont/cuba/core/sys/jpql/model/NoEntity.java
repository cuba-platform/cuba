/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.model;

import com.haulmont.cuba.core.sys.jpql.InferredType;

import java.util.List;
import java.util.Set;

/**
 * @author chevelev
 * @version $Id$
 */
public class NoEntity implements Entity{
    public static final NoEntity instance = new NoEntity();

    private NoEntity() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getUserFriendlyName() {
        return null;
    }

    @Override
    public Attribute getAttributeByName(String attributeName) {
        return null;
    }

    @Override
    public List<Attribute> findAttributesStartingWith(String fieldNamePattern, Set<InferredType> expectedTypes) {
        return null;
    }

    @Override
    public void addAttributeCopy(Attribute attribute) {
    }

    public static NoEntity getInstance() {
        return instance;
    }

    @Override
    public void addSingleValueAttribute(Class aClass, String name, String userFriendlyName) {
    }

    @Override
    public void addReferenceAttribute(String referencedEntityName, String name, String userFriendlyName, boolean isEmbedded) {
    }

    @Override
    public void addCollectionReferenceAttribute(String referencedEntityName, String name, String userFriendlyName) {
    }
}