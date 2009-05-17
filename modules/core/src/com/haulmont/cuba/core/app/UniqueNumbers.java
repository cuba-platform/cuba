/*
 * Author: Konstantin Krivopustov
 * Created: 15.05.2009 22:13:42
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.sys.persistence.SequenceSqlProvider;
import com.haulmont.cuba.core.sys.EntityManagerFactoryImpl;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.sql.DBDictionary;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;

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
            SequenceSqlProvider sqlProvider = getSequenceSqlProvider();
            Query query = em.createNativeQuery(sqlProvider.getNextValueSql(seqName));
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
            SequenceSqlProvider sqlProvider = getSequenceSqlProvider();
            Query query = em.createNativeQuery(sqlProvider.getCurrentValueSql(seqName));
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
            SequenceSqlProvider sqlProvider = getSequenceSqlProvider();
            String sql = sqlProvider.modifySequenceSql(seqName, value);
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

        SequenceSqlProvider sqlProvider = getSequenceSqlProvider();
        Query query = em.createNativeQuery(sqlProvider.sequenceExistsSql(seqName));
        List list = query.getResultList();
        if (list.isEmpty()) {
            query = em.createNativeQuery(sqlProvider.createSequenceSql(seqName, 1, 1));
            query.executeUpdate();
        }
        existingSequences.add(seqName);
    }

    private String getSequenceName(String domain) {
        return "seq_un_" + domain;
    }

    private SequenceSqlProvider getSequenceSqlProvider() {
        OpenJPAEntityManagerFactory factory = ((EntityManagerFactoryImpl) PersistenceProvider.getEntityManagerFactory()).getDelegate();
        OpenJPAConfiguration configuration = ((OpenJPAEntityManagerFactorySPI) factory).getConfiguration();
        if (configuration instanceof JDBCConfiguration) {
            DBDictionary dictionary = ((JDBCConfiguration) configuration).getDBDictionaryInstance();
            if (dictionary instanceof SequenceSqlProvider) {
                return (SequenceSqlProvider) dictionary;
            }
        }
        throw new UnsupportedOperationException("DB sequences not supported");
    }
}
