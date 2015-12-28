/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.google.common.collect.ImmutableList;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author artamonov
 */
@Ignore
public class LoadSubstitutionsTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private User user;
    private User substitutedUser;
    private UserSubstitution userSubstitution;

    public void tearDown() throws Exception {
        if (user != null) {
            cont.deleteRecord(user);
        }
        if (substitutedUser != null) {
            cont.deleteRecord(substitutedUser);
        }
        if (userSubstitution != null) {
            cont.deleteRecord(userSubstitution);
        }
    }

    @Test
    public void testQuerySubstitutions() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Group group = em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

            user = new User();
            user.setLogin("user");
            user.setGroup(group);

            em.persist(user);

            substitutedUser = new User();
            substitutedUser.setLogin("substitutedUser");
            substitutedUser.setGroup(group);

            em.persist(substitutedUser);

            userSubstitution = new UserSubstitution();
            userSubstitution.setUser(user);
            userSubstitution.setSubstitutedUser(substitutedUser);

            user.setSubstitutions(new ArrayList<>(ImmutableList.of(userSubstitution)));

            em.persist(userSubstitution);

            tx.commit();
        } finally {
            tx.end();
        }

        ViewRepository viewRepository = AppBeans.get(ViewRepository.NAME);
        View userView = new View(new View.ViewParams().src(viewRepository.getView(User.class, View.LOCAL)));

        View substitutedUserView = new View(User.class);
        substitutedUserView.addProperty("login");

        View substitutionsView = new View(UserSubstitution.class);
        substitutionsView.addProperty("substitutedUser", substitutedUserView);
        userView.addProperty("substitutions", substitutionsView);

        tx = cont.persistence().createTransaction();
        User loadedUser;
        try {
            EntityManager em = cont.persistence().getEntityManager();
            loadedUser = em.find(User.class, user.getId(), userView);

            tx.commit();
        } finally {
            tx.end();
        }

        Assert.assertNotNull(loadedUser);
        Assert.assertNotNull(loadedUser.getSubstitutions());
        Assert.assertEquals(1, loadedUser.getSubstitutions().size());

        UserSubstitution loadedSubstitution = loadedUser.getSubstitutions().iterator().next();
        Assert.assertNotNull(loadedSubstitution.getUser());
        Assert.assertNotNull(loadedSubstitution.getSubstitutedUser());
    }
}