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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.sys.jdbc.ProxyDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.lang.NonNull;

import javax.naming.NamingException;
import javax.sql.DataSource;

public class CubaDataSourceFactoryBean extends CubaJndiObjectFactoryBean {

    protected static final String DATASOURCE_PROVIDER_PROPERTY_NAME = "cuba.dataSourceProvider";

    private String storeName;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public Class<DataSource> getObjectType() {
        return DataSource.class;
    }

    @Override
    public Object getObject() {
        String dataSourceProvider = getDataSourceProvider();
        if (dataSourceProvider == null || "jndi".equals(dataSourceProvider)) {
            return super.getObject();
        }
        if ("application".equals(dataSourceProvider)) {
            ApplicationDataSourceInitialization applicationDataSourceInitialization = new ApplicationDataSourceInitialization();
            return applicationDataSourceInitialization.getApplicationDataSource(storeName);
        }
        throw new RuntimeException(String.format("DataSource provider '%s' is unsupported! Available: 'jndi', 'application'", dataSourceProvider));
    }

    @Override
    @NonNull
    protected Object lookupWithFallback() throws NamingException {
        String dataSourceProvider = getDataSourceProvider();
        Object object = new HikariDataSource();
        if (dataSourceProvider == null || "jndi".equals(dataSourceProvider)) {
            object = super.lookupWithFallback();
            if (object instanceof DataSource) {
                return new ProxyDataSource((DataSource) object);
            }
        }
        return object;
    }

    protected String getDataSourceProvider() {
        return AppContext.getProperty(getDsProviderPropertyName());
    }

    protected String getDsProviderPropertyName() {
        if (storeName != null) {
            return DATASOURCE_PROVIDER_PROPERTY_NAME + "_" + storeName;
        }
        return DATASOURCE_PROVIDER_PROPERTY_NAME;
    }
}
