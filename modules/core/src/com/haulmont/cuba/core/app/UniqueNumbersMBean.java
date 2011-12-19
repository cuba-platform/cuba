/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

/*
 * Author: Konstantin Krivopustov
 * Created: 15.05.2009 22:10:48
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * {@link UniqueNumbers} JMX interface.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface UniqueNumbersMBean
{
    long getCurrentNumber(String domain);

    void setCurrentNumber(String domain, long value);

    long getNextNumber(String domain);
}
