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
package com.haulmont.cuba.core.global;


import javax.annotation.Nullable;
import java.util.Set;

/**
 * Parses JPQL query and returns some information about it.
 *
 */
public interface QueryParser {
    String NAME = "cuba_QueryParser";

    /** Get all parameter names */
    Set<String> getParamNames();

    /** Main entity name */
    String getEntityName();

    /** Main entity alias */
    String getEntityAlias(String targetEntity);

    String getEntityAlias();

    /** Returns true if this is a standard select from an entity - not count() and not fields (e.id, etc.) */
    boolean isEntitySelect(String targetEntity);

    boolean hasIsNullCondition(String attribute);

    /**
     * @return Entity name if not main entity name is returned, otherwise null
     * Example: select u.group from sec$User u -> sec$Group
     * Example: select g from sec$User u join u.group g -> sec$Group
     */
    @Nullable
    String getEntityNameIfSecondaryReturnedInsteadOfMain();
}
