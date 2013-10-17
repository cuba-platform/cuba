/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 * @author novikov
 * @version $Id$
 */
@ManagedBean(ScreenHistory.NAME)
public class ScreenHistoryBean implements ScreenHistory {

    private Log log = LogFactory.getLog(ScreenHistoryBean.class);

    private static final int DELETE_BATCH = 10;

    @Inject
    protected Persistence persistence;

    @Override
    public void cleanup() {
        if (!AppContext.isStarted())
            return;

        log.debug("Cleanup screen history");
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query q = em.createQuery("select h.user.id, count(h.id) " +
                    "from sec$ScreenHistory h group by h.user.id having count(h.id) > ?1");
            q.setParameter(1, MAX_RECORDS);
            List<Object[]> userList = q.getResultList();
            tx.commitRetaining();

            for (Object[] row : userList) {
                UUID userId = (UUID) row[0];

                TypedQuery<UUID> idQuery = em.createQuery(
                        "select h.id from sec$ScreenHistory h where h.user.id = ?1 order by h.createTs desc", UUID.class);
                idQuery.setParameter(1, userId);
                List<UUID> list = idQuery.getResultList();

                int start = MAX_RECORDS;
                int end = Math.min(start + DELETE_BATCH, list.size());
                while (start < list.size()) {
                    List<UUID> toDelete = list.subList(start, end);

                    Query deleteQuery = em.createQuery("delete from sec$ScreenHistory h where h.id in (?1)");
                    deleteQuery.setParameter(1, toDelete);
                    deleteQuery.executeUpdate();
                    tx.commitRetaining();

                    start = end;
                    end = Math.min(start + DELETE_BATCH, list.size());
                }
            }
            tx.commit();
        } finally {
            tx.end();
        }
    }
}
