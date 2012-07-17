/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.03.2009 18:11:55
 *
 * $Id: MapHandler.java 3028 2010-11-09 08:12:36Z krivopustov $
 */
package com.haulmont.bali.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * <code>ResultSetHandler</code> implementation that converts the first
 * <code>ResultSet</code> row into a <code>Map</code>. This class is thread
 * safe.
 *
 */
public class MapHandler implements ResultSetHandler<Map<String, Object>> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Maps.
     */
    private RowProcessor convert = ArrayHandler.ROW_PROCESSOR;

    /**
     * Creates a new instance of MapHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public MapHandler() {
        super();
    }

    /**
     * Creates a new instance of MapHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into Maps.
     */
    public MapHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Converts the first row in the <code>ResultSet</code> into a
     * <code>Map</code>.
     *
     * @return A <code>Map</code> with the values from the first row or
     * <code>null</code> if there are no rows in the <code>ResultSet</code>.
     *
     * @throws SQLException if a database access error occurs
     *
     */
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toMap(rs) : null;
    }
}
