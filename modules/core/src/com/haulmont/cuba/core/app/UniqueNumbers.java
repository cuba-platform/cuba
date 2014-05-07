/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DbDialect;
import com.haulmont.cuba.core.global.SequenceSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides unique numbers based on database sequences.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(UniqueNumbersAPI.NAME)
public class UniqueNumbers implements UniqueNumbersAPI {

    @Inject
    private Persistence persistence;

    private Set<String> existingSequences = Collections.synchronizedSet(new HashSet<String>());

    public long getNextNumber(String domain) {
        String seqName = getSequenceName(domain);
        SequenceSupport support = getSequenceSqlProvider();
        String sqlScript = support.getNextValueSql(seqName);

        return getResult(seqName, sqlScript);
    }

    @Override
    public long getCurrentNumber(String domain) {
        String seqName = getSequenceName(domain);
        SequenceSupport support = getSequenceSqlProvider();
        String sqlScript = support.getCurrentValueSql(seqName);

        return getResult(seqName, sqlScript);
    }

    @Override
    public void setCurrentNumber(String domain, long value) {
        String seqName = getSequenceName(domain);
        SequenceSupport support = getSequenceSqlProvider();
        String sqlScript = support.modifySequenceSql(seqName, value);

        Transaction tx = persistence.getTransaction();
        try {
            checkSequenceExists(seqName);
            executeScript(sqlScript);
            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    public void deleteDbSequence(String domain) {
        String seqName = getSequenceName(domain);
        SequenceSupport support = getSequenceSqlProvider();
        String sqlScript = support.deleteSequenceSql(seqName);

        Transaction tx = persistence.getTransaction();
        try {
            executeScript(sqlScript);
            tx.commit();
        } finally {
            tx.end();
        }
    }

    private long getResult(String seqName, String sqlScript) {
        Transaction tx = persistence.getTransaction();
        try {
            checkSequenceExists(seqName);

            Object value = executeScript(sqlScript);
            tx.commit();
            if (value instanceof Long)
                return (Long) value;
            else if (value instanceof BigDecimal)
                return ((BigDecimal) value).longValue();
            else if (value instanceof String)
                return Long.valueOf((String) value);
            else if (value == null)
                throw new IllegalStateException("No value returned");
            else
                throw new IllegalStateException("Unsupported value type: " + value.getClass());
        } finally {
            tx.end();
        }
    }

    private Object executeScript(String sqlScript) {
        EntityManager em = persistence.getEntityManager();
        StrTokenizer tokenizer = new StrTokenizer(sqlScript, SequenceSupport.SQL_DELIMITER);
        Object value = null;
        while (tokenizer.hasNext()) {
            String sql = tokenizer.nextToken();
            Query query = em.createNativeQuery(sql);
            if (isSelectSql(sql))
                value = query.getSingleResult();
            else
                query.executeUpdate();
        }
        return value;
    }

    private boolean isSelectSql(String sql) {
        return sql.trim().toLowerCase().startsWith("select");
    }

    private void checkSequenceExists(String seqName) {
        if (existingSequences.contains(seqName))
            return;

        // Create sequence in separate transaction because it's name is cached and we want to be sure it is created
        // regardless of possible errors in the invoking code
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            SequenceSupport support = getSequenceSqlProvider();
            Query query = em.createNativeQuery(support.sequenceExistsSql(seqName));
            List list = query.getResultList();
            if (list.isEmpty()) {
                query = em.createNativeQuery(support.createSequenceSql(seqName, 1, 1));
                query.executeUpdate();
            }
            existingSequences.add(seqName);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private String getSequenceName(String domain) {
        if (StringUtils.isBlank(domain))
            throw new IllegalArgumentException("Sequence name can not be blank");

        return "seq_un_" + domain;
    }

    private SequenceSupport getSequenceSqlProvider() {
        DbDialect dialect = persistence.getDbDialect();
        if (dialect instanceof SequenceSupport)
            return (SequenceSupport) dialect;
        else
            throw new UnsupportedOperationException("DB sequences not supported");
    }
}
