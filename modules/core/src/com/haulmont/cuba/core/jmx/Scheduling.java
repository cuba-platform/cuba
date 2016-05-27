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

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.scheduling.SchedulingAPI;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.Authenticated;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component("cuba_SchedulingMBean")
public class Scheduling implements SchedulingMBean {

    @Inject
    protected SchedulingAPI scheduling;

    @Inject
    protected Persistence persistence;

    @Inject
    protected TimeSource timeSource;

    @Override
    public boolean isActive() {
        return scheduling.isActive();
    }

    @Override
    @Authenticated
    public void setActive(boolean value) {
        scheduling.setActive(value);
    }

    @Override
    public String printActiveScheduledTasks() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        StringBuilder sb = new StringBuilder();
        List<ScheduledTask> tasks = scheduling.getActiveTasks();
        for (ScheduledTask task : tasks) {
            sb.append(task).append(", lastStart=");
            if (task.getLastStartTime() != null) {
                sb.append(dateFormat.format(task.getLastStartTime()));
                if (BooleanUtils.isTrue(task.getSingleton()))
                    sb.append(" on ").append(task.getLastStartServer());
            } else {
                sb.append("<never>");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    @Override
    public String processScheduledTasks() {
        if (!AppContext.isStarted())
            return "Not started yet";

        try {
            scheduling.processScheduledTasks(false);
            return "Done";
        } catch (Throwable e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Authenticated
    @Override
    public String removeExecutionHistory(String age, String maxPeriod) {
        List<UUID> list;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            String jpql = "select e.id from sys$ScheduledExecution e where e.startTime < ?1";
            if (maxPeriod != null) {
                jpql += " and e.task.period <= ?2";
            }
            jpql += " order by e.startTime";

            Query query = em.createQuery(jpql);

            Date startDate = DateUtils.addHours(timeSource.currentTimestamp(), -Integer.valueOf(age));
            query.setParameter(1, startDate);
            if (maxPeriod != null) {
                query.setParameter(2, Integer.parseInt(maxPeriod) * 3600);
            }
            list = query.getResultList();

            tx.commit();
        } finally {
            tx.end();
        }

        for (int i = 0; i < list.size(); i += 100) {
            final List<UUID> subList = list.subList(i, Math.min(i + 100, list.size()));
            persistence.createTransaction().execute(new Transaction.Runnable() {
                @Override
                public void run(EntityManager em) {
                    Query query = em.createQuery("delete from sys$ScheduledExecution e where e.id in ?1");
                    query.setParameter(1, subList);
                    query.executeUpdate();
                }
            });
        }

        return "Deleted " + list.size();
    }
}