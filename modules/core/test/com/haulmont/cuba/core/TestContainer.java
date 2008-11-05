/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.11.2008 11:26:12
 * $Id$
 */
package com.haulmont.cuba.core;

import org.jboss.ejb3.embedded.EJB3StandaloneBootstrap;

public class TestContainer
{
    private static boolean started;

    public static void start() {
        if (started)
            return;

        EJB3StandaloneBootstrap.boot(null);
        EJB3StandaloneBootstrap.scanClasspath("build/20-cuba-core.jar");
        started = true;
    }

    public static void stop() {
        if (!started)
            return;
        EJB3StandaloneBootstrap.shutdown();
        started = false;
    }
}
