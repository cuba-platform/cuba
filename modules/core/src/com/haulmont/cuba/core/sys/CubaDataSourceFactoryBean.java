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

import com.haulmont.cuba.core.global.Stores;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

import javax.sql.DataSource;

public class CubaDataSourceFactoryBean implements FactoryBean<Object>, InitializingBean, BeanFactoryAware, DisposableBean {

    protected String storeName;
    protected String jndiNameAppProperty;

    protected DataSource dataSource;
    protected DataSourceProvider dataSourceProvider;
    protected BeanFactory beanFactory;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setJndiNameAppProperty(String jndiNameAppProperty) {
        this.jndiNameAppProperty = jndiNameAppProperty;
    }

    @Override
    public Class<DataSource> getObjectType() {
        return DataSource.class;
    }

    @Override
    public void afterPropertiesSet() throws IllegalArgumentException {
        try {
            this.dataSourceProvider = beanFactory.getBean(DataSourceProvider.class);
        } catch (NoSuchBeanDefinitionException e) {
            this.dataSourceProvider = new DataSourceProvider();
        }
        this.storeName = storeName == null ? Stores.MAIN : storeName;
        this.dataSource = dataSourceProvider.getDataSource(storeName, AppContext.getProperty(jndiNameAppProperty));
    }

    @Override
    public Object getObject() {
        return dataSource;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void destroy() throws Exception {
        if (dataSource != null) {
            dataSourceProvider.closeDataSource(dataSource);
        }
    }
}
