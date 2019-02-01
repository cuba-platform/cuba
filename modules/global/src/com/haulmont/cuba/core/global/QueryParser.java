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
import java.util.List;
import java.util.Set;

/**
 * Parses JPQL query and returns some information about it.
 */
public interface QueryParser {
    String NAME = "cuba_QueryParser";

    /**
     * Get all parameter names
     */
    Set<String> getParamNames();

    /**
     * Main entity name
     */
    String getEntityName();

    Set<String> getAllEntityNames();

    /**
     * Main entity alias
     */
    String getEntityAlias(String targetEntity);

    String getEntityAlias();

    /**
     * Returns true if this is a standard select from an entity - not count() and not fields (e.id, etc.)
     */
    boolean isEntitySelect(String targetEntity);

    boolean hasIsNullCondition(String attribute);

    boolean hasIsNotNullCondition(String attribute);

    /**
     * Returns true if SELECT query contains joins
     */
    boolean isQueryWithJoins();

    /**
     * @return Entity name if not main entity name is returned, otherwise null
     * Example: select u.group from sec$User u -&gt; sec$Group
     * Example: select g from sec$User u join u.group g -&gt; sec$Group
     */
    @Nullable
    String getOriginalEntityName();

    /**
     * @return Entity path if not main entity name is returned, otherwise null
     * Example: select u.group from sec$User u -&gt; u.group
     * Example: select g from sec$User u join u.group g -&gt; g
     */
    @Nullable
    String getOriginalEntityPath();

    /**
     * @return true if not main entity selected and it's path with collection
     * Example: select u.group from sec$User u -&gt; false
     * Example: select u.userRoles from sec$User u -&gt; true
     */
    boolean isCollectionOriginalEntitySelect();

    boolean isParameterInCondition(String parameterName);

    List<QueryPath> getQueryPaths();

    class QueryPath {
        protected String entityName;
        protected String pathString;
        protected String variableName;
        protected boolean selectedPath;

        public QueryPath(String entityName, String variableName, String pathString, boolean selectedPath) {
            this.entityName = entityName;
            this.variableName = variableName;
            this.pathString = pathString;
            this.selectedPath = selectedPath;
        }

        public String getEntityName() {
            return entityName;
        }

        public String getFullPath() {
            return pathString;
        }

        public String getPropertyPath() {
            if (pathString.contains(".")) {
                return pathString.replace(variableName + ".", "");
            } else {
                return pathString;
            }
        }

        public String getVariableName() {
            return variableName;
        }

        public boolean isSelectedPath() {
            return selectedPath;
        }
    }
}