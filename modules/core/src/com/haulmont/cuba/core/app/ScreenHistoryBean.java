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

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.sys.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 */
@Component(ScreenHistory.NAME)
public class ScreenHistoryBean implements ScreenHistory {

    private Logger log = LoggerFactory.getLogger(ScreenHistoryBean.class);

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
            Query q = persistence.getEntityManager().createQuery("select h.user.id, count(h.id) " +
                    "from sec$ScreenHistory h group by h.user.id having count(h.id) > ?1");
            q.setParameter(1, MAX_RECORDS);
            List<Object[]> userList = q.getResultList();
            tx.commitRetaining();

            for (Object[] row : userList) {
                UUID userId = (UUID) row[0];

                TypedQuery<UUID> idQuery = persistence.getEntityManager().createQuery(
                        "select h.id from sec$ScreenHistory h where h.user.id = ?1 order by h.createTs desc", UUID.class);
                idQuery.setParameter(1, userId);
                List<UUID> list = idQuery.getResultList();

                int start = MAX_RECORDS;
                int end = Math.min(start + DELETE_BATCH, list.size());
                while (start < list.size()) {
                    List<UUID> toDelete = list.subList(start, end);

                    Query deleteQuery = persistence.getEntityManager().createQuery(
                            "delete from sec$ScreenHistory h where h.id in ?1");
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
