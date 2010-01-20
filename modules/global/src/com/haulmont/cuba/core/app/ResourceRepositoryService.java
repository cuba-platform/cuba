/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 15:44:32
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Service interface to ResourceRepository MBean
 */
public interface ResourceRepositoryService
{
    String NAME = "cuba_ResourceRepositoryService";

    @Deprecated
    String JNDI_NAME = NAME;

    /**
     * Checks whether the specified resource exists
     * @param name resource file name relative to resources root (WEB-INF/conf)
     */
    boolean resourceExists(String name);

    /**
     * Loads resource into cache as String and returns it
     * @param name resource file name relative to resources root (WEB-INF/conf)
     * @return String resource
     */
    String getResAsString(String name);
}
