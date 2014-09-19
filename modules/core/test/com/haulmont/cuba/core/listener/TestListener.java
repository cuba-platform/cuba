/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.listener;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.AppBeans;

import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

/**
* @author krivopustov
* @version $Id$
*/
public class TestListener implements
        AfterInsertEntityListener<Server>,
        AfterUpdateEntityListener<Server>,
        AfterDeleteEntityListener<Server>
{
    protected Persistence persistence = AppBeans.get(Persistence.class);

    public void onAfterInsert(Server entity) {
        System.out.println("onAfterInsert " + entity);
    }

    public void onAfterUpdate(Server entity) {
        System.out.println("onAfterUpdate " + entity);

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
        System.out.println("onAfterDelete " + entity);
    }
}
