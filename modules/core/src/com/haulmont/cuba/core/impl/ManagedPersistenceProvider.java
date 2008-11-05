/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.11.2008 13:24:28
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAPersistence;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.*;
import java.util.Hashtable;
import java.util.Map;

import com.haulmont.cuba.core.impl.EntityManagerAdapterImpl;
import com.haulmont.cuba.core.impl.EntityManagerFactoryAdapterImpl;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.EntityManagerFactoryAdapter;
import com.haulmont.cuba.core.EntityManagerAdapter;
import com.haulmont.cuba.core.CubaProperties;

public class ManagedPersistenceProvider implements PersistenceProvider
{
    private Context jndiContext;

    private final Object mutex = new Object();

    private boolean emfInitialized;

    private Map<Transaction, EntityManagerAdapterImpl> emMap = new Hashtable<Transaction, EntityManagerAdapterImpl>();

    public static final String EMF_JNDI_NAME = "EntityManagerFactoryAdapterImpl";

    public static final String TM_JNDI_NAME = "java:/TransactionManager";

    private Log log = LogFactory.getLog(ManagedPersistenceProvider.class);

    ManagedPersistenceProvider(Context jndiContext) {
        this.jndiContext = jndiContext;
    }

    public EntityManagerFactoryAdapter getEntityManagerFactory() {
        synchronized (mutex) {
            if (!emfInitialized) {
                log.debug("Creating new EntityManagerFactory");

                String xmlPath = System.getProperty(CubaProperties.PERSISTENCE_XML);
                if (StringUtils.isBlank(xmlPath))
                    xmlPath = "META-INF/cuba-persistence.xml";
                String unitName = System.getProperty(CubaProperties.PERSISTENCE_UNIT);
                if (StringUtils.isBlank(unitName))
                    unitName = "cuba";
                log.debug(String.format("Using persistence unit %s from %s", unitName, xmlPath));

                OpenJPAEntityManagerFactory jpaFactory =
                        OpenJPAPersistence.createEntityManagerFactory(unitName, xmlPath);

                EntityManagerFactoryAdapter emf = new EntityManagerFactoryAdapterImpl(jpaFactory);
                try {
                    log.debug("Binding new EntityManagerFactory to JNDI context " + EMF_JNDI_NAME);
                    jndiContext.bind(EMF_JNDI_NAME, emf);
                } catch (NamingException e) {
                    throw new RuntimeException(e);
                }
                emfInitialized = true;
            }
        }
        try {
            return (EntityManagerFactoryAdapter) jndiContext.lookup(EMF_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public EntityManagerAdapter getEntityManager() {
        EntityManagerAdapterImpl em;
        try {
            TransactionManager tm = getTransactionManager();
            Transaction tx = tm.getTransaction();
            if (tx != null) {
                em = emMap.get(tx);
                if (em == null) {
                    log.trace("Creating new EntityManager for transaction " + tx);
                    em = getEntityManagerFactory().createEntityManager();
                    registerSync(tx, em);
                    emMap.put(tx, em);
                }
            }
            else {
                log.trace("Creating new non-transactional EntityManager");
                em = getEntityManagerFactory().createEntityManager();
            }
            return em;
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerSync(final javax.transaction.Transaction tx, final EntityManagerAdapter em) {
        try {
            tx.registerSynchronization(
                    new Synchronization()
                    {
                        public void beforeCompletion() {
                            log.trace("Closing EntityManager for transaction " + tx);
                            em.close();
                        }

                        public void afterCompletion(int i) {
                            emMap.remove(tx);
                        }
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException("Unable to register synchronization with JTA transaction", e);
        }
    }

    private TransactionManager getTransactionManager() throws NamingException {
        return (TransactionManager) jndiContext.lookup(TM_JNDI_NAME);
    }
}
