/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.bali.db.DbUtils;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.SequenceSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import org.springframework.stereotype.Component;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Provides unique numbers based on database sequences.
 *
 * @author krivopustov
 * @version $Id$
 */
@Component(UniqueNumbersAPI.NAME)
public class UniqueNumbers implements UniqueNumbersAPI {

    @Inject
    protected Persistence persistence;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    @GuardedBy("lock")
    protected Set<String> existingSequences = new HashSet<>();

    protected SequenceSupport sequenceSupport;

    @PostConstruct
    public void init() {
        sequenceSupport = DbmsSpecificFactory.getSequenceSupport();
    }

    @Override
    public long getNextNumber(String domain) {
        String seqName = getSequenceName(domain);
        String sqlScript = sequenceSupport.getNextValueSql(seqName);

        try {
            lock.readLock().lock();
            return getResult(seqName, sqlScript);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public long getCurrentNumber(String domain) {
        String seqName = getSequenceName(domain);
        String sqlScript = sequenceSupport.getCurrentValueSql(seqName);

        try {
            lock.readLock().lock();
            return getResult(seqName, sqlScript);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void setCurrentNumber(String domain, long value) {
        String seqName = getSequenceName(domain);
        String sqlScript = sequenceSupport.modifySequenceSql(seqName, value);

        Transaction tx = persistence.getTransaction();
        try {
            lock.readLock().lock();
            checkSequenceExists(seqName);
            executeScript(sqlScript);
            tx.commit();
        } finally {
            lock.readLock().unlock();
            tx.end();
        }
    }

    @Override
    public void deleteSequence(String domain) {
        String seqName = getSequenceName(domain);

        if (!containsSequence(seqName)) {
            throw new IllegalStateException("Attempt to delete nonexistent sequence " + domain);
        }
        
        String sqlScript = sequenceSupport.deleteSequenceSql(seqName);

        Transaction tx = persistence.getTransaction();
        try {
            lock.writeLock().lock();
            if (!containsSequence(seqName)) {
                tx.commit();
                return;
            }

            executeScript(sqlScript);
            tx.commit();
            existingSequences.remove(seqName);
        } finally {
            lock.writeLock().unlock();
            tx.end();
        }
    }

    protected long getResult(String seqName, String sqlScript) {
        Transaction tx = persistence.getTransaction();
        try {
            checkSequenceExists(seqName);

            Object value = executeScript(sqlScript);
            tx.commit();
            if (value instanceof Long)
                return (Long) value;
            else if (value instanceof BigDecimal)
                return ((BigDecimal) value).longValue();
            else if (value instanceof BigInteger)
                return ((BigInteger) value).longValue();
            else if (value instanceof String)
                return Long.parseLong((String) value);
            else if (value == null)
                throw new IllegalStateException("No value returned");
            else
                throw new IllegalStateException("Unsupported value type: " + value.getClass());
        } finally {
            tx.end();
        }
    }

    protected Object executeScript(String sqlScript) {
        EntityManager em = persistence.getEntityManager();
        StrTokenizer tokenizer = new StrTokenizer(sqlScript, SequenceSupport.SQL_DELIMITER);
        Object value = null;
        Connection connection = em.getConnection();
        while (tokenizer.hasNext()) {
            String sql = tokenizer.nextToken();
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                try {
                    statement.execute();
                    if (isSelectSql(sql) || isInsertSql(sql)) {
                        ResultSet rs = statement.getResultSet();
                        if (rs != null && rs.next())
                            value = rs.getLong(1);
                    }
                } finally {
                    DbUtils.closeQuietly(statement);
                }
            } catch (SQLException e) {
                throw new IllegalStateException(String.format("Error in sql while getting next number"), e);
            }
        }
        return value;
    }

    protected boolean isInsertSql(String sql) {
        return sql.trim().toLowerCase().startsWith("insert");
    }

    protected boolean isSelectSql(String sql) {
        return sql.trim().toLowerCase().startsWith("select");
    }

    protected void checkSequenceExists(String seqName) {
        if (containsSequence(seqName)) return;

        // Create sequence in separate transaction because it's name is cached and we want to be sure it is created
        // regardless of possible errors in the invoking code
        Transaction tx = persistence.createTransaction();
        try {
            lock.readLock().unlock();
            lock.writeLock().lock();

            EntityManager em = persistence.getEntityManager();

            Query query = em.createNativeQuery(sequenceSupport.sequenceExistsSql(seqName));
            List list = query.getResultList();
            if (list.isEmpty()) {
                query = em.createNativeQuery(sequenceSupport.createSequenceSql(seqName, 1, 1));
                query.executeUpdate();
            }
            tx.commit();
            existingSequences.add(seqName);
        } finally {
            lock.readLock().lock();
            lock.writeLock().unlock();
            tx.end();
        }
    }

    protected boolean containsSequence(String name) {
        try {
            lock.readLock().lock();
            return existingSequences.contains(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    protected String getSequenceName(String domain) {
        if (StringUtils.isBlank(domain))
            throw new IllegalArgumentException("Sequence name can not be blank");

        return "seq_un_" + domain;
    }
}