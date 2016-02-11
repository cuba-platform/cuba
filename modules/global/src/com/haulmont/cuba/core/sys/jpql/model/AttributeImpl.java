/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.model;

/**
 * User: Alex Chevelev
 * Date: 13.10.2010
 * Time: 23:12:41
 */
public class AttributeImpl implements Attribute {
    private final Class simpleType;
    private final String name;
    private final String referencedEntityName;
    private boolean collection;
    private String userFriendlyName;
    private boolean isEmbedded;

    public AttributeImpl(Class simpleType, String name) {
        this.simpleType = simpleType;
        this.name = name;
        referencedEntityName = null;
    }

    public AttributeImpl(String referencedEntityName, String name, boolean isCollection) {
        collection = isCollection;
        this.simpleType = null;
        this.name = name;
        this.referencedEntityName = referencedEntityName;
    }

    @Override
    public Class getSimpleType() {
        if (simpleType == null)
            throw new IllegalStateException("Not a simpletype attribute");

        return simpleType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEntityReferenceAttribute() {
        return referencedEntityName != null;
    }

    @Override
    public boolean isCollection() {
        return collection;
    }

    @Override
    public String getReferencedEntityName() {
        if (referencedEntityName == null)
            throw new IllegalStateException("Not a referenced entity attribute");

        return referencedEntityName;
    }

    @Override
    public String getUserFriendlyName() {
        return userFriendlyName;
    }

    public void setUserFriendlyName(String userFriendlyName) {
        this.userFriendlyName = userFriendlyName;
    }

    public boolean isEmbedded() {
        return isEmbedded;
    }

    public void setEmbedded(boolean embedded) {
        isEmbedded = embedded;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}