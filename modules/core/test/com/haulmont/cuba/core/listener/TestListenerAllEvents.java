/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.listener;

import com.haulmont.bali.db.ArrayHandler;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.AppBeans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
* @author krivopustov
* @version $Id$
*/
public class TestListenerAllEvents implements
        BeforeInsertEntityListener<Server>,
        BeforeUpdateEntityListener<Server>,
        BeforeDeleteEntityListener<Server>,
        AfterInsertEntityListener<Server>,
        AfterUpdateEntityListener<Server>,
        AfterDeleteEntityListener<Server>
{
    protected Persistence persistence = AppBeans.get(Persistence.class);

    public static final List<String> events = new ArrayList<>();

    private Object[] selectObjectFromDb(String sql, Server entity) {
        QueryRunner queryRunner = new QueryRunner();
        try {
            return queryRunner.query(persistence.getEntityManager().getConnection(),
                    sql,
                    new Object[] {entity.getId().toString()},
                    new ArrayHandler()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBeforeInsert(Server entity) {
        Object[] objects = selectObjectFromDb("select id from sys_server where id = ?", entity);
        assertNull(objects);

        events.add("onBeforeInsert: " + entity.getId());
    }

    @Override
    public void onAfterInsert(Server entity) {
        Object[] objects = selectObjectFromDb("select id from sys_server where id = ?", entity);
        assertEquals(entity.getId().toString(), objects[0]);

        events.add("onAfterInsert: " + entity.getId());
    }

    @Override
    public void onBeforeUpdate(Server entity) {
        Object[] objects = selectObjectFromDb("select name from sys_server where id = ?", entity);
        assertEquals("localhost", objects[0]);

        events.add("onBeforeUpdate: " + entity.getId());
    }

    @Override
    public void onAfterUpdate(Server entity) {
        Object[] objects = selectObjectFromDb("select name from sys_server where id = ?", entity);
        assertEquals("changed", objects[0]);

        events.add("onAfterUpdate: " + entity.getId());
    }

    @Override
    public void onBeforeDelete(Server entity) {
        Object[] objects = selectObjectFromDb("select id from sys_server where id = ?", entity);
        assertEquals(entity.getId().toString(), objects[0]);

        events.add("onBeforeDelete: " + entity.getId());
    }

    @Override
    public void onAfterDelete(Server entity) {
        Object[] objects = selectObjectFromDb("select id from sys_server where id = ?", entity);
        assertNull(objects);

        events.add("onAfterDelete: " + entity.getId());
    }
}
