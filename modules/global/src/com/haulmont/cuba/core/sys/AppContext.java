/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.sys.events.AppContextStartedEvent;
import com.haulmont.cuba.core.sys.events.AppContextStoppedEvent;
import com.haulmont.cuba.core.sys.logging.LogMdc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.OrderComparator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * System-level class with static methods providing access to some central application structures:
 * <ul>
 *     <li>Spring's {@link ApplicationContext}</li>
 *     <li>Application properties which were set in {@code app.properties} files</li>
 *     <li>Current thread's {@link SecurityContext}</li>
 * </ul>
 * It also allows to register listeners which are triggered on the application start/stop, and provides the method
 * {@link #isStarted()} to check whether the app is fully initialized at the moment.
 */
public class AppContext {

    private static final Logger log = LoggerFactory.getLogger(AppContext.class);

    /**
     * Application startup/shutdown listener.
     * Implementors should be passed to {@link AppContext#addListener(com.haulmont.cuba.core.sys.AppContext.Listener)} method.
     *
     * It is recommended to use {@link AppContextStartedEvent} and {@link AppContextStoppedEvent} instead.
     */
    public interface Listener {

        /**
         * Defines the highest precedence for {@link org.springframework.core.Ordered} listeners added by the platform.
         */
        int HIGHEST_PLATFORM_PRECEDENCE = 100;

        /**
         * Defines the lowest precedence for {@link org.springframework.core.Ordered} listeners added by the platform.
         */
        int LOWEST_PLATFORM_PRECEDENCE = 1000;

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

    private static SecurityContextHolder securityContextHolder = new ThreadLocalSecurityContextHolder();

    private static AppComponents appComponents;

    private static AppProperties appProperties;

    private static final List<Listener> listeners = new ArrayList<>();

    private static volatile boolean started;
    private static volatile boolean listenersNotified;

    public static final SecurityContext NO_USER_CONTEXT =
            new SecurityContext(UUID.fromString("23dce942-d13f-11df-88cd-b3d32fd1e595"), "server");

    /**
     * INTERNAL.
     * Used by other framework classes to get access Spring's context.
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * @return all property names defined in the set of {@code app.properties} files
     */
    public static String[] getPropertyNames() {
        if (appProperties == null)
            throw new IllegalStateException("appProperties not initialized");
        return appProperties.getPropertyNames();
    }

    /**
     * Get property value defined in the set of {@code app.properties} files.
     * @param key   property key
     * @return      property value or null if the key is not found
     */
    @Nullable
    public static String getProperty(String key) {
        if (appProperties == null)
            throw new IllegalStateException("appProperties not initialized");
        return appProperties.getProperty(key);
    }

    /**
     * Set property value. The new value will be accessible at the runtime through {@link #getProperty(String)} and
     * {@link #getPropertyNames()}, but will not be saved in any {@code app.properties} file and will be lost
     * after the application restart.
     * @param key       property key
     * @param value     property value. If null, the property will be removed.
     */
    public static void setProperty(String key, @Nullable String value) {
        if (appProperties == null)
            throw new IllegalStateException("appProperties not initialized");
        appProperties.setProperty(key, value);
    }

    /**
     * @return  current thread's {@link SecurityContext} or null if there is no context bound
     */
    @Nullable
    public static SecurityContext getSecurityContext() {
        if (started)
            return securityContextHolder.get();
        else
            return NO_USER_CONTEXT;
    }

    /**
     * @return  current thread's {@link SecurityContext}
     * @throws SecurityException if there is no context bound to the current thread
     */
    public static SecurityContext getSecurityContextNN() {
        SecurityContext securityContext = getSecurityContext();
        if (securityContext == null)
            throw new SecurityException("No security context bound to the current thread");

        return securityContext;
    }

    /**
     * Set current thread's {@link SecurityContext}.
     * @param securityContext security context to be set for the current thread
     */
    public static void setSecurityContext(@Nullable SecurityContext securityContext) {
        log.trace("setSecurityContext {} for thread {}", securityContext, Thread.currentThread());

        securityContextHolder.set(securityContext);
        LogMdc.setup(securityContext);
    }

    /**
     * Sets current thread's {@link SecurityContext}, invokes runnable and sets previous security context back.
     *
     * @param securityContext security context to be set for the current thread
     * @param runnable        runnable
     */
    public static void withSecurityContext(SecurityContext securityContext, Runnable runnable) {
        SecurityContext previousSecurityContext = getSecurityContext();
        setSecurityContext(securityContext);
        try {
            runnable.run();
        } finally {
            setSecurityContext(previousSecurityContext);
        }
    }

    /**
     * Sets current thread's {@link SecurityContext}, calls operation and sets previous security context back.
     *
     * @param securityContext security context to be set for the current thread
     * @param operation       operation
     * @return result of operation
     */
    public static <T> T withSecurityContext(SecurityContext securityContext, SecuredOperation<T> operation) {
        SecurityContext previousSecurityContext = getSecurityContext();
        setSecurityContext(securityContext);
        try {
            return operation.call();
        } finally {
            setSecurityContext(previousSecurityContext);
        }
    }

    /**
     * @return app components holder instance
     */
    public static AppComponents getAppComponents() {
        return appComponents;
    }

    /**
     * Register an application start/stop listener.
     * @param listener  listener implementation
     */
    public static void addListener(Listener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    /**
     * @return true if the application context is initialized
     */
    public static boolean isStarted() {
        return started;
    }

    /**
     * @return true if the application context is initialized and all {@link Listener}s have been notified
     */
    public static boolean isReady() {
        return started && listenersNotified;
    }

    static void startContext() {
        if (started)
            return;

        started = true;
        listeners.sort(new OrderComparator());
        for (Listener listener : listeners) {
            listener.applicationStarted();
        }

        Events events = (Events) getApplicationContext().getBean(Events.NAME);
        events.publish(new AppContextStartedEvent(context));

        listenersNotified = true;
    }

    /**
     * Called by the framework right before the application shutdown.
     */
    static void stopContext() {
        if (!started)
            return;

        started = false;
        for (int i = listeners.size() - 1; i >= 0; i--) {
            Listener listener = listeners.get(i);
            listener.applicationStopped();
        }

        Events events = (Events) getApplicationContext().getBean(Events.NAME);
        events.publish(new AppContextStoppedEvent(context));

        if (context instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) context).close();
        }
    }

    /**
     * INTERNAL.
     * Contains methods for setting up AppContext internals.
     */
    public static class Internals {

        public static void setAppComponents(AppComponents appComponents) {
            AppContext.appComponents = appComponents;
            AppContext.appProperties = new AppProperties(appComponents);
        }

        /**
         * Called by the framework to set Spring's context.
         *
         * @param applicationContext initialized Spring's context
         */
        public static void setApplicationContext(@Nullable ApplicationContext applicationContext) {
            AppContext.context = applicationContext;
        }

        /**
         * Called by the framework to replace standard thread-local holder.
         *
         * @param holder a holder implementation
         */
        public static void setSecurityContextHolder(SecurityContextHolder holder) {
            AppContext.securityContextHolder = holder;
        }

        /**
         * Called by the framework after the application has been started and fully initialized.
         */
        public static void startContext() {
            AppContext.startContext();
        }

        /**
         * Called by the framework right before the application shutdown.
         */
        public static void stopContext() {
            AppContext.stopContext();
        }

        /**
         * Direct access to the {@link AppProperties} object.
         */
        public static AppProperties getAppProperties() {
            return AppContext.appProperties;
        }
    }

    public interface SecuredOperation<T> {
        T call();
    }
}