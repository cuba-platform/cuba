/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.05.2009 14:43:03
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * API of {@link PersistenceConfig} MBean.<br>
 */
public interface PersistenceConfigAPI
{
    String NAME = "cuba_PersistenceConfig";

    boolean isSoftDeleteFor(String table);
}
