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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/*
 * Most of the code is taken from the project https://github.com/mjiderhamn/classloader-leak-prevention
 */

public class StopThreadsCleanUp {

    protected static final String JURT_ASYNCHRONOUS_FINALIZER = "com.sun.star.lib.util.AsynchronousFinalizer";
    protected static final String JURT_KILLER_THREAD_NAME = "JURTKiller";

    /**
     * Thread {@link Runnable} for Sun/Oracle JRE i.e. java.lang.Thread.target
     */
    private Field oracleTarget;

    /**
     * Thread {@link Runnable} for IBM JRE i.e. java.lang.Thread.runnable
     */
    private Field ibmRunnable;

    protected boolean stopThreads;

    /**
     * No of milliseconds to wait for threads to finish execution, before stopping them.
     */
    protected int threadWaitMs = 0;

    /**
     * Should Timer threads tied to the protected ClassLoader classloader be forced to stop at application shutdown?
     */
    protected boolean stopTimerThreads;

    /**
     * Default constructor with {@link #stopThreads} = true and {@link #stopTimerThreads} = true
     */
    public StopThreadsCleanUp() {
        this(true, true);
    }

    public StopThreadsCleanUp(boolean stopThreads) {
        this(stopThreads, stopThreads);
    }

    public StopThreadsCleanUp(boolean stopThreads, boolean stopTimerThreads) {
        this.stopThreads = stopThreads;
        this.stopTimerThreads = stopTimerThreads;
    }

    public void setStopThreads(boolean stopThreads) {
        this.stopThreads = stopThreads;
    }

    public void setStopTimerThreads(boolean stopTimerThreads) {
        this.stopTimerThreads = stopTimerThreads;
    }

    public void setThreadWaitMs(int threadWaitMs) {
        this.threadWaitMs = threadWaitMs;
    }

    public void cleanUp(CleanupTools cleanupTools) {
        // Force the execution of the cleanup code for JURT; see https://issues.apache.org/ooo/show_bug.cgi?id=122517
        forceStartOpenOfficeJurtCleanup(cleanupTools); // (Do this before stopThreads())

        // Fix generic leaks
        stopThreads(cleanupTools);
    }

    /**
     * The bug detailed at https://issues.apache.org/ooo/show_bug.cgi?id=122517 is quite tricky. This is a try to
     * avoid the issues by force starting the threads and it's job queue.
     */
    protected void forceStartOpenOfficeJurtCleanup(CleanupTools cleanupTools) {
        if (stopThreads) {
            if (cleanupTools.isLoadedByClassLoader(cleanupTools.findClass(JURT_ASYNCHRONOUS_FINALIZER))) {
        /*
          The com.sun.star.lib.util.AsynchronousFinalizer class was found and loaded, which means that in case the
          static block that starts the daemon thread had not been started yet, it has been started now.

          Now let's force Garbage Collection, with the hopes of having the finalize()ers that put Jobs on the
          AsynchronousFinalizer queue be executed. Then just leave it, and handle the rest in {@link #stopThreads}.
          */
                cleanupTools.info("OpenOffice JURT AsynchronousFinalizer thread started - forcing garbage collection to invoke finalizers");
                CleanupTools.gc();
            }
        } else {
            // Check for class existence without loading class and thus executing static block
            if (cleanupTools.getClassLoader().getResource("com/sun/star/lib/util/AsynchronousFinalizer.class") != null) {
                cleanupTools.warn("OpenOffice JURT AsynchronousFinalizer thread will not be stopped if started, as stopThreads is false");
        /*
         By forcing Garbage Collection, we'll hopefully start the thread now, in case it would have been started by
         GC later, so that at least it will appear in the logs.
         */
                CleanupTools.gc();
            }
        }
    }

