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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.UuidProvider;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.platform.database.MySQLPlatform;
import org.eclipse.persistence.platform.database.OraclePlatform;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.platform.database.SQLServerPlatform;
import org.eclipse.persistence.sessions.Session;

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
        } else if (session.getPlatform() instanceof OraclePlatform
                || session.getPlatform() instanceof MySQLPlatform) {
            return objectValue != null ? objectValue.toString().replace("-", "") : null;
        } else {
            return objectValue != null ? objectValue.toString() : null;
        }
    }

    @Override
    public Object convertDataValueToObjectValue(Object dataValue, Session session) {
        try {
            if (session.getPlatform() instanceof PostgreSQLPlatform) {
                return dataValue;
            } else if (session.getPlatform() instanceof OraclePlatform
                    || session.getPlatform() instanceof MySQLPlatform) {
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
        } catch (Exception e) {
            throw new RuntimeException("Error creating UUID from database value '" + dataValue + "'", e);
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