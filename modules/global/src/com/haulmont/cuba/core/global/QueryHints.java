/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.core.global;

/**
 * The class defines CUBA query hints.
 * These query hints allow a JPA Query to be customized or optimized beyond
 * what is available in the JPA specification
 * <p>Query Hint Usage:
 *
 * <p><code>query.setHint(QueryHints.SQL_HINT, "OPTION(RECOMPILE)");</code>
 * <p>or
 * <p><code>query.setHint(QueryHints.MSSQL_RECOMPILE_HINT, true);</code>
 */
public interface QueryHints {
    /**
     * Sets a SQL hint string into the query that will be generated into the SQL statement.
     * A SQL hint can be used on certain database platforms to define how the query uses indexes
     * and other such low level usages.
     */
    String SQL_HINT = "sql.hint";

    /**
     * Adds <code>OPTION(RECOMPILE)</code> SQL hint for MSSQL database. Hint value is ignored.
     */
    String MSSQL_RECOMPILE_HINT = "mssql.recompile";
}
