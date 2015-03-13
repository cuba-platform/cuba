/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {

    private static final long serialVersionUID = -2882103892163602009L;

    @Override
    protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler) {
            @Override
            protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
                return decorate(runnable, task);
            }

            @Override
            protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
                return decorate(callable, task);
            }

            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                while (!AppContext.isReady()) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        t.interrupt();
                    }
                }
                super.beforeExecute(t, r);
            }
        };
    }

    protected <V> RunnableScheduledFuture<V> decorate(Runnable runnable, RunnableScheduledFuture<V> task) {
        return new TaskDecorator<>(task);
    }

    protected <V> RunnableScheduledFuture<V> decorate(Callable<V> callable, RunnableScheduledFuture<V> task) {
        return new TaskDecorator<>(task);
    }

    protected static class TaskDecorator<V> implements RunnableScheduledFuture<V> {

        protected RunnableScheduledFuture<V> delegate;

        protected Log log = LogFactory.getLog(getClass());

        public TaskDecorator(RunnableScheduledFuture<V> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean isPeriodic() {
            return delegate.isPeriodic();
        }

        @Override
        public void run() {
            if (log.isTraceEnabled())
                log.trace("before execution: SecurityContext=" + AppContext.getSecurityContext());

            // Reset a SecurityContext in the thread before and after execution
            cleanContext();
            try {
                delegate.run();

                if (log.isTraceEnabled())
                    log.trace("after execution: SecurityContext=" + AppContext.getSecurityContext());
            } finally {
                cleanContext();
            }
        }

        protected void cleanContext() {
            AppContext.setSecurityContext(null);
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return delegate.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return delegate.isCancelled();
        }

        @Override
        public boolean isDone() {
            return delegate.isDone();
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            return delegate.get();
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return delegate.get(timeout, unit);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return delegate.getDelay(unit);
        }

        @Override
        public int compareTo(Delayed o) {
            return delegate.compareTo(o);
        }
    }
}
