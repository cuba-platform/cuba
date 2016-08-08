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

import java.util.List;
import java.util.Set;

public class NoJpqlEntityModel implements JpqlEntityModel {
    public static final NoJpqlEntityModel instance = new NoJpqlEntityModel();

    private NoJpqlEntityModel() {
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

    public static NoJpqlEntityModel getInstance() {
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