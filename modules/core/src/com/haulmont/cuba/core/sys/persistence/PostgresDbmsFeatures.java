/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
@SuppressWarnings("UnusedDeclaration")
public class PostgresDbmsFeatures implements DbmsFeatures {

    @Override
    public Map<String, String> getJpaParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("openjpa.jdbc.DBDictionary",
                "com.haulmont.cuba.core.sys.persistence.CubaPostgresDictionary(RequiresCastForComparisons=true)");
        params.put("openjpa.jdbc.MappingDefaults",
                "FieldStrategies='java.util.UUID=com.haulmont.cuba.core.sys.persistence.UuidPostgresValueHandler'");
        return params;
    }

    @Override
    public String getIdColumn() {
        return "id";
    }

    @Override
    public String getDeleteTsColumn() {
        return "delete_ts";
    }

    @Override
    public String getTimeStampType() {
        return "timestamp";
    }

    @Nullable
    @Override
    public String getUuidTypeClassName() {
        return "com.haulmont.cuba.core.sys.persistence.PostgresUUID";
    }

    @Nullable
    @Override
    public String getTransactionTimeoutStatement() {
        return "set local statement_timeout to %d";
    }

    @Override
    public String getUniqueConstraintViolationPattern() {
        return "ERROR: duplicate key value violates unique constraint \"(.+)\"";
    }

    @Override
    public boolean isNullsLastSorting() {
        return true;
    }
}
