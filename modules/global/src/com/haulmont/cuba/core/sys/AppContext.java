/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 23.12.2009 14:50:58
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import org.springframework.context.ApplicationContext;

import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class AppContext {

    public interface Listener {
        void applicationStarted();
    }

    private static ApplicationContext context;

    private static Map<String, String> properties = new Hashtable<String, String>();

    private static ThreadLocal<SecurityContext> securityContextHolder = new ThreadLocal<SecurityContext>();

    private static Set<Listener> listeners = new LinkedHashSet<Listener>();

    private static volatile boolean started;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static String[] getPropertyNames() {
        return properties.keySet().toArray(new String[properties.size()]);
    }

    public static String getProperty(String key) {
        return properties.get(key);
    }

    public static void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public static SecurityContext getSecurityContext() {
        return securityContextHolder.get();
    }

    public static void setSecurityContext(SecurityContext securityContext) {
        securityContextHolder.set(securityContext);
    }

    public static void addListener(Listener listener) {
        listeners.add(listener);
    }

    public static boolean isStarted() {
        return started;
    }

    public static void startContext() {
        if (started)
            return;

        started = true;
        for (Listener listener : listeners) {
            listener.applicationStarted();
        }
    }
}
