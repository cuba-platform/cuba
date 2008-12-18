/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.11.2008 18:42:58
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.impl.EntityManagerImpl;

import java.io.Serializable;

public interface EntityManagerFactory extends Serializable
{
    EntityManagerImpl createEntityManager();
}
