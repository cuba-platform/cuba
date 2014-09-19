/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.Server;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_TestListenerBean")
public class TestListenerBean implements
        AfterInsertEntityListener<Server>,
        AfterUpdateEntityListener<Server>,
        AfterDeleteEntityListener<Server> {

    @Inject
    private Persistence persistence;

    @Override
    public void onAfterDelete(Server entity) {
        if (persistence == null)
            throw new IllegalStateException("Injected value is null");
        System.out.println("TestListenerBean: onAfterDelete " + entity);
    }

    @Override
    public void onAfterInsert(Server entity) {
        if (persistence == null)
            throw new IllegalStateException("Injected value is null");
        System.out.println("TestListenerBean: onAfterInsert " + entity);
    }

    @Override
    public void onAfterUpdate(Server entity) {
        if (persistence == null)
            throw new IllegalStateException("Injected value is null");
        System.out.println("TestListenerBean: onAfterUpdate " + entity);
    }
}
