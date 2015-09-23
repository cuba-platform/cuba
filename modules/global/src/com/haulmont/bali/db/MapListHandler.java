/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.bali.db;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author krivopustov
 * @version $Id$
 */
public class MapListHandler implements ResultSetHandler<List<Map<String, Object>>> {
    /**
     * The RowProcessor implementation to use when converting rows
     * into Maps.
     */
    private RowProcessor convert = ArrayHandler.ROW_PROCESSOR;

    public MapListHandler() {
    }

    public MapListHandler(RowProcessor convert) {
        this.convert = convert;
    }

    @Override
    public List<Map<String, Object>> handle(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        while (rs.next()) {
            result.add(convert.toMap(rs));
        }
        return result;
    }
}