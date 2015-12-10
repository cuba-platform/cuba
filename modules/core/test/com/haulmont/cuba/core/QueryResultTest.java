/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.bali.db.MapListHandler;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.QueryResult;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author krivopustov
 * @version $Id$
 */
@SuppressWarnings("IncorrectCreateEntity")
public class QueryResultTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private List<UUID> userIds = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        try {
            runner.update("delete from SYS_QUERY_RESULT");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        createEntities();
    }

    @After
    public void tearDown() throws Exception {
        for (UUID userId : userIds) {
            cont.deleteRecord("SEC_USER", userId);
        }
    }

    private void createEntities() {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            User user;

            Group group = em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

            int k = 0;
            for (String domain : Arrays.asList("@aaa.com", "@bbb.com")) {
                for (String name : Arrays.asList("A-", "B-")) {
                    for (String firstName : Arrays.asList("C-", "D-")) {
                        for (int i = 0; i < 5; i++) {
                            user = new User();
                            user.setGroup(group);

                            userIds.add(user.getId());
                            user.setLogin("user" + StringUtils.leftPad(String.valueOf(k++), 2, '0'));
                            user.setName(name + "User" + i);
                            user.setFirstName(firstName + "User" + i);
                            user.setEmail(user.getLogin() + domain);

                            em.persist(user);
                        }
                    }
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void test() {
        Transaction tx;
        javax.persistence.EntityManager emDelegate;
        EntityManager em;
        Query query;

        UUID sessionId = UUID.randomUUID();
        int queryKey = 1;

        tx = cont.persistence().createTransaction();
        try {
            emDelegate = cont.persistence().getEntityManager().getDelegate();

            QueryResult queryResult = new QueryResult();
            queryResult.setSessionId(sessionId);
            queryResult.setQueryKey(queryKey);
            queryResult.setEntityId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

            emDelegate.persist(queryResult);

            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            query = em.createQuery(
                    "select u from sec$User u, sys$QueryResult qr " +
                            "where qr.entityId = u.id and qr.sessionId = ?1 and qr.queryKey = ?2"
            );
            query.setParameter(1, sessionId);
            query.setParameter(2, queryKey);
            query.setView(
                    new View(User.class)
                            .addProperty("login")
                            .addProperty("name")
                            .addProperty("group", new View(Group.class).addProperty("name"))
            );

//            OpenJPAQuery openJPAQuery = (OpenJPAQuery) query.getDelegate();
//            Map params = new HashMap();
//            params.put(1, sessionId);
//            params.put(2, queryKey);
//            String[] dataStoreActions = openJPAQuery.getDataStoreActions(params);
//
//            System.out.println(dataStoreActions);


            List<User> list = query.getResultList();
            assertEquals(1, list.size());
            User user = list.get(0);
            assertEquals("admin", user.getLogin());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void testFirstQuery() throws SQLException {
        DataService dataService = AppBeans.get(DataService.class);
        LoadContext context = new LoadContext(User.class).setView(View.LOCAL);
        context.setQueryString("select u from sec$User u where u.name like :name").setParameter("name", "A-%");
        List<Entity> entities = dataService.loadList(context);
        assertEquals(20, entities.size());

        List<Map<String, Object>> queryResults = getQueryResults();
        assertEquals(0, queryResults.size());
    }

    @Test
    public void testSecondQuery() throws SQLException {
        DataService dataService = AppBeans.get(DataService.class);
        LoadContext context = new LoadContext(User.class).setView(View.LOCAL);
        context.setQueryString("select u from sec$User u where u.email like :email").setParameter("email", "%aaa.com");

        LoadContext.Query prevQuery = new LoadContext.Query("select u from sec$User u where u.name like :name")
                .setParameter("name", "A-%");
        context.getPrevQueries().add(prevQuery);          context.setQueryKey(111);

        List<Entity> entities = dataService.loadList(context);
        assertEquals(10, entities.size());

        List<Map<String, Object>> queryResults = getQueryResults();
        assertEquals(20, queryResults.size());
    }

    @Test
    public void testThirdQuery() throws SQLException {
        DataService dataService = AppBeans.get(DataService.class);
        LoadContext context;
        List<Entity> entities;

        context = new LoadContext(User.class).setView(View.LOCAL);
        LoadContext.Query query1 = context.setQueryString("select u from sec$User u where u.email like :email")
                .setParameter("email", "%aaa.com");
        entities = dataService.loadList(context);
        assertEquals(20, entities.size());

        context = new LoadContext(User.class).setView(View.LOCAL);
        LoadContext.Query query2 = context.setQueryString("select u from sec$User u where u.name like :name")
                .setParameter("name", "A-%");
        context.getPrevQueries().add(query1);
        context.setQueryKey(111);

        entities = dataService.loadList(context);
        assertEquals(10, entities.size());

        context = new LoadContext(User.class).setView(View.LOCAL);
        context.setQueryString("select u from sec$User u where u.firstName like :firstName")
                .setParameter("firstName", "C-%");
        context.getPrevQueries().add(query1);
        context.getPrevQueries().add(query2);
        context.setQueryKey(111);

        entities = dataService.loadList(context);
        assertEquals(5, entities.size());
    }

    private List<Map<String, Object>> getQueryResults() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(cont.persistence().getDataSource());
        return queryRunner.query("select * from SYS_QUERY_RESULT", new MapListHandler());
    }
}
