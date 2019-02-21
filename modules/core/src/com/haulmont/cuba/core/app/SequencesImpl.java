/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.core.app;

import com.google.common.base.Preconditions;
import com.haulmont.bali.db.DbUtils;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.SequenceSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

@Component(Sequences.NAME)
public class SequencesImpl implements Sequences {

    @Inject
    protected Persistence persistence;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();
    protected Set<String> existingSequences = ConcurrentHashMap.newKeySet();

    protected static final Pattern SEQ_PATTERN = Pattern.compile("[a-zA-Z0-9_]+");

    @Override
    public long createNextValue(Sequence sequence) {
        Preconditions.checkNotNull(sequence, "Sequence can't be null");
        checkSequenceName(sequence.getName());
        String sqlScript = getSequenceSupport(sequence).getNextValueSql(sequence.getName());
        return getResult(sequence, sqlScript);
    }

    @Override
    public long getCurrentValue(Sequence sequence) {
        Preconditions.checkNotNull(sequence, "Sequence can't be null");
        checkSequenceName(sequence.getName());
        String sqlScript = getSequenceSupport(sequence).getCurrentValueSql(sequence.getName());
        return getResult(sequence, sqlScript);
    }

    @Override
    public void setCurrentValue(Sequence sequence, long value) {
        Preconditions.checkNotNull(sequence, "Sequence can't be null");
        checkSequenceName(sequence.getName());
        String sqlScript = getSequenceSupport(sequence).modifySequenceSql(sequence.getName(), value);
        lock.readLock().lock();
        try {
            Transaction tx = persistence.getTransaction(getDataStore(sequence));
            try {
                checkSequenceExists(sequence);
                executeScript(sequence, sqlScript);
                tx.commit();
            } finally {
                tx.end();
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void deleteSequence(Sequence sequence) {
        Preconditions.checkNotNull(sequence, "Sequence can't be null");
        checkSequenceName(sequence.getName());
        String sequenceName = sequence.getName();
        if (!existingSequences.contains(sequenceName)) {
            throw new IllegalStateException(String.format("Attempt to delete nonexistent sequence '%s'", sequence));
        }
        String sqlScript = getSequenceSupport(sequence).deleteSequenceSql(sequenceName);
        lock.writeLock().lock();
        try {
            if (!existingSequences.contains(sequenceName)) {
                return;
            }
            Transaction tx = persistence.getTransaction(getDataStore(sequence));
            try {
                executeScript(sequence, sqlScript);
                existingSequences.remove(sequenceName);

                tx.commit();
            } finally {
                tx.end();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * INTERNAL. Used by tests.
     */
    public void reset() {
        existingSequences.clear();
    }

    protected long getResult(Sequence sequence, String sqlScript) {
        lock.readLock().lock();
        try {
            Transaction tx = persistence.getTransaction(getDataStore(sequence));
            try {
                checkSequenceExists(sequence);
                Object value = executeScript(sequence, sqlScript);
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
        } finally {
            lock.readLock().unlock();
        }
    }

    protected void checkSequenceExists(Sequence sequence) {
        String sequenceName = sequence.getName();
        if (existingSequences.contains(sequenceName)) {
            return;
        }

        lock.readLock().unlock();
        lock.writeLock().lock();
        try {
            try {
                // Create sequence in separate transaction because it's name is cached and we want to be sure it is created
                // regardless of possible errors in the invoking code
                String storeName = getDataStore(sequence);
                Transaction tx = persistence.createTransaction(storeName);
                try {
                    EntityManager em = persistence.getEntityManager(storeName);
                    SequenceSupport sequenceSupport = getSequenceSupport(sequence);
                    Query query = em.createNativeQuery(sequenceSupport.sequenceExistsSql(sequenceName));
                    List list = query.getResultList();
                    if (list.isEmpty()) {
                        query = em.createNativeQuery(sequenceSupport.createSequenceSql(sequenceName, sequence.getStartValue(), sequence.getIncrement()));
                        query.executeUpdate();
                    }
                    existingSequences.add(sequenceName);

                    tx.commit();
                } finally {
                    tx.end();
                }
            } finally {
                lock.readLock().lock();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected Object executeScript(Sequence sequence, String sqlScript) {
        EntityManager em = persistence.getEntityManager(getDataStore(sequence));
        StrTokenizer tokenizer = new StrTokenizer(sqlScript, SequenceSupport.SQL_DELIMITER);
        Object value = null;
        Connection connection = em.getConnection();
        while (tokenizer.hasNext()) {
            String sql = tokenizer.nextToken();
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                try {
                    if (statement.execute()) {
                        ResultSet rs = statement.getResultSet();
                        if (rs.next())
                            value = rs.getLong(1);
                    }
                } finally {
                    DbUtils.closeQuietly(statement);
                }
            } catch (SQLException e) {
                throw new IllegalStateException("Error executing SQL for getting next number", e);
            }
        }
        return value;
    }


    protected SequenceSupport getSequenceSupport(Sequence sequence) {
        return DbmsSpecificFactory.getSequenceSupport(getDataStore(sequence));
    }

    protected String getDataStore(Sequence sequence) {
        return sequence.getDataStore() == null ? Stores.MAIN : sequence.getDataStore();
    }

    protected void checkSequenceName(String sequenceName) {
        if (StringUtils.isBlank(sequenceName))
            throw new IllegalArgumentException("Sequence name can not be blank");
        if (!SEQ_PATTERN.matcher(sequenceName).matches())
            throw new IllegalArgumentException(
                    String.format("Invalid sequence name: '%s'. It can contain only alphanumeric characters and underscores", sequenceName));
    }
}
