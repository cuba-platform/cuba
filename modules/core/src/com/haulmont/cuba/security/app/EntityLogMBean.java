/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.2009 16:09:14
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

public interface EntityLogMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=EntityLog";

    void create();

    void start();

    EntityLogAPI getAPI();

    void invalidateCache();
}
