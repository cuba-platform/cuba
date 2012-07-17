/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.03.2009 10:52:40
 *
 * $Id: ResultSetHandler.java 3028 2010-11-09 08:12:36Z krivopustov $
 */
package com.haulmont.bali.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementations of this interface convert ResultSets into other objects.
 */
public interface ResultSetHandler<T> {

    /**
     * Turn the <code>ResultSet</code> into an Object.
     *
     * @param rs The <code>ResultSet</code> to handle.  It has not been touched
     * before being passed to this method.
     *
     * @return An Object initialized with <code>ResultSet</code> data. It is
     * legal for implementations to return <code>null</code> if the
     * <code>ResultSet</code> contained 0 rows.
     *
     * @throws SQLException if a database access error occurs
     */
    public T handle(ResultSet rs) throws SQLException;

}