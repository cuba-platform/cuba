/*
 * Author: Konstantin Krivopustov
 * Created: 15.05.2009 22:10:48
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Management interface of the {@link UniqueNumbers} MBean.<br>
 * Use {@link #getAPI()} method to obtain a direct reference to application interface.<br>
 * Other methods are intended to invoke from the JMX console.
 * <p>
 * Reference to this interface can be obtained through {@link com.haulmont.cuba.core.Locator#lookupMBean(Class, String)} method
 */
public interface UniqueNumbersMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=UniqueNumbers";

    /**
     * Get direct reference to application interface. Direct means no proxies or container interceptors.
     */
    UniqueNumbersAPI getAPI();

    long getCurrentNumber(String domain);

    void setCurrentNumber(String domain, long value);
}
