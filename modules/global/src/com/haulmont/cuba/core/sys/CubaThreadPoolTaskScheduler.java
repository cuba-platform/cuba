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
                return new TaskDecorator<>(task);
            }

            @Override
            protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
                return new TaskDecorator<>(task);
            }
        };
    }

    private static class TaskDecorator<V> implements RunnableScheduledFuture<V> {

        private RunnableScheduledFuture<V> delegate;

        private Log log = LogFactory.getLog(getClass());

        private TaskDecorator(RunnableScheduledFuture<V> delegate) {
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
            AppContext.setSecurityContext(null);
            try {
                delegate.run();

                if (log.isTraceEnabled())
                    log.trace("after execution: SecurityContext=" + AppContext.getSecurityContext());
            } finally {
                AppContext.setSecurityContext(null);
            }
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
