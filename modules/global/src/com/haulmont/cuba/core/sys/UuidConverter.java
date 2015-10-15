/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.UuidProvider;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.platform.database.OraclePlatform;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.platform.database.SQLServerPlatform;
import org.eclipse.persistence.sessions.Session;

/**
 * @author krivopustov
 * @version $Id$
 */
public class UuidConverter implements Converter {

    private final static UuidConverter INSTANCE = new UuidConverter();

    public static UuidConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Object convertObjectValueToDataValue(Object objectValue, Session session) {
        if (session.getPlatform() instanceof PostgreSQLPlatform) {
            return objectValue;
        } else if (session.getPlatform() instanceof SQLServerPlatform) {
            return objectValue != null ? objectValue.toString().toUpperCase() : null; // for correct binding of batch query results
        } else if (session.getPlatform() instanceof OraclePlatform) {
            return objectValue != null ? objectValue.toString().replace("-", "") : null;
        } else {
            return objectValue != null ? objectValue.toString() : null;
        }
    }

    @Override
    public Object convertDataValueToObjectValue(Object dataValue, Session session) {
        if (session.getPlatform() instanceof PostgreSQLPlatform) {
            return dataValue;
        } else if (session.getPlatform() instanceof OraclePlatform) {
            if (dataValue instanceof String) {
                StringBuilder sb = new StringBuilder((String) dataValue);
                sb.insert(8, '-');
                sb.insert(13, '-');
                sb.insert(18, '-');
                sb.insert(23, '-');
                return UuidProvider.fromString(sb.toString());
            } else {
                return dataValue;
            }
        } else {
            return dataValue instanceof String ? UuidProvider.fromString((String) dataValue) : dataValue;
        }
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public void initialize(DatabaseMapping mapping, Session session) {
    }
}
