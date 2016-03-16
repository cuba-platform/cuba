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

    void addReferenceAttribute(String referencedEntityName, String name, String userFriendlyName, boolean isEmbedded);

    void addCollectionReferenceAttribute(String referencedEntityName, String name, String userFriendlyName);
}
