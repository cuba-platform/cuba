/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import org.apache.openjpa.jdbc.sql.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaHSQLDictionary extends HSQLDictionary {
    @Override
    public SQLBuffer toTraditionalJoin(Join join) {
        return DBDictionaryUtils.toTraditionalJoin(this, join);
    }

    @Override
    protected SQLBuffer getWhere(Select sel, boolean forUpdate) {
        return DBDictionaryUtils.getWhere(this, sel, forUpdate, true);
    }
}