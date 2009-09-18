/*
 * Author: Konstantin Krivopustov
 * Created: 15.05.2009 22:13:42
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.DbDialect;
import com.haulmont.cuba.core.global.SequenceSupport;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;

/**
 * UniqueNumbers MBean implementation.
 * <p>
 * Provides unique numbers based on database sequences.
 */
public class UniqueNumbers
        extends ManagementBean implements UniqueNumbersMBean, UniqueNumbersAPI
{
    private Set<String> existingSequences = Collections.synchronizedSet(new HashSet<String>());

    public long getNextNumber(String domain) {
        String seqName = getSequenceName(domain);
        Transaction tx = Locator.getTransaction();
        try {
            checkSequenceExists(seqName);

            EntityManager em = PersistenceProvider.getEntityManager();
            SequenceSupport support = getSequenceSqlProvider();
            Query query = em.createNativeQuery(support.getNextValueSql(seqName));
            Object value = query.getSingleResult();

            tx.commit();
            return (Long) value;
        } finally {
            tx.end();
        }
    }

    public UniqueNumbersAPI getAPI() {
        return this;
    }

    public long getCurrentNumber(String domain) {
        String seqName = getSequenceName(domain);
        Transaction tx = Locator.getTransaction();
        try {
            checkSequenceExists(seqName);

            EntityManager em = PersistenceProvider.getEntityManager();
            SequenceSupport support = getSequenceSqlProvider();
            Query query = em.createNativeQuery(support.getCurrentValueSql(seqName));
            Object value = query.getSingleResult();

            tx.commit();
            if (value instanceof Long)
                return (Long) value;
            else if (value instanceof String)
                return Long.valueOf((String) value);
            else
                throw new IllegalStateException("Unsupported value type: " + value.getClass());
        } finally {
            tx.end();
        }
    }

    public void setCurrentNumber(String domain, long value) {
        String seqName = getSequenceName(domain);

        Transaction tx = Locator.getTransaction();
        try {
            checkSequenceExists(seqName);

            EntityManager em = PersistenceProvider.getEntityManager();
            SequenceSupport support = getSequenceSqlProvider();
            String sql = support.modifySequenceSql(seqName, value);
            Query query = em.createNativeQuery(sql);
            if (sql.startsWith("select"))
                query.getResultList();
            else
                query.executeUpdate();
            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void checkSequenceExists(String seqName) {
        if (existingSequences.contains(seqName))
            return;

        EntityManager em = PersistenceProvider.getEntityManager();

        SequenceSupport support = getSequenceSqlProvider();
        Query query = em.createNativeQuery(support.sequenceExistsSql(seqName));
        List list = query.getResultList();
        if (list.isEmpty()) {
            query = em.createNativeQuery(support.createSequenceSql(seqName, 1, 1));
            query.executeUpdate();
        }
        existingSequences.add(seqName);
    }

    private String getSequenceName(String domain) {
        return "seq_un_" + domain;
    }

    private SequenceSupport getSequenceSqlProvider() {
        DbDialect dialect = PersistenceProvider.getDbDialect();
        if (dialect instanceof SequenceSupport)
            return (SequenceSupport) dialect;
        else
            throw new UnsupportedOperationException("DB sequences not supported");
    }
}
