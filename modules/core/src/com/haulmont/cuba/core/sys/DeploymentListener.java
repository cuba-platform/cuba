/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.12.2008 13:39:40
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import org.jboss.mx.util.MBeanServerLocator;

import javax.management.*;

public class DeploymentListener implements NotificationListener
{
    public static void start() {
        new DeploymentListener();
    }

    public DeploymentListener() {
        MBeanServer server = MBeanServerLocator.locateJBoss();
        try {
            server.addNotificationListener(new ObjectName("jboss.web:service=WebServer"), this, null, null);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleNotification(Notification notification, Object handback) {
        System.out.println("Deployment event: " + notification);
    }
}
