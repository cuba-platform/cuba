/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 02.12.2009 17:47:12
 *
 * $Id: ListArrayHandler.java 3028 2010-11-09 08:12:36Z krivopustov $
 */
package com.haulmont.bali.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListArrayHandler implements ResultSetHandler<List<Object[]>> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Maps.
     */
    private RowProcessor convert;

    public ListArrayHandler() {
        convert = ArrayHandler.ROW_PROCESSOR;
    }

    public ListArrayHandler(RowProcessor convert) {
        this.convert = convert;
    }

    public List<Object[]> handle(ResultSet rs) throws SQLException {
        List<Object[]> result = new ArrayList<Object[]>();
        while (rs.next()) {
            result.add(convert.toArray(rs));
        }
        return result;
    }
}
