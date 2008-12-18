/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.12.2008 12:04:22
 *
 * $Id$
 */
package com.haulmont.cuba.security.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.listener.BeforeDeleteEntityListener;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.ProfileRole;

public class ProfileEntityListener implements BeforeDeleteEntityListener<Profile>
{
    public void onBeforeDelete(Profile profile) {
        EntityManager em = PersistenceProvider.getEntityManager();

        for (ProfileRole profileRole : profile.getProfileRoles()) {
            em.remove(profileRole);
        }
    }
}
