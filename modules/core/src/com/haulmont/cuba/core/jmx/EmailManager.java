/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.EmailManagerAPI;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_EmailManagerMBean")
public class EmailManager implements EmailManagerMBean {

    @Inject
    protected EmailManagerAPI emailManager;

    @Override
    public int getDelayCallCount() {
        return emailManager.getDelayCallCount();
    }

    @Override
    public int getMessageQueueCapacity() {
        return emailManager.getMessageQueueCapacity();
    }
}
