/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.11.2008 13:23:09
 * $Id$
 */
package com.haulmont.cuba.core;

public interface PersistenceProvider
{
    CubaEntityManagerFactory getEntityManagerFactory();

    CubaEntityManager getEntityManager();

    CubaEntityManager getEntityManager(boolean transactional);
}
