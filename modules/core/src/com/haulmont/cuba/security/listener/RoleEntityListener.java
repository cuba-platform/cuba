/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.12.2008 13:07:13
 *
 * $Id$
 */
package com.haulmont.cuba.security.listener;

import com.haulmont.cuba.core.EntityManagerAdapter;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.QueryAdapter;
import com.haulmont.cuba.core.listener.BeforeDeleteEntityListener;
import com.haulmont.cuba.security.entity.ProfileRole;
import com.haulmont.cuba.security.entity.Role;

import java.util.List;

public class RoleEntityListener implements BeforeDeleteEntityListener<Role>
{
    public void onBeforeDelete(Role entity) {
        EntityManagerAdapter em = PersistenceProvider.getEntityManager();

        QueryAdapter query = em.createQuery("select pr from sec$ProfileRole pr where pr.role = ?1");
        query.setParameter(1, entity);
        List<ProfileRole> list = query.getResultList();
        for (ProfileRole profileRole : list) {
            em.remove(profileRole);
        }
    }
}
