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

package com.haulmont.cuba.core.listener;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.AppBeans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class TestListener implements
        AfterInsertEntityListener<Server>,
        AfterUpdateEntityListener<Server>,
        AfterDeleteEntityListener<Server>
{
    protected Persistence persistence = AppBeans.get(Persistence.class);

    public static final List<String> events = new ArrayList<>();

    public void onAfterInsert(Server entity) {
        events.add("onAfterInsert: " + entity.getId());
    }

    public void onAfterUpdate(Server entity) {
        events.add("onAfterUpdate: " + entity.getId());

        Set<String> dirtyFields = persistence.getTools().getDirtyFields(entity);
        System.out.println(dirtyFields);

        EntityManager em = persistence.getEntityManager();
        Query q = em.createQuery("select max(s.createTs) from sys$Server s");
        Date maxDate = (Date) q.getSingleResult();
        System.out.println(maxDate);

        // JPA update queries don't work: reentrant flush error
//            Query q = em.createQuery("update sys$Server s set s.name = :name where s.id = :id");
//            Query q = em.createNativeQuery("update SYS_SERVER set NAME = ?1 where ID = ?2");
//            q.setParameter(1, "some other");
//            q.setParameter(2, entity.getId());
//            q.executeUpdate();

        QueryRunner runner = new QueryRunner();
        try {
            runner.update(
                    persistence.getEntityManager().getConnection(),
                    "update SYS_SERVER set NAME = ? where ID = ?",
                    new Object[] {
                        "some other",
                        persistence.getDbTypeConverter().getSqlObject(entity.getId())}
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onAfterDelete(Server entity) {
        events.add("onAfterDelete: " + entity.getId());
    }
}
