/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 15:19:38
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface ResourceRepositoryMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=ResourceRepository";

    void create();

    void start();

    ResourceRepositoryAPI getAPI();

    String getContent();

    void evict(String name);

    void evictAll();

    String getResAsString(String name);
}
