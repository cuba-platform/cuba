/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 15:19:38
 *
 * $Id$
 */
package com.haulmont.cuba.core.mbean;

import java.io.InputStream;

public interface ResourceRepositoryMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=ResourceRepository";

    void create();

    void evictAll();

    /**
     * Loads resource into cache as byte array and returns it
     * @param name resource file name relative to resources root (jboss/server/default/conf)
     * @return resource as stream
     */
    InputStream getResAsStream(String name);

    /**
     * Loads resource into cache as String and returns it
     * @param name resource file name relative to resources root (jboss/server/default/conf)
     * @return String resource
     */
    String getResAsString(String name);
}
