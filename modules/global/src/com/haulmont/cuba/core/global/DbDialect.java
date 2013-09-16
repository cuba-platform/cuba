/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import java.io.Serializable;

/**
 * Class that encapsulates some database-specific persistence properties.
 *
 * @author krivopustov
 * @version $Id$
 *
 * @see HsqlDbDialect
 * @see PostgresDbDialect
 * @see MssqlDbDialect
 * @see OracleDbDialect
 */
public abstract class DbDialect implements Serializable {

    public static final String DBMS_HSQL = "hsql";
    public static final String DBMS_POSTGRES = "postgres";
    public static final String DBMS_MSSQL = "mssql";
    public static final String DBMS_ORACLE = "oracle";

    /**
     * @return  the DBMS type ID
     * @see #DBMS_HSQL
     * @see #DBMS_POSTGRES
     * @see #DBMS_MSSQL
     * @see #DBMS_ORACLE
     */
    public abstract String getDbmsType();

    /**
     * @return  primary key column name
     */
    public abstract String getIdColumn();

    /**
     * @return  soft deletion column name
     */
    public abstract String getDeleteTsColumn();

    /**
     * @return  regexp to extract a unique constraint name from an exception message
     */
    public abstract String getUniqueConstraintViolationPattern();
}