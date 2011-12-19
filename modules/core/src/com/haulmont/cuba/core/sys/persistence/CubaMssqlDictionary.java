/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.persistence;

import org.apache.openjpa.jdbc.sql.Join;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.jdbc.sql.SQLServerDictionary;
import org.apache.openjpa.jdbc.sql.Select;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class CubaMssqlDictionary extends SQLServerDictionary {

    public SQLBuffer toTraditionalJoin(Join join) {
        return DBDictionaryUtils.toTraditionalJoin(this, join);
    }

    protected SQLBuffer getWhere(Select sel, boolean forUpdate) {
        return DBDictionaryUtils.getWhere(this, sel, forUpdate, false);
    }
}
