/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

/*
 * Author: Konstantin Krivopustov
 * Created: 16.05.2009 0:15:48
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Service interface to UniqueNumbers MBean
 */
public interface UniqueNumbersService
{
    String NAME = "cuba_UniqueNumbersService";

    long getNextNumber(String domain);
}
