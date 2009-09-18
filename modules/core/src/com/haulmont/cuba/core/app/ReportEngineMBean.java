/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.05.2009 18:28:47
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Management interface of the {@link ReportEngine} MBean.<br>
 * Use {@link #getAPI()} method to obtain a direct reference to application interface.<br>
 * Other methods are intended to invoke from the JMX console.
 * <p>
 * Reference to this interface can be obtained through {@link com.haulmont.cuba.core.Locator#lookupMBean(Class, String)} method
 */
public interface ReportEngineMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=ReportEngine";

    void create();

    /**
     * Get direct reference to application interface. Direct means no proxies or container interceptors.
     */
    ReportEngineAPI getAPI();
}
