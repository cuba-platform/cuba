/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.core.sys.connectionpoolinfo;

import java.util.regex.Pattern;

public class CommonsConnectionPoolInfo extends AbstractConnectionPoolInfo {

    @Override
    public String getPoolName() {
        return "Commons Connection Pool";
    }

    @Override
    public Pattern getRegexPattern() {
        String usualDsRegexp = String.format(
                "Catalina:type=DataSource,host=[\\w\\d]+,.*,class=javax.sql.DataSource,name=\".*%s\"",
                getMainDatasourceName()
        );
        return Pattern.compile(usualDsRegexp);
    }

    @Override
    public String getActiveConnectionsAttrName() {
        return "numActive";
    }

    @Override
    public String getIdleConnectionsAttrName() {
        return "numIdle";
    }

    @Override
    public String getTotalConnectionsAttrName() {
        return "maxTotal";
    }
}
