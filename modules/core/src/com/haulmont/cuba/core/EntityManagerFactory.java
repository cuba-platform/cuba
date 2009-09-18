/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.11.2008 18:42:58
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.sys.EntityManagerImpl;

import java.io.Serializable;

/**
 * Factory creating {@link com.haulmont.cuba.core.EntityManager}s
 */
public interface EntityManagerFactory extends Serializable
{
    EntityManagerImpl createEntityManager();
}
