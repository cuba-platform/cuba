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

import org.springframework.jndi.JndiObjectFactoryBean;

/**
 * This class is used for locating objects by name defined in application properties before
 * {@link CubaPropertyPlaceholderConfigurer} comes into play.
 * <p>
 * Example: you need to use {@code org.mybatis.spring.mapper.MapperScannerConfigurer}, which is a
 * BeanDefinitionRegistryPostProcessor and runs before CubaPropertyPlaceholderConfigurer, but depends on DataSource,
 * which is configured through application properties.
 *
 */
public class CubaJndiObjectFactoryBean extends JndiObjectFactoryBean {

    private String jndiNameAppProperty;

    public String getJndiNameAppProperty() {
        return jndiNameAppProperty;
    }

    public void setJndiNameAppProperty(String jndiNameAppProperty) {
        this.jndiNameAppProperty = jndiNameAppProperty;
        setJndiName(AppContext.getProperty(jndiNameAppProperty));
    }
}
