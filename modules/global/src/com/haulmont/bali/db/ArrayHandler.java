/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.bali.db;

import javax.annotation.concurrent.ThreadSafe;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <code>ResultSetHandler</code> implementation that converts a
 * <code>ResultSet</code> into an <code>Object[]</code>. This class is
 * thread safe.
 *
 */
@ThreadSafe
public class ArrayHandler implements ResultSetHandler<Object[]> {

    /**
     * Singleton processor instance that handlers share to save memory.  Notice
     * the default scoping to allow only classes in this package to use this
     * instance.
     */
    static final RowProcessor ROW_PROCESSOR = new BasicRowProcessor();

    /**
     * The RowProcessor implementation to use when converting rows
     * into arrays.
     */
    private RowProcessor convert = ROW_PROCESSOR;

    /**
     * Creates a new instance of ArrayHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public ArrayHandler() {
        super();
    }

    /**
     * Creates a new instance of ArrayHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into arrays.
     */
    public ArrayHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Places the column values from the first row in an <code>Object[]</code>.
     *
     * @return An Object[] or <code>null</code> if there are no rows in the
     * <code>ResultSet</code>.
     *
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toArray(rs) : null;
    }
}