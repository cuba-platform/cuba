/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * System-level class with static methods providing access to some central application structures:
 * <ul>
 *     <li/>Spring's {@link ApplicationContext}
 *     <li/>Application properties which were set in <code>app.properties</code> files
 *     <li/>Current thread's {@link SecurityContext}
 * </ul>
 * It also allows to register listeners which are triggered on the application start/stop, and provides the method
 * {@link #isStarted()} to check whether the app is fully initialized at the moment.
 *
 * @author krivopustov
 * @version $Id$
 */
public class AppContext {

    private static Log log = LogFactory.getLog(AppContext.class);

    /**
     * Application startup/shutdown listener.
     * Implementors should be passed to {@link AppContext#addListener(com.haulmont.cuba.core.sys.AppContext.Listener)} method.
     */
    public interface Listener {
        /**
         * Called by {@link AppContext} after successful application startup and initialization.
         */
        void applicationStarted();

        /**
         * Called by {@link AppContext} before application shutdown.
         */
        void applicationStopped();
    }

    private static ApplicationContext context;

    private static Map<String, String> properties = new ConcurrentHashMap<>();

    private static SecurityContextHolder securityContextHolder = new ThreadLocalSecurityContextHolder();

    private static Set<Listener> listeners = new LinkedHashSet<Listener>();

    private static volatile boolean started;

    public static final SecurityContext NO_USER_CONTEXT =
            new SecurityContext(UUID.fromString("23dce942-d13f-11df-88cd-b3d32fd1e595"), "server");

    /**
     * Used by other framework classes to get access Spring's context. Don't use it in application code.
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * Called by the framework to set Spring's context.
     * @param applicationContext initialized Spring's context
     */
    public static void setApplicationContext(@Nullable ApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * DEPRECATED because this class is considered system-level and should not be called from application code.
     * Use {@link com.haulmont.cuba.core.global.AppBeans} instead.
     */
    @Deprecated
    @Nonnull
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }

    /**
     * DEPRECATED because this class is considered system-level and should not be called from application code.
     * Use {@link com.haulmont.cuba.core.global.AppBeans} instead.
     */
    @Deprecated
    @Nonnull
    public static <T> T getBean(String name, Class<T> beanType) {
        return context.getBean(name, beanType);
    }

    /**
     * DEPRECATED because this class is considered system-level and should not be called from application code.
     * Use {@link com.haulmont.cuba.core.global.AppBeans} instead.
     */
    @Deprecated
    public static <T> T getBean(Class<T> beanType) {
        return context.getBean(beanType);
    }

    /**
     * DEPRECATED because this class is considered system-level and should not be called from application code.
     * Use {@link com.haulmont.cuba.core.global.AppBeans} instead.
     */
    @Deprecated
    public static <T> Map<String, T> getBeansOfType(Class<T> beanType) {
        return context.getBeansOfType(beanType);
    }

    /**
     * @return all property names defined in the set of <code>app.properties</code> files
     */
    public static String[] getPropertyNames() {
        return properties.keySet().toArray(new String[properties.size()]);
    }

    /**
     * Get property value defined in the set of <code>app.properties</code> files.
     * @param key   property key
     * @return      property value or null if the key is not found
     */
    @Nullable
    public static String getProperty(String key) {
        return properties.get(key);
    }

    /**
     * Set property value. The new value will be accessible at the runtime through {@link #getProperty(String)} and
     * {@link #getPropertyNames()}, but will not be saved in any <code>app.properties</code> file and will be lost
     * after the application restart.
     * @param key       property key
     * @param value     property value. If null, the property will be removed.
     */
    public static void setProperty(String key, @Nullable String value) {
        if (value == null)
            properties.remove(key);
        else
            properties.put(key, value);
    }

    /**
     * Called by the framework to replace standard thread-local holder.
     * @param holder    a holder implementation
     */
    public static void setSecurityContextHolder(SecurityContextHolder holder) {
        securityContextHolder = holder;
    }

    /**
     * @return  current thread's {@link SecurityContext}
     */
    public static SecurityContext getSecurityContext() {
        if (started)
            return securityContextHolder.get();
        else
            return NO_USER_CONTEXT;
    }

    /**
     * Set current thread's {@link SecurityContext}.
     * @param securityContext security context to be set for the current thread
     */
    public static void setSecurityContext(@Nullable SecurityContext securityContext) {
        if (log.isTraceEnabled())
            log.trace("setSecurityContext " + securityContext + " for thread " + Thread.currentThread());
        securityContextHolder.set(securityContext);
    }

    /**
     * Register an application start/stop listener.
     * @param listener  listener implementation
     */
    public static void addListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * @return true if the application is fully initialized and can process requests
     */
    public static boolean isStarted() {
        return started;
    }

    /**
     * Called by the framework after the aplication has been started and fully initialized.
     */
    public static void startContext() {
        if (started)
            return;

        started = true;
        for (Listener listener : listeners) {
            listener.applicationStarted();
        }
    }

    /**
     * Called by the framework before the aplication shutdown.
     */
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
