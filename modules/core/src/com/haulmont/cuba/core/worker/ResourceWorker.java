/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 15:11:02
 *
 * $Id$
 */
package com.haulmont.cuba.core.worker;

import javax.ejb.Local;
import java.io.InputStream;

@Local
public interface ResourceWorker
{
    String JNDI_NAME = "cuba/core/ResourceWorker";

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
