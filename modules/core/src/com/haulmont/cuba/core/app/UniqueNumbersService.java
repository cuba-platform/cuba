/*
 * Author: Konstantin Krivopustov
 * Created: 16.05.2009 0:15:48
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

import javax.ejb.Local;

/**
 * Local interface to {@link com.haulmont.cuba.core.app.UniqueNumbersServiceBean}
 */
@Local
public interface UniqueNumbersService
{
    String JNDI_NAME = "cuba/core/UniqueNumbersService";

    long getNextNumber(String domain);
}
