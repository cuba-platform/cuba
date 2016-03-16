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

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.sys.logging.LogMdc;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Nullable;
import java.util.*;
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
 */
public class AppContext {

    private static Logger log = LoggerFactory.getLogger(AppContext.class);

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

    private static Set<Listener> listeners = new LinkedHashSet<>();

    private static volatile boolean started;
    private static volatile boolean listenersNotified;

    public static final SecurityContext NO_USER_CONTEXT =
            new SecurityContext(UUID.fromString("23dce942-d13f-11df-88cd-b3d32fd1e595"), "server");

    // Temporary support for deprecated properties: the second element has priority
    private static final List<Pair<String, String>> DEPRECATED_PROPERTIES = Arrays.asList(
            new Pair<>("cuba.connectionUrlList", "cuba.connectionUrl"),
            new Pair<>("cuba.entityLog.enabled", "cuba.security.EntityLog.enabled"), // 6.1
            new Pair<>("cuba.web.externalAuthentication", "cuba.web.ExternalAuthentication"), // 6.1
            new Pair<>("cuba.cluster.messageSendingThreadPoolSize", "cuba.clusterMessageSendingThreadPoolSize"), // 6.1
            new Pair<>("reporting.entityTreeModelMaxDepth", "cuba.reporting.entityTreeModelMaxDeep"), // 6.1
            new Pair<>("cuba.maxUploadSizeMb", "cuba.client.maxUploadSizeMb"), // 6.1
            new Pair<>("cuba.gui.systemInfoScriptsEnabled", "cuba.systemInfoScriptsEnabled"), // 6.1
            new Pair<>("cuba.gui.manualScreenSettingsSaving", "cuba.manualScreenSettingsSaving"), // 6.1
            new Pair<>("cuba.gui.showIconsForPopupMenuActions", "cuba.showIconsForPopupMenuActions"), // 6.1
            new Pair<>("cuba.gui.tableShortcut.insert", "cuba.gui.tableInsertShortcut"), // 6.1
            new Pair<>("cuba.gui.tableShortcut.add", "cuba.gui.tableAddShortcut"), // 6.1
            new Pair<>("cuba.gui.tableShortcut.remove", "cuba.gui.tableRemoveShortcut"), // 6.1
            new Pair<>("cuba.gui.tableShortcut.edit", "cuba.gui.tableEditShortcut"), // 6.1
            new Pair<>("reporting.parameterPrototypeQueryLimit", "reporting.parameterPrototype.queryLimit"), // 6.1
            new Pair<>("reporting.*", "cuba.reporting.*"), // 6.1
            new Pair<>("fts.*", "cuba.fts.*"), // 6.1
            new Pair<>("charts.*", "cuba.charts.*"), // 6.1
            new Pair<>("cuba.amazonS3.*", "cuba.amazon.s3.*") // 6.1
    );

    /**
     * Used by other framework classes to get access Spring's context. Don't use it in application code.
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
        for (Pair<String, String> pair : DEPRECATED_PROPERTIES) {
            if (pair.getFirst().endsWith("*")) {
                String substring1 = pair.getFirst().substring(0, pair.getFirst().length() - 1);
                String substring2 = pair.getSecond().substring(0, pair.getSecond().length() - 1);
                if (key.startsWith(substring1)) {
                    return getDeprecatedProperty(new Pair<>(key, substring2 + key.substring(substring1.length())));
                }
                if (key.startsWith(substring2)) {
                    return getDeprecatedProperty(new Pair<>(substring1 + key.substring(substring2.length()), key));
                }
            }
            if (pair.getFirst().equals(key) || pair.getSecond().equals(key)) {
                return getDeprecatedProperty(pair);
            }
        }
        return getSystemOrAppProperty(key);
    }

    private static String getDeprecatedProperty(Pair<String, String> pair) {
        String value = getSystemOrAppProperty(pair.getSecond());
        if (value != null)
            return value;
        else
            return getSystemOrAppProperty(pair.getFirst());
    }

    private static String getSystemOrAppProperty(String key) {
        String value = System.getProperty(key);
        if (StringUtils.isNotEmpty(value))
            return value;
        else
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
        LogMdc.setup(securityContext);
    }

    /**
     * Register an application start/stop listener.
     * @param listener  listener implementation
     */
    public static void addListener(Listener listener) {
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

    /**
     * Called by the framework after the application has been started and fully initialized.
     */
    public static void startContext() {
        if (started)
            return;

        started = true;
        for (Listener listener : listeners) {
            listener.applicationStarted();
        }
        listenersNotified = true;
    }

    /**
     * Called by the framework right before the application shutdown.
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
