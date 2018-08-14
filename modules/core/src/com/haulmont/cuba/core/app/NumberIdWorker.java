/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core.app;

import com.haulmont.bali.db.DbUtils;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.NumberIdSequence;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.SequenceSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrTokenizer;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generates ids for entities with long/integer PK using database sequences.
 */
@Component(NumberIdWorker.NAME)
public class NumberIdWorker implements NumberIdSequence {

    public static final String NAME = "cuba_NumberIdWorker";

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected ServerConfig serverConfig;

    protected Set<String> existingSequences = Collections.synchronizedSet(new HashSet<String>());

    @Override
    public Long createLongId(String entityName) {
       return createLongId(entityName, 0);
    }

    @Override
    public Long createLongId(String entityName, long startValue) {
        String sqlScript = getSequenceSupport(entityName).getNextValueSql(getSequenceName(entityName));
        int cacheSize = globalConfig.getNumberIdCacheSize();
        return getResult(entityName, sqlScript, startValue, cacheSize == 0 ? 1 : cacheSize);
    }

    /**
     * INTERNAL. Used by tests.
     */
    public void reset() {
        existingSequences.clear();
    }

    protected String getDataStore(String entityName) {
        if (!serverConfig.getUseEntityDataStoreForIdSequence()) {
            return Stores.MAIN;
        } else {
            return metadataTools.getStoreName(metadata.getClassNN(entityName));
        }
    }

    protected SequenceSupport getSequenceSupport(String entityName) {
        return DbmsSpecificFactory.getSequenceSupport(getDataStore(entityName));
    }

    protected String getSequenceName(String entityName) {
        if (StringUtils.isBlank(entityName))
            throw new IllegalArgumentException("entityName is blank");

        return "seq_id_" + entityName.replace("$", "_");
    }

    protected long getResult(String entityName, String sqlScript, long startValue, long increment) {
        Transaction tx = persistence.getTransaction(getDataStore(entityName));
        try {
            checkSequenceExists(entityName, startValue, increment);

            Object value = executeScript(entityName, sqlScript);
            tx.commit();
            if (value instanceof Long)
                return (Long) value;
            else if (value instanceof BigDecimal)
                return ((BigDecimal) value).longValue();
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

    protected void checkSequenceExists(String entityName, long startValue, long increment) {
        String seqName = getSequenceName(entityName);
        if (existingSequences.contains(seqName))
            return;

        synchronized (this) {
            // Create sequence in separate transaction because it's name is cached and we want to be sure it is created
            // regardless of possible errors in the invoking code
            Transaction tx = persistence.createTransaction(getDataStore(entityName));
            try {
                EntityManager em = persistence.getEntityManager(getDataStore(entityName));
                SequenceSupport sequenceSupport = getSequenceSupport(entityName);
                Query query = em.createNativeQuery(sequenceSupport.sequenceExistsSql(seqName));
                List list = query.getResultList();
                if (list.isEmpty()) {
                    query = em.createNativeQuery(sequenceSupport.createSequenceSql(seqName, startValue, increment));
                    query.executeUpdate();
                }
                existingSequences.add(seqName);

                tx.commit();
            } finally {
                tx.end();
            }
        }
    }

    protected Object executeScript(String entityName, String sqlScript) {
        EntityManager em = persistence.getEntityManager(getDataStore(entityName));
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
}