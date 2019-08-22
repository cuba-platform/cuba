/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.core.sys.cleanup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.DomainCombiner;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * Most of the code is taken from the project https://github.com/mjiderhamn/classloader-leak-prevention
 */

public class CleanupTools {

    private final Logger log = LoggerFactory.getLogger(CleanupTools.class);

    private static final ProtectionDomain[] NO_DOMAINS = new ProtectionDomain[0];

    private static final AccessControlContext NO_DOMAINS_ACCESS_CONTROL_CONTEXT = new AccessControlContext(NO_DOMAINS);

    /**
     * {@link DomainCombiner} that filters any {@link ProtectionDomain}s loaded by our classloader
     */
    private final DomainCombiner domainCombiner = createDomainCombiner();

    private boolean runningInSingleWar;

    private final Field java_security_AccessControlContext$combiner = findField(AccessControlContext.class, "combiner");
    private final Field java_security_AccessControlContext$parent = findField(AccessControlContext.class, "parent");
    private final Field java_security_AccessControlContext$privilegedContext = findField(AccessControlContext.class, "privilegedContext");

    public CleanupTools(boolean runningInSingleWar) {
        this.runningInSingleWar = runningInSingleWar;
    }

    public boolean isLoadedInClassLoader(Object o) {
        return (o instanceof Class) && isLoadedByClassLoader((Class<?>) o) || // Object is a java.lang.Class instance
                o != null && isLoadedByClassLoader(o.getClass());
    }

    public boolean isLoadedByClassLoader(Class<?> clazz) {
        return clazz != null && isClassLoaderOrChild(clazz.getClassLoader());
    }

    public boolean isClassLoaderOrChild(ClassLoader cl) {
        if (cl == null) {
            return false;
        } else if (cl == getClassLoader()) {
            return true;
        } else { // It could be a child of the webapp classloader
            while (cl != null) {
                if (cl == getClassLoader())
                    return true;

                cl = cl.getParent();
            }
            return false;
        }
    }

    public Collection<Thread> getAllThreads() {
        // This is some orders of magnitude slower...
        // return Thread.getAllStackTraces().keySet();

        // Find root ThreadGroup
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        while (tg.getParent() != null)
            tg = tg.getParent();

        // Note that ThreadGroup.enumerate() silently ignores all threads that does not fit into array
        int guessThreadCount = tg.activeCount() + 50;
        Thread[] threads = new Thread[guessThreadCount];
        int actualThreadCount = tg.enumerate(threads);
        while (actualThreadCount == guessThreadCount) { // Map was filled, there may be more
            guessThreadCount *= 2;
            threads = new Thread[guessThreadCount];
            actualThreadCount = tg.enumerate(threads);
        }

        // Filter out nulls
        final List<Thread> output = new ArrayList<>();
        for (Thread t : threads) {
            if (t != null) {
                output.add(t);
            }
        }
        return output;
    }

    public Class<?> findClass(String className) {
        return findClass(className, false);
    }

    public Class<?> findClass(String className, boolean trySystemCL) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            if (trySystemCL) {
                try {
                    return Class.forName(className, true, ClassLoader.getSystemClassLoader());
                } catch (ClassNotFoundException e1) {
                    // Silently ignore
                    return null;
                }
            }
            // Silently ignore
            return null;
        } catch (Exception ex) { // Example SecurityException
            warn(ex);
            return null;
        }
    }

    public Field findField(Class<?> clazz, String fieldName) {
        if (clazz == null)
            return null;

        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true); // (Field is probably private)
            return field;
        } catch (NoSuchFieldException ex) {
            // Silently ignore
            return null;
        } catch (Exception ex) { // Example SecurityException
            warn(ex);
            return null;
        }
    }

    public AccessControlContext createAccessControlContext() {
        try { // Try the normal way
            return new AccessControlContext(NO_DOMAINS_ACCESS_CONTROL_CONTEXT, domainCombiner);
        } catch (SecurityException e) { // createAccessControlContext not granted
            try { // Try reflection
                Constructor<AccessControlContext> constructor =
                        AccessControlContext.class.getDeclaredConstructor(ProtectionDomain[].class, DomainCombiner.class);
                constructor.setAccessible(true);
                return constructor.newInstance(NO_DOMAINS, domainCombiner);
            } catch (RuntimeException e1) {
                throw e1;
            } catch (Exception e1) {
                log.error("createAccessControlContext not granted and AccessControlContext could not be created via reflection");
                return AccessController.getContext();
            }
        }
    }

    /**
     * {@link DomainCombiner} that filters any {@link ProtectionDomain}s loaded by our classloader
     */
    private DomainCombiner createDomainCombiner() {
        return new DomainCombiner() {

            /** Flag to detected recursive calls */
            private final ThreadLocal<Boolean> isExecuting = new ThreadLocal<Boolean>();

            @Override
            public ProtectionDomain[] combine(ProtectionDomain[] currentDomains, ProtectionDomain[] assignedDomains) {
                if (assignedDomains != null && assignedDomains.length > 0) {
                    log.error("Unexpected assignedDomains - please report to developer of this library!");
                }

                if (Boolean.TRUE.equals(isExecuting.get()))
                    throw new RuntimeException();

                try {
                    isExecuting.set(Boolean.TRUE); // Throw NestedProtectionDomainCombinerException on nested calls

                    // Keep all ProtectionDomain not involving the web app classloader
                    final List<ProtectionDomain> output = new ArrayList<ProtectionDomain>();
                    for (ProtectionDomain protectionDomain : currentDomains) {
                        if (protectionDomain.getClassLoader() == null ||
                                !isClassLoaderOrChild(protectionDomain.getClassLoader())) {
                            output.add(protectionDomain);
                        }
                    }
                    return output.toArray(new ProtectionDomain[output.size()]);
                } finally {
                    isExecuting.remove();
                }
            }
        };
    }

    /**
     * Recursively unset our custom {@link DomainCombiner} (loaded in the web app) from the {@link AccessControlContext}
     * and any parents or privilegedContext thereof.
     */
    public void removeDomainCombiner(String owner, AccessControlContext accessControlContext) {
        if (accessControlContext != null && java_security_AccessControlContext$combiner != null) {
            if (getFieldValue(java_security_AccessControlContext$combiner, accessControlContext) == this.domainCombiner) {
                warn(AccessControlContext.class.getSimpleName() + " of " + owner + " used custom combiner - unsetting");
                try {
                    java_security_AccessControlContext$combiner.set(accessControlContext, null);
                } catch (Exception e) {
                    error(e);
                }
            }

            // Recurse
            if (java_security_AccessControlContext$parent != null) {
                removeDomainCombiner(owner, (AccessControlContext) getFieldValue(java_security_AccessControlContext$parent, accessControlContext));
            }
            if (java_security_AccessControlContext$privilegedContext != null) {
                removeDomainCombiner(owner, (AccessControlContext) getFieldValue(java_security_AccessControlContext$privilegedContext, accessControlContext));
            }
        }
    }

    public <T> T getFieldValue(Field field, Object obj) {
        try {
            // noinspection unchecked
            return (T) field.get(obj);
        } catch (Exception ex) {
            warn(ex);
            // Silently ignore
            return null;
        }
    }

    public Method findMethod(Class<?> clazz, String methodName, Class... parameterTypes) {
        if (clazz == null)
            return null;

        try {
            final Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException ex) {
            warn(ex);
            // Silently ignore
            return null;
        }
    }

    public <E> E getStaticFieldValue(String className, String fieldName) {
        // noinspection unchecked
        return (E) getStaticFieldValue(className, fieldName, false);
    }

    public <E> E getStaticFieldValue(String className, String fieldName, boolean trySystemCL) {
        Field staticField = findFieldOfClass(className, fieldName, trySystemCL);
        // noinspection unchecked
        return (staticField != null) ? (E) getStaticFieldValue(staticField) : null;
    }

    public <T> T getStaticFieldValue(Field field) {
        try {
            if (!Modifier.isStatic(field.getModifiers())) {
                warn(field.toString() + " is not static");
                return null;
            }

            // noinspection unchecked
            return (T) field.get(null);
        } catch (Exception ex) {
            warn(ex);
            // Silently ignore
            return null;
        }
    }

    public Field findFieldOfClass(String className, String fieldName, boolean trySystemCL) {
        Class<?> clazz = findClass(className, trySystemCL);
        if (clazz != null) {
            return findField(clazz, fieldName);
        } else
            return null;
    }

    /**
     * Make the provided Thread stop sleep(), wait() or join() and then give it the provided no of milliseconds to finish
     * executing.
     *
     * @param thread    The thread to wake up and wait for
     * @param waitMs    The no of milliseconds to wait. If <= 0 this method does nothing.
     * @param interrupt Should {@link Thread#interrupt()} be called first, to make thread stop sleep(), wait() or join()?
     */
    public void waitForThread(Thread thread, long waitMs, boolean interrupt) {
        if (waitMs > 0) {
            if (interrupt) {
                try {
                    thread.interrupt(); // Make Thread stop waiting in sleep(), wait() or join()
                } catch (SecurityException e) {
                    error(e);
                }
            }

            try {
                thread.join(waitMs); // Wait for thread to run
            } catch (InterruptedException e) {
                // Do nothing
            }
        }
    }

    /**
     * Get current stack trace or provided thread as string. Returns {@code "unavailable"} if stack trace could not be acquired.
     */
    public String getStackTrace(Thread thread) {
        try {
            final StackTraceElement[] stackTrace = thread.getStackTrace();
            if (stackTrace.length == 0)
                return "Thread state: " + thread.getState();

            final StringBuilder output = new StringBuilder("Thread stack trace: ");
            for (StackTraceElement stackTraceElement : stackTrace) {
                // if(output.length() > 0) // Except first
                output.append("\n\tat ");
                output.append(stackTraceElement.toString());
            }
            return output.toString().trim(); //
        } catch (Throwable t) { // SecurityException
            return "Thread details unavailable";
        }
    }


    public ClassLoader getClassLoader() {
        if (runningInSingleWar) {
            return this.getClass().getClassLoader().getParent();
        }
        return this.getClass().getClassLoader();
    }

    public ClassLoader getLeakSafeClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    /**
     * Unlike <code>{@link System#gc()}</code> this method guarantees that garbage collection has been performed before
     * returning.
     */
    public static void gc() {
        if (isDisableExplicitGCEnabled()) {
            System.err.println(CleanupTools.class.getSimpleName() + ": "
                    + "Skipping GC call since -XX:+DisableExplicitGC is supplied as VM option.");
            return;
        }

        Object obj = new Object();
        WeakReference<Object> ref = new WeakReference<Object>(obj);
        //noinspection UnusedAssignment
        obj = null;
        while (ref.get() != null) {
            System.gc();
        }
    }

    /**
     * Check is "-XX:+DisableExplicitGC" enabled.
     *
     * @return true is "-XX:+DisableExplicitGC" is set als vm argument, false otherwise.
     */
    private static boolean isDisableExplicitGCEnabled() {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        List<String> aList = bean.getInputArguments();

        return aList.contains("-XX:+DisableExplicitGC");
    }

    public void debug(String msg) {
        log.debug(msg);
    }

    public void warn(Throwable t) {
        log.warn(t.getMessage(), t);
    }

    public void error(Throwable t) {
        log.error(t.getMessage(), t);
    }

    public void warn(String msg) {
        log.warn(msg);
    }

    public void error(String msg) {
        log.error(msg);
    }

    public void info(String msg) {
        log.info(msg);
    }
}
