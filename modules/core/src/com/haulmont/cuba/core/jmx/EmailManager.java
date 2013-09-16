/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.EmailerConfig;
import com.haulmont.cuba.core.global.Configuration;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_EmailManagerMBean")
public class EmailManager implements EmailManagerMBean {

    protected EmailerConfig config;

    @Inject
    public void setConfig(Configuration configuration) {
        this.config = configuration.getConfig(EmailerConfig.class);
    }

    @Override
    public int getDelayCallCount() {
        return config.getDelayCallCount();
    }

    @Override
    public int getMessageQueueCapacity() {
        return config.getMessageQueueCapacity();
    }
}
