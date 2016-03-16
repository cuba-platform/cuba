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

package com.haulmont.cuba.core.sys.persistence;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 */
@SuppressWarnings("UnusedDeclaration")
public class HsqlDbmsFeatures implements DbmsFeatures {

    @Override
    public Map<String, String> getJpaParameters() {
        HashMap<String, String> params = new HashMap<>();
        params.put("eclipselink.target-database", "org.eclipse.persistence.platform.database.HSQLPlatform");
        return params;
    }

    @Override
    public String getIdColumn() {
        return "ID";
    }

    @Override
    public String getDeleteTsColumn() {
        return "DELETE_TS";
    }

    @Override
    public String getTimeStampType() {
        return "timestamp";
    }

    @Nullable
    @Override
    public String getUuidTypeClassName() {
        return null;
    }

    @Nullable
    @Override
    public String getTransactionTimeoutStatement() {
        return null;
    }

    @Override
    public String getUniqueConstraintViolationPattern() {
        return "integrity constraint violation: unique constraint or index violation: ([^\\s]+)";
    }

    @Override
    public boolean isNullsLastSorting() {
        return false;
    }
}
