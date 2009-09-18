/*
 * Author: Konstantin Krivopustov
 * Created: 16.05.2009 0:12:45
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * API of {@link UniqueNumbers} MBean.<br>
 * Reference to this interface must be obtained through {@link UniqueNumbersMBean#getAPI()} method
 */
public interface UniqueNumbersAPI
{
    /**
     * Returns next sequence value
     * @param domain sequence identifier
     */
    long getNextNumber(String domain);
}
