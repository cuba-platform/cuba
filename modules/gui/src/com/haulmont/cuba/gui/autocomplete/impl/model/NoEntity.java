package com.haulmont.cuba.jpql.impl.model;

import java.util.List;

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

    public void addSingleValueAttribute(Class aClass, String name) {
    }

    public Attribute getAttributeByName(String attributeName) {
        return null;
    }

    public List<Attribute> findAttributesStartingWith(String fieldNamePattern) {
        return null;
    }

    public void addReferenceAttribute(String referencedEntityName, String name) {
    }

    public void addAttributeCopy(Attribute attribute) {
    }

    public static NoEntity getInstance() {
        return instance;
    }

    public void addCollectionReferenceAttribute(String referencedEntityName, String name) {
    }

    public void addSingleValueAttribute(Class aClass, String name, String userFriendlyName) {
    }

    public void addReferenceAttribute(String referencedEntityName, String name, String userFriendlyName) {
    }

    public void addCollectionReferenceAttribute(String referencedEntityName, String name, String userFriendlyName) {
    }
}
