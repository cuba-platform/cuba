/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import java.util.List;
import java.util.UUID;

@ManagedBean(ScreenHistory.NAME)
public class ScreenHistoryBean implements ScreenHistory {

    private Log log = LogFactory.getLog(ScreenHistoryBean.class);

    private static final int DELETE_BATCH = 10;

    public void cleanup() {
        if (!AppContext.isStarted())
            return;

        log.debug("Cleanup screen history");
        Transaction tx = Locator.createTransaction();
        try {
            Query q = PersistenceProvider.getEntityManager().createQuery("select h.user.id, count(h.id) " +
                    "from sec$ScreenHistory h group by h.user.id having count(h.id) > ?1");
            q.setParameter(1, MAX_RECORDS);
            List<Object[]> userList = q.getResultList();
            tx.commitRetaining();

            for (Object[] row : userList) {
                UUID userId = (UUID) row[0];

                q = PersistenceProvider.getEntityManager().createQuery(
                        "select h.id from sec$ScreenHistory h where h.user.id = ?1 order by h.createTs desc");
                q.setParameter(1, userId);
                List list = q.getResultList();

                int start = MAX_RECORDS;
                int end = Math.min(start + DELETE_BATCH, list.size());
                while (start < list.size()) {
                    List<UUID> toDelete = list.subList(start, end);

                    Query deleteQuery = PersistenceProvider.getEntityManager().createQuery(
                            "delete from sec$ScreenHistory h where h.id in (?1)");
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
