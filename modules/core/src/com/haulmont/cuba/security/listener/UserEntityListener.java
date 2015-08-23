/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.listener;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.cuba.security.entity.User;

import javax.annotation.ManagedBean;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_UserEntityListener")
public class UserEntityListener implements
        BeforeInsertEntityListener<User>,
        BeforeUpdateEntityListener<User> {
    /**
     * @param entity updated entity
     */
    @Override
    public void onBeforeInsert(User entity) {
        if (PersistenceHelper.isLoaded(entity, "login")) {
            entity.setLoginLowerCase(entity.getLogin() != null ? entity.getLogin().toLowerCase() : null);
        }
    }

    /**
     * @param entity updated entity
     */
    @Override
    public void onBeforeUpdate(User entity) {
        if (PersistenceHelper.isLoaded(entity, "login")) {
            entity.setLoginLowerCase(entity.getLogin() != null ? entity.getLogin().toLowerCase() : null);
        }
    }
}