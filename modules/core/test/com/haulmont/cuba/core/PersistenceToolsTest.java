/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.security.entity.User;

import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class PersistenceToolsTest extends CubaTestCase {

    private Persistence persistence;
    private ViewRepository viewRepository;
    private UUID userId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        persistence = AppBeans.get(Persistence.class);
        viewRepository = AppBeans.get(Metadata.class).getViewRepository();
        userId = UUID.fromString("60885987-1b61-4247-94c7-dff348347f93");
    }

    public void testReloadEntityNewTx() {
        final User originalUser = persistence.createTransaction().execute(new Transaction.Callable<User>() {
            @Override
            public User call(EntityManager em) {
                em.setView(viewRepository.getView(User.class, "user.browse"));
                return em.find(User.class, userId);
            }
        });

        assertNotNull(originalUser);
        assertNull(originalUser.getUserRoles());

        User reloadedUser = persistence.createTransaction().execute(new Transaction.Callable<User>() {
            @Override
            public User call(EntityManager em) {
                return persistence.getTools().reloadEntity(originalUser, "user.edit");
            }
        });
        assertNotNull(reloadedUser);
        assertNotNull(reloadedUser.getUserRoles());
        assertTrue(originalUser != reloadedUser);
    }

    public void testReloadEntitySameTx() {
        User originalUser;
        User reloadedUser;

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            em.setView(viewRepository.getView(User.class, "user.browse"));
            originalUser = em.find(User.class, userId);
            assertNotNull(originalUser);

            reloadedUser = persistence.getTools().reloadEntity(originalUser, "user.edit");
            tx.commit();
        } finally {
            tx.end();
        }
        assertNotNull(reloadedUser);
        assertNotNull(reloadedUser.getUserRoles());
        assertTrue(originalUser == reloadedUser);
    }
}
