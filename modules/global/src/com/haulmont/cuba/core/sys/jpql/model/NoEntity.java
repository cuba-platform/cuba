/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.model;

import com.haulmont.cuba.core.sys.jpql.InferredType;

import java.util.List;
import java.util.Set;

/**
 * Author: Alexander Chevelev
 * Date: 26.10.2010
 * Time: 23:25:43
 */
public class NoEntity implements Entity{
    public static final NoEntity instance = new NoEntity();

    private NoEntity() {
    }

    public String getName() {
        return null;
    }

    public String getUserFriendlyName() {
        return null;
    }

    public Attribute getAttributeByName(String attributeName) {
        return null;
    }

    public List<Attribute> findAttributesStartingWith(String fieldNamePattern, Set<InferredType> expectedTypes) {
        return null;
    }

    public void addAttributeCopy(Attribute attribute) {
    }

    public static NoEntity getInstance() {
        return instance;
    }

    public void addSingleValueAttribute(Class aClass, String name, String userFriendlyName) {
    }

    public void addReferenceAttribute(String referencedEntityName, String name, String userFriendlyName) {
    }

    public void addCollectionReferenceAttribute(String referencedEntityName, String name, String userFriendlyName) {
    }
}
