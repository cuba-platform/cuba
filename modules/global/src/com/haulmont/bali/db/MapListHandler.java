/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.03.2009 18:14:54
 *
 * $Id: MapListHandler.java 3028 2010-11-09 08:12:36Z krivopustov $
 */
package com.haulmont.bali.db;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MapListHandler implements ResultSetHandler<List<Map<String, Object>>>
{
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

    public List<Map<String, Object>> handle(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            result.add(convert.toMap(rs));
        }
        return result;
    }
}
