/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.NumberIdSequence;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.SequenceSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(NumberIdWorker.NAME)
public class NumberIdWorker implements NumberIdSequence {

    public static final String NAME = "cuba_NumberIdWorker";

    @Inject
    protected Persistence persistence;

    @Inject
    protected GlobalConfig config;

    protected Set<String> existingSequences = Collections.synchronizedSet(new HashSet<String>());

    protected SequenceSupport sequenceSupport;

    @PostConstruct
    public void init() {
        sequenceSupport = DbmsSpecificFactory.getSequenceSupport();
    }

    public Long createLongId(String entityName) {
        String seqName = getSequenceName(entityName);
        String sqlScript = sequenceSupport.getNextValueSql(seqName);

        return getResult(seqName, sqlScript, 0, config.getNumberIdCacheSize());
    }

    protected String getSequenceName(String entityName) {
        if (StringUtils.isBlank(entityName))
            throw new IllegalArgumentException("entityName is blank");

        return "seq_id_" + entityName.replace("$", "_");
    }

    protected long getResult(String seqName, String sqlScript, long startValue, long increment) {
        Transaction tx = persistence.getTransaction();
        try {
            checkSequenceExists(seqName, startValue, increment);

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

    protected void checkSequenceExists(String seqName, long startValue, long increment) {
        if (existingSequences.contains(seqName))
            return;

        // Create sequence in separate transaction because it's name is cached and we want to be sure it is created
        // regardless of possible errors in the invoking code
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

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

    protected Object executeScript(String sqlScript) {
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

    protected boolean isSelectSql(String sql) {
        return sql.trim().toLowerCase().startsWith("select");
    }
}
