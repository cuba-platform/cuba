/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.bali.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
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

    @Override
    public List<Object[]> handle(ResultSet rs) throws SQLException {
        List<Object[]> result = new ArrayList<>();
        while (rs.next()) {
            result.add(convert.toArray(rs));
        }
        return result;
    }
}