/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author artamonov
 * @version $Id$
 */
public interface DbTypeConverter {

    Object getJavaObject(ResultSet resultSet, int column) throws SQLException;
}
