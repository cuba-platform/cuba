/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.05.2009 10:55:55
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Management interface of the {@link Emailer} MBean.<br>
 * Use {@link #getAPI()} method to obtain a direct reference to application interface.<br>
 * Other methods are intended to invoke from the JMX console.
 * <p>
 * Reference to this interface can be obtained through {@link com.haulmont.cuba.core.Locator#lookupMBean(Class, String)} method
 */
public interface EmailerMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=Emailer";

    /**
     * Get direct reference to application interface. Direct means no proxies or container interceptors.
     */
    EmailerAPI getAPI();

    String getFromAddress();

    void setFromAddress(String address);

    String sendTestEmail(String addresses);
}
