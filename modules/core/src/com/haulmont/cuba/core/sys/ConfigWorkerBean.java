/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.01.2009 17:04:44
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.entity.Config;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Query;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@Stateless(name = ConfigWorker.JNDI_NAME)
public class ConfigWorkerBean implements ConfigWorker
{
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String getProperty(String name) {
        Config instance = getConfigInstance(name);
        return instance == null ? null : instance.getValue();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void setProperty(String name, String value) {
        EntityManager em = PersistenceProvider.getEntityManager();
        Config instance = getConfigInstance(name);
        if (instance == null) {
            instance = new Config();
            instance.setName(name);
            instance.setValue(value);
            em.persist(instance);
        }
        else {
            instance.setValue(value);
        }
    }

    private Config getConfigInstance(String name) {
        EntityManager em = PersistenceProvider.getEntityManager();
        Query query = em.createQuery("select c from core$Config c where c.name = ?1");
        query.setParameter(1, name);
        List<Config> list = query.getResultList();
        if (list.isEmpty())
            return null;
        else
            return list.get(0);
    }
}
