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
 * Time: 23:27:24
 */
public interface Entity {

    String getName();

    String getUserFriendlyName();

    Attribute getAttributeByName(String attributeName);

    List<Attribute> findAttributesStartingWith(String fieldNamePattern, Set<InferredType> expectedTypes);

    void addAttributeCopy(Attribute attribute);

    void addSingleValueAttribute(Class aClass, String name, String userFriendlyName);

    void addReferenceAttribute(String referencedEntityName, String name, String userFriendlyName);

    void addCollectionReferenceAttribute(String referencedEntityName, String name, String userFriendlyName);
}
