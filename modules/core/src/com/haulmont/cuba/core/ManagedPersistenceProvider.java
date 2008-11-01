/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.11.2008 13:24:28
 * $Id$
 */
package com.haulmont.cuba.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAPersistence;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.*;
import java.util.Hashtable;
import java.util.Map;

public class ManagedPersistenceProvider implements PersistenceProvider
{
    private Context jndiContext;

    private final Object mutex = new Object();

    private boolean emfInitialized;

    private Map<Transaction, CubaEntityManager> emMap = new Hashtable<Transaction, CubaEntityManager>();

    public static final String EMF_JNDI_NAME = "CubaEntityManagerFactory";

    public static final String TM_JNDI_NAME = "java:/TransactionManager";

    private Log log = LogFactory.getLog(ManagedPersistenceProvider.class);

    ManagedPersistenceProvider(Context jndiContext) {
        this.jndiContext = jndiContext;
    }

    public CubaEntityManagerFactory getEntityManagerFactory() {
        synchronized (mutex) {
            if (!emfInitialized) {
                log.debug("Create new EntityManagerFactory");
                OpenJPAEntityManagerFactory jpaFactory =
                        OpenJPAPersistence.createEntityManagerFactory("cuba", "META-INF/cuba-persistence.xml");
                CubaEntityManagerFactory emf = new CubaEntityManagerFactory(jpaFactory);
                try {
                    log.debug("Bind new EntityManagerFactory to JNDI context " + EMF_JNDI_NAME);
                    jndiContext.bind(EMF_JNDI_NAME, emf);
                } catch (NamingException e) {
                    throw new RuntimeException(e);
                }
                emfInitialized = true;
            }
        }
        try {
            return (CubaEntityManagerFactory) jndiContext.lookup(EMF_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public CubaEntityManager getEntityManager() {
        return getEntityManager(true);
    }

    public CubaEntityManager getEntityManager(boolean transactional) {
        CubaEntityManager em;
        try {
            TransactionManager tm = getTransactionManager();
            Transaction tx = tm.getTransaction();
            if (transactional) {
                if (tx == null) {
                    log.trace("Begin new transaction");
                    tm.begin();
                    tx = tm.getTransaction();
                }
                em = emMap.get(tx);
                if (em == null) {
                    log.trace("Create new EntityManager for transaction " + tx);
                    em = getEntityManagerFactory().createEntityManager();
                    registerSync(tx, em);
                    emMap.put(tx, em);
                }
            }
            else {
                if (tx != null)
                    throw new RuntimeException("Unable to get non-transactional EntityManager: JTA transaction exists");
                em = getEntityManagerFactory().createEntityManager();
            }
            return em;
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        } catch (NotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerSync(final javax.transaction.Transaction tx, final CubaEntityManager em) {
        try {
            tx.registerSynchronization(
                    new Synchronization()
                    {
                        public void beforeCompletion() {
                            log.trace("Close EntityManager for transaction " + tx);
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
