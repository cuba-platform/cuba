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
 */

package com.haulmont.cuba.core.sys.dbupdate;

import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * link on docs for jdbc url:
 * oracle : http://docs.oracle.com/cd/B28359_01/java.111/b31224/urls.htm#i1058725
 * mssql: https://msdn.microsoft.com/ru-ru/library/ms378428(v=sql.110).aspx, http://jtds.sourceforge.net/faq.html
 * hsql: http://hsqldb.org/doc/guide/dbproperties-chapt.html
 * postgres: https://jdbc.postgresql.org/documentation/head/connect.html
 */
public class DbProperties {

    protected String url;

    public DbProperties(String url) {
        this.url = url;
    }

    public Map<String, String> getProperties() {
        String connectionParams = getConnectionParams();
        if (StringUtils.isNotEmpty(connectionParams)) {
            Map<String, String> result = new HashMap<>();
            if (connectionParams.startsWith("?")) {
                connectionParams = connectionParams.substring(1);
            }
            for (String param : connectionParams.split("[&,;]")) {
                int index = param.indexOf("=");
                if (index > 0) {
                    String key = param.substring(0, index);
                    String value = param.substring(index + 1);
                    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                        result.put(key.trim(), value.trim());
                    }
                }
            }
            return result;
        }
        return null;
    }

    public String getCurrentSchemaProperty() {
        Map<String, String> properties = getProperties();
        if (properties != null) {
            String currentSchema = properties.get("currentSchema");
            return StringUtils.isNotEmpty(currentSchema) ? currentSchema.replace("\"", StringUtils.EMPTY) : null;
        }
        return null;
    }

    @Nullable
    public String getConnectionParams() {
        if (url == null) {
            return null;
        }
        int slash = url.lastIndexOf("/");
        if (slash <= 0) {
            return null;
        }
        Matcher m = Pattern.compile("[^\\?;,\\\\]*([\\?;,\\\\].*)").matcher(url.substring(slash + 1));
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
}
