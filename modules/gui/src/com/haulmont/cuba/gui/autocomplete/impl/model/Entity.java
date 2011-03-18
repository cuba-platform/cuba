package com.haulmont.cuba.jpql.impl.model;

import java.util.List;

/**
 * Author: Alexander Chevelev
 * Date: 26.10.2010
 * Time: 23:27:24
 */
public interface Entity {
    String getName();

    void addSingleValueAttribute(Class aClass, String name);

    Attribute getAttributeByName(String attributeName);

    List<Attribute> findAttributesStartingWith(String fieldNamePattern);

    void addReferenceAttribute(String referencedEntityName, String name);

    void addAttributeCopy(Attribute attribute);

    void addCollectionReferenceAttribute(String referencedEntityName, String name);

    void addSingleValueAttribute(Class aClass, String name, String userFriendlyName);

    void addReferenceAttribute(String referencedEntityName, String name, String userFriendlyName);

    void addCollectionReferenceAttribute(String referencedEntityName, String name, String userFriendlyName);
}