    /**
     * Partially inspired by org.apache.catalina.loader.WebappClassLoader.clearReferencesThreads()
     */
    protected void stopThreads(CleanupTools cleanupTools) {
        final Class<?> workerClass = cleanupTools.findClass("java.util.concurrent.ThreadPoolExecutor$Worker");

        final boolean waitForThreads = threadWaitMs > 0;
        for (Thread thread : cleanupTools.getAllThreads()) {
            final Runnable runnable = getRunnable(cleanupTools, thread);

            final boolean threadLoadedByClassLoader = cleanupTools.isLoadedInClassLoader(thread);
            final boolean threadGroupLoadedByClassLoader = cleanupTools.isLoadedInClassLoader(thread.getThreadGroup());
            final boolean runnableLoadedByClassLoader = cleanupTools.isLoadedInClassLoader(runnable);
            final boolean hasContextClassLoader = cleanupTools.isClassLoaderOrChild(thread.getContextClassLoader());
            if (thread != Thread.currentThread() && // Ignore current thread
                    !JURT_KILLER_THREAD_NAME.equals(thread.getName()) &&
                    (threadLoadedByClassLoader || threadGroupLoadedByClassLoader || hasContextClassLoader || // = cleanupTools.isThreadInClassLoader(thread)
                            runnableLoadedByClassLoader)) {

                if (thread.getClass().getName().startsWith(StopThreadsCleanUp.JURT_ASYNCHRONOUS_FINALIZER)) {
                    // Note, the thread group of this thread may be "system" if it is triggered by the Garbage Collector
                    // however if triggered by us in forceStartOpenOfficeJurtCleanup() it may depend on the application server
                    if (stopThreads) {
                        cleanupTools.info("Found JURT thread " + thread.getName() + "; starting " + JURTKiller.class.getSimpleName());
                        new JURTKiller(cleanupTools, thread).start();
                    } else
                        cleanupTools.warn("JURT thread " + thread.getName() + " is still running in protected ClassLoader");
                } else if (thread.getThreadGroup() != null &&
                        ("system".equals(thread.getThreadGroup().getName()) ||  // System thread
                                "RMI Runtime".equals(thread.getThreadGroup().getName()))) { // RMI thread (honestly, just copied from Tomcat)

                    if ("Keep-Alive-Timer".equals(thread.getName())) {
                        thread.setContextClassLoader(cleanupTools.getLeakSafeClassLoader());
                        cleanupTools.debug("Changed contextClassLoader of HTTP keep alive thread");
                    }
                } else if (thread.isAlive()) { // Non-system, running in protected ClassLoader

                    if (thread.getClass().getName().startsWith("java.util.Timer")) { // Sun/Oracle = "java.util.TimerThread"; IBM = "java.util.Timer$TimerImpl"
                        if (thread.getName() != null && thread.getName().startsWith("PostgreSQL-JDBC-SharedTimer-")) { // Postgresql JDBC timer thread
                            // Replace contextClassLoader, if needed
                            if (hasContextClassLoader) {
                                final Class<?> postgresqlDriver = cleanupTools.findClass("org.postgresql.Driver");
                                final ClassLoader postgresqlCL = (postgresqlDriver != null && !cleanupTools.isLoadedByClassLoader(postgresqlDriver)) ?
                                        postgresqlDriver.getClassLoader() : // Postgresql driver loaded by other classloader than we want to protect
                                        cleanupTools.getLeakSafeClassLoader();
                                thread.setContextClassLoader(postgresqlCL);
                                cleanupTools.warn("Changing contextClassLoader of " + thread + " to " + postgresqlCL);
                            }

                            // Replace AccessControlContext
                            final Field inheritedAccessControlContext = cleanupTools.findField(Thread.class, "inheritedAccessControlContext");
                            if (inheritedAccessControlContext != null) {
                                try {
                                    final AccessControlContext acc = cleanupTools.createAccessControlContext();
                                    inheritedAccessControlContext.set(thread, acc);
                                    cleanupTools.removeDomainCombiner("thread " + thread, acc);
                                } catch (Exception e) {
                                    cleanupTools.error(e);
                                }
                            }
                        } else if (stopTimerThreads) {
                            cleanupTools.warn("Stopping Timer thread '" + thread.getName() + "' running in protected ClassLoader. " +
                                    cleanupTools.getStackTrace(thread));
                            stopTimerThread(cleanupTools, thread);
                        } else {
                            cleanupTools.info("Timer thread is running in protected ClassLoader, but will not be stopped. " +
                                    cleanupTools.getStackTrace(thread));
                        }
                    } else {
                        final String displayString = "Thread '" + thread + "'" +
                                (threadLoadedByClassLoader ? " of type " + thread.getClass().getName() + " loaded by protected ClassLoader" : "") +
                                (runnableLoadedByClassLoader ? " with Runnable of type " + runnable.getClass().getName() + " loaded by protected ClassLoader" : "") +
                                (threadGroupLoadedByClassLoader ? " with ThreadGroup of type " + thread.getThreadGroup().getClass().getName() + " loaded by protected ClassLoader" : "") +
                                (hasContextClassLoader ? " with contextClassLoader = protected ClassLoader or child" : "");

                        // If threads is running an java.util.concurrent.ThreadPoolExecutor.Worker try shutting down the executor
                        if (workerClass != null && workerClass.isInstance(runnable)) {
                            try {
                                // java.util.concurrent.ThreadPoolExecutor, introduced in Java 1.5
                                final Field workerExecutor = cleanupTools.findField(workerClass, "this$0");
                                final ThreadPoolExecutor executor = cleanupTools.getFieldValue(workerExecutor, runnable);
                                if (executor != null) {
                                    if ("org.apache.tomcat.util.threads.ThreadPoolExecutor".equals(executor.getClass().getName())) {
                                        // Tomcat pooled thread
                                        cleanupTools.debug(displayString + " is worker of " + executor.getClass().getName());
                                    } else if (cleanupTools.isLoadedInClassLoader(executor) || cleanupTools.isLoadedInClassLoader(executor.getThreadFactory())) {
                                        if (stopThreads) {
                                            cleanupTools.warn("Shutting down ThreadPoolExecutor of type " + executor.getClass().getName());
                                            executor.shutdownNow();
                                        } else {
                                            cleanupTools.warn("ThreadPoolExecutor of type " + executor.getClass().getName() +
                                                    " should be shut down.");
                                        }
                                    } else {
                                        cleanupTools.info(displayString + " is a ThreadPoolExecutor.Worker of " + executor.getClass().getName() +
                                                " but found no reason to shut down ThreadPoolExecutor.");
                                    }
                                }
                            } catch (Exception ex) {
                                cleanupTools.error(ex);
                            }
                        }

                        if (stopThreads) { // Thread/Runnable/ThreadGroup loaded by protected ClassLoader
                            if (waitForThreads) {
                                cleanupTools.warn("Waiting for " + displayString + " for " + threadWaitMs + " ms. " +
                                        cleanupTools.getStackTrace(thread));

                                cleanupTools.waitForThread(thread, threadWaitMs, true /* Interrupt if needed */);
                            }

                            // Normally threads should not be stopped (method is deprecated), since it may cause an inconsistent state.
                            // In this case however, the alternative is a classloader leak, which may or may not be considered worse.
                            if (thread.isAlive()) {
                                cleanupTools.warn("Stopping " + displayString + ". " + cleanupTools.getStackTrace(thread));
                                //noinspection deprecation
                                thread.stop();
                            } else {
                                cleanupTools.info(displayString + " no longer alive - no action needed.");
                            }
                        } else {
                            cleanupTools.warn(displayString + " would cause leak. " + cleanupTools.getStackTrace(thread));
                        }

                    }
                }
            }
        }
    }

