/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys.jpql.model;

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

    @Override
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