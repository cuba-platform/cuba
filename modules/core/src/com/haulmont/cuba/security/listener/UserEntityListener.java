/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.security.listener;

import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.cuba.security.entity.User;

/**
 * @author krivopustov
 * @version $Id$
 */
@SuppressWarnings({"UnusedDeclaration"})
public class UserEntityListener implements
        BeforeInsertEntityListener<User>,
        BeforeUpdateEntityListener<User> {
    /**
     * @param entity updated entity
     */
    @Override
    public void onBeforeInsert(User entity) {
        entity.setLoginLowerCase(entity.getLogin() != null ? entity.getLogin().toLowerCase() : null);
    }

    /**
     * @param entity updated entity
     */
    @Override
    public void onBeforeUpdate(User entity) {
        entity.setLoginLowerCase(entity.getLogin() != null ? entity.getLogin().toLowerCase() : null);
    }
}
