/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.03.2009 15:05:49
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import java.io.InputStream;

/**
 * API of {@link ResourceRepository} MBean.<br>
 * Reference to this interface must be obtained through {@link ResourceRepositoryMBean#getAPI()} method
 */
public interface ResourceRepositoryAPI
{
    String NAME = "cuba_ResourceRepository";
    
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
