/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
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
