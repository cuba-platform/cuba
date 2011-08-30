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
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Nullable;
import java.util.*;

public class AppContext {

    public interface Listener {
        void applicationStarted();
        void applicationStopped();
    }

    private static ApplicationContext context;

    private static Map<String, String> properties = new Hashtable<String, String>();

    private static SecurityContextHolder securityContextHolder = new ThreadLocalSecurityContextHolder();

    private static Set<Listener> listeners = new LinkedHashSet<Listener>();

    private static volatile boolean started;

    public static final SecurityContext NO_USER_CONTEXT = new SecurityContext(UUID.fromString("23dce942-d13f-11df-88cd-b3d32fd1e595")).setUser("server");

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
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

    public static void setSecurityContextHolder(SecurityContextHolder holder) {
        securityContextHolder = holder;
    }

    public static SecurityContext getSecurityContext() {
        if (started)
            return securityContextHolder.get();
        else
            return NO_USER_CONTEXT;
    }

    public static void setSecurityContext(@Nullable SecurityContext securityContext) {
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

    public static void stopContext() {
        if (!started)
            return;

        started = false;
        for (Listener listener : listeners) {
            listener.applicationStopped();
        }
        if (context != null && context instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) context).close();
        }
    }
}