    /**
     * Get {@link Runnable} of given thread, if any
     */
    private Runnable getRunnable(CleanupTools cleanupTools, Thread thread) {
        if (oracleTarget == null && ibmRunnable == null) { // Not yet initialized
            oracleTarget = cleanupTools.findField(Thread.class, "target"); // Sun/Oracle JRE
            ibmRunnable = cleanupTools.findField(Thread.class, "runnable"); // IBM JRE
        }

        return (oracleTarget != null) ? (Runnable) cleanupTools.getFieldValue(oracleTarget, thread) : // Sun/Oracle JRE
                (Runnable) cleanupTools.getFieldValue(ibmRunnable, thread);   // IBM JRE
    }

    protected void stopTimerThread(CleanupTools cleanupTools, Thread thread) {

        try {
            final Field newTasksMayBeScheduled = cleanupTools.findField(thread.getClass(), "newTasksMayBeScheduled");
            final Object queue = cleanupTools.findField(thread.getClass(), "queue").get(thread); // java.lang.TaskQueue
            final Method clear = cleanupTools.findMethod(queue.getClass(), "clear");

            // Do what java.util.Timer.cancel() does
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (queue) {
                newTasksMayBeScheduled.set(thread, Boolean.FALSE);
                clear.invoke(queue);
                queue.notify(); // "In case queue was already empty."
            }

            // We shouldn't need to join() here, thread will finish soon enough
        } catch (Exception ex) {
            cleanupTools.error(ex);
        }
    }

    /**
     * Inner class with the sole task of killing JURT finalizer thread after it is done processing jobs.
     * We need to postpone the stopping of this thread, since more Jobs may in theory be add()ed when the protected
     * ClassLoader is closing down and being garbage collected.
     * See https://issues.apache.org/ooo/show_bug.cgi?id=122517
     */
    protected class JURTKiller extends Thread {

        private final CleanupTools cleanupTools;

        private final Thread jurtThread;

        private final List<?> jurtQueue;

        public JURTKiller(CleanupTools cleanupTools, Thread jurtThread) {
            super(JURT_KILLER_THREAD_NAME);
            this.cleanupTools = cleanupTools;
            this.jurtThread = jurtThread;
            jurtQueue = cleanupTools.getStaticFieldValue(StopThreadsCleanUp.JURT_ASYNCHRONOUS_FINALIZER, "queue");
        }

        @Override
        public void run() {
            if (jurtQueue == null || jurtThread == null) {
                cleanupTools.error(getName() + ": No queue or thread!?");
                return;
            }
            if (!jurtThread.isAlive()) {
                cleanupTools.warn(getName() + ": " + jurtThread.getName() + " is already dead?");
            }

            boolean queueIsEmpty = false;
            while (!queueIsEmpty) {
                try {
                    cleanupTools.debug(getName() + " goes to sleep for " + threadWaitMs + " ms");
                    Thread.sleep(threadWaitMs);
                } catch (InterruptedException e) {
                    // Do nothing
                }

                if (State.RUNNABLE != jurtThread.getState()) { // Unless thread is currently executing a Job
                    cleanupTools.debug(getName() + " about to force Garbage Collection");
                    CleanupTools.gc(); // Force garbage collection, which may put new items on queue

                    synchronized (jurtQueue) {
                        queueIsEmpty = jurtQueue.isEmpty();
                        cleanupTools.debug(getName() + ": JURT queue is empty? " + queueIsEmpty);
                    }
                } else
                    cleanupTools.debug(getName() + ": JURT thread " + jurtThread.getName() + " is executing Job");
            }

            cleanupTools.info(getName() + " about to kill " + jurtThread);
            if (jurtThread.isAlive()) {
                //noinspection deprecation
                jurtThread.stop();
            }
        }
    }
}
