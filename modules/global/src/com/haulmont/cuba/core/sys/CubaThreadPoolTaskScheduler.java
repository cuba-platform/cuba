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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.Nonnull;
import java.util.concurrent.*;

public class CubaThreadPoolTaskScheduler extends ThreadPoolTaskScheduler implements ApplicationContextAware,
        ApplicationListener<ContextClosedEvent> {

    private static final long serialVersionUID = -2882103892163602009L;

    protected ApplicationContext applicationContext;
    protected StatisticsAccumulator statisticsAccumulator;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setStatisticsAccumulator(StatisticsAccumulator statisticsAccumulator) {
        this.statisticsAccumulator = statisticsAccumulator;
    }

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
                        return;
                    }
                }
                super.beforeExecute(t, r);
            }
        };
    }

    protected <V> RunnableScheduledFuture<V> decorate(Runnable runnable, RunnableScheduledFuture<V> task) {
        return new TaskDecorator<>(task, statisticsAccumulator);
    }

    protected <V> RunnableScheduledFuture<V> decorate(Callable<V> callable, RunnableScheduledFuture<V> task) {
        return new TaskDecorator<>(task, statisticsAccumulator);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (applicationContext == event.getApplicationContext()) {
            getScheduledExecutor().shutdown();
        }
    }

    protected static class TaskDecorator<V> implements RunnableScheduledFuture<V> {

        private final Logger log = LoggerFactory.getLogger(TaskDecorator.class);

        protected RunnableScheduledFuture<V> delegate;
        protected StatisticsAccumulator statisticsAccumulator;

        public TaskDecorator(RunnableScheduledFuture<V> delegate, StatisticsAccumulator statisticsAccumulator) {
            this.delegate = delegate;
            this.statisticsAccumulator = statisticsAccumulator;
        }

        @Override
        public boolean isPeriodic() {
            return delegate.isPeriodic();
        }

        @Override
        public void run() {
            if (log.isTraceEnabled())
                log.trace("before execution: SecurityContext={}", AppContext.getSecurityContext());

            if (statisticsAccumulator != null)
                statisticsAccumulator.incSpringScheduledTasksCount();

            // Reset a SecurityContext in the thread before and after execution
            cleanContext();
            try {
                delegate.run();

                if (log.isTraceEnabled())
                    log.trace("after execution: SecurityContext={}", AppContext.getSecurityContext());
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
        public V get(long timeout, @Nonnull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return delegate.get(timeout, unit);
        }

        @Override
        public long getDelay(@Nonnull TimeUnit unit) {
            return delegate.getDelay(unit);
        }

        @Override
        public int compareTo(@Nonnull Delayed o) {
            return delegate.compareTo(o);
        }
    }
}