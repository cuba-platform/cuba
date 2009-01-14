/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.01.2009 17:02:53
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import javax.ejb.Local;

@Local
public interface ConfigWorker
{
    String JNDI_NAME = "cuba/core/ConfigWorker";
    
    String getProperty(String name);

    void setProperty(String name, String value);
}
