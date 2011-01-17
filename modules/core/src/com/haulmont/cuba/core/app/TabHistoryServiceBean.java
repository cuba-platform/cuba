/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Valery Novikov
 * Created: 22.11.2010 16:39:11
 *
 * $Id$
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.security.entity.TabHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(TabHistoryService.NAME)
public class TabHistoryServiceBean implements TabHistoryService {

    public int getCurrentUserTabHistoryCount() {
        Transaction tx = Locator.createTransaction();
        long result = 0;
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Query q = em.createQuery("select count(th.id) from sec$TabHistory th where th.creator.id = ?1");
            q.setParameter(1, SecurityProvider.currentOrSubstitutedUserId());
            result = (Long) q.getSingleResult();
            tx.commit();
            return (int)result;
        } finally {
            tx.end();
        }
    }

    public void deleteEndTabHistory() {
        Transaction tx = Locator.createTransaction();
        List<TabHistory> result;
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Query q = em.createQuery("select th from sec$TabHistory th where th.creator.id = ?1 order by th.createTs desc");
            q.setParameter(1, SecurityProvider.currentOrSubstitutedUserId());
            result = (List<TabHistory>) q.getResultList();
            for (int i = MAX_RECORDS - 1; i < result.size(); i++) {
                em.remove(result.get(i));
            }
            tx.commit();
        } finally {
            tx.end();
        }
    }
    public void saveTabHistoryEntity(TabHistory tabHistory) {
        Transaction tx = Locator.createTransaction();
        List<TabHistory> result;
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            em.persist(tabHistory);
            tx.commit();
        } finally {
            tx.end();
        }
    }
}
