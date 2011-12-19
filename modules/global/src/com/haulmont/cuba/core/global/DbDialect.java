/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

import java.io.Serializable;

/**
 * Class that encapsulates some database-specific persistence properties.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 *
 * @see HsqlDbDialect
 * @see PostgresDbDialect
 * @see MssqlDbDialect
 */
public abstract class DbDialect implements Serializable
{
    /**
     * @return  the dialect name
     */
    public abstract String getName();

    /**
     * @return  primary key column name
     */
    public abstract String getIdColumn();

    /**
     * @return  soft deletion column name
     */
    public abstract String getDeleteTsColumn();

    /**
     * @return  a message to distinguish a unique constraint violation
     */
    public abstract String getUniqueConstraintViolationMarker();

    /**
     * @return  regexp to extract a unique constraint name from an exception message
     */
    public abstract String getUniqueConstraintViolationPattern();

    /**
     * @return  character to separate SQL statements in scripts
     */
    public abstract String getScriptSeparator();
}
