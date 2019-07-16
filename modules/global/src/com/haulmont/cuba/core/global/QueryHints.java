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
 * Defines a set of CUBA query hints which enable customization of generated SQL statements.
 *
 * <p>Usage examples:
 * <pre>
 *     query.setHint(QueryHints.SQL_HINT, "OPTION(RECOMPILE)");
 *     query.setHint(QueryHints.MSSQL_RECOMPILE_HINT, true);
 * </pre>
 */
public interface QueryHints {
    /**
     * Adds an SQL hint string after the SQL statement.
     * <p>The SQL hint can be used on certain database platforms to define how the query uses indexes
     * and other such low level usages. It should be the full hint string including the comment delimiters.
     * <p>Corresponds to {@code org.eclipse.persistence.config.QueryHints#HINT}
     */
    String SQL_HINT = "sql.hint";

    /**
     * Adds <code>OPTION(RECOMPILE)</code> SQL hint for MSSQL database. Hint value is ignored.
     */
    String MSSQL_RECOMPILE_HINT = "mssql.recompile";
}
