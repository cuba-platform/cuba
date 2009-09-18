/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.2009 11:36:02
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.ServiceInterceptor;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSetting;

import javax.ejb.*;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Service providing current user settings functionality:
 * an application can save/load some "setting" (plain or XML string) for current user.
 * <br>Ususally used by UI forms and components. 
 */
@Stateless(name = UserSettingService.JNDI_NAME)
@Interceptors({ServiceInterceptor.class})
@TransactionManagement(TransactionManagementType.BEAN)
public class UserSettingServiceBean implements UserSettingService
{
    public String loadSetting(String name) {
        return loadSetting(null, name);
    }

    public String loadSetting(ClientType clientType, String name) {
        String value;
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query q = em.createQuery(
                    "select s from sec$UserSetting s where s.user.id = ?1 and s.name =?2 and s.clientType = ?3");
            q.setParameter(1, SecurityProvider.currentUserId());
            q.setParameter(2, name);
            q.setParameter(3, clientType == null ? null : clientType.getId());
            q.setView(new View(UserSetting.class, false).addProperty("value"));

            List<UserSetting> list = q.getResultList();

            value = list.isEmpty() ? null : list.get(0).getValue();

            tx.commit();

        } finally {
            tx.end();
        }
        return value;
    }

    public void saveSetting(String name, String value) {
        saveSetting(null, name, value);
    }

    public void saveSetting(ClientType clientType, String name, String value) {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query q = em.createQuery(
                    "select s from sec$UserSetting s where s.user.id = ?1 and s.name =?2 and s.clientType = ?3");
            q.setParameter(1, SecurityProvider.currentUserId());
            q.setParameter(2, name);
            q.setParameter(3, clientType == null ? null : clientType.getId());
            q.setView(new View(UserSetting.class, false).addProperty("value"));

            List<UserSetting> list = q.getResultList();
            if (list.isEmpty()) {
                UserSetting us = new UserSetting();
                em.setView(new View(User.class, false));
                us.setUser(em.find(User.class, SecurityProvider.currentUserSession().getUser().getId()));
                us.setName(name);
                us.setClientType(clientType);
                us.setValue(value);
                em.persist(us);
            } else {
                UserSetting us = list.get(0);
                us.setValue(value);
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }
}
