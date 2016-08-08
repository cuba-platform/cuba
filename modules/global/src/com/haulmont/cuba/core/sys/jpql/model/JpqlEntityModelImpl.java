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

import com.haulmont.cuba.core.sys.jpql.InferredType;

import java.util.*;

public class JpqlEntityModelImpl implements JpqlEntityModel {
    private String name;
    private List<String> attributeNames = new ArrayList<>();
    private Map<String, AttributeImpl> name2attribute = new HashMap<>();
    private String userFriendlyName;

    public JpqlEntityModelImpl(String name) {
        if (name == null)
            throw new NullPointerException("No entity name passed");

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUserFriendlyName() {
        return userFriendlyName;
    }

    public void setUserFriendlyName(String userFriendlyName) {
        this.userFriendlyName = userFriendlyName;
    }

    public void addSingleValueAttribute(Class aClass, String name) {
        addSingleValueAttribute(aClass, name, null);
    }

    @Override
    public void addSingleValueAttribute(Class aClass, String name, String userFriendlyName) {
        if (aClass == null)
            throw new NullPointerException("No attribute type passed");
        if (name == null)
            throw new NullPointerException("No attribute name passed");

        AttributeImpl attribute = new AttributeImpl(aClass, name);
        attribute.setUserFriendlyName(userFriendlyName);
        attributeNames.add(name);
        name2attribute.put(name, attribute);
    }

    @Override
    public AttributeImpl getAttributeByName(String attributeName) {
        return name2attribute.get(attributeName);
    }

    @Override
    public List<Attribute> findAttributesStartingWith(String fieldNamePattern, Set<InferredType> expectedTypes) {
        List<Attribute> result = new ArrayList<>();
        for (Map.Entry<String, AttributeImpl> entry : name2attribute.entrySet()) {
            if (entry.getKey().startsWith(fieldNamePattern)) {
                for (InferredType expectedType : expectedTypes) {
                    AttributeImpl attribute = entry.getValue();
                    if (expectedType.matches(attribute)) {
                        result.add(attribute);
                        break;
                    }
                }
            }
        }
        return result;
    }

    public void addReferenceAttribute(String referencedEntityName, String name) {
        addReferenceAttribute(referencedEntityName, name, null, false);
    }

    @Override
    public void addReferenceAttribute(String referencedEntityName, String name, String userFriendlyName, boolean isEmbedded) {
        if (referencedEntityName == null)
            throw new NullPointerException("No referencedEntityName passed");
        if (name == null)
            throw new NullPointerException("No attribute name passed");

        AttributeImpl attribute = new AttributeImpl(referencedEntityName, name, false);
        attribute.setUserFriendlyName(userFriendlyName);
        attribute.setEmbedded(isEmbedded);
        attributeNames.add(name);
        name2attribute.put(name, attribute);
    }

    public void addCollectionReferenceAttribute(String referencedEntityName, String name) {
        addCollectionReferenceAttribute(referencedEntityName, name, null);
    }

    @Override
    public void addCollectionReferenceAttribute(String referencedEntityName, String name, String userFriendlyName) {
        if (referencedEntityName == null)
            throw new NullPointerException("No referencedEntityName passed");
        if (name == null)
            throw new NullPointerException("No attribute name passed");

        AttributeImpl attribute = new AttributeImpl(referencedEntityName, name, true);
        attribute.setUserFriendlyName(userFriendlyName);
        attributeNames.add(name);
        name2attribute.put(name, attribute);
    }

    @Override
    public void addAttributeCopy(Attribute attribute) {
        if (attribute == null)
            throw new NullPointerException("No attribute passed");

        attributeNames.add(attribute.getName());
        try {
            name2attribute.put(attribute.getName(), (AttributeImpl) attribute.clone());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Unable to clone attribute", e);
        }
    }
}