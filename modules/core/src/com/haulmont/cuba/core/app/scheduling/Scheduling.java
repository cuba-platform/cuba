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

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.app.ServerInfoAPI;
import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.entity.SchedulingType;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Class that manages {@link ScheduledTask}s in distributed environment.
 */
@Component(SchedulingAPI.NAME)
public class Scheduling implements SchedulingAPI {

    @Inject
    protected Configuration configuration;

    @Inject
    protected ServerInfoAPI serverInfo;

    @Inject
    protected ClusterManagerAPI clusterManager;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected Authentication authentication;

    @Inject
    protected Coordinator coordinator;

    @Inject
    protected Runner runner;

    @Inject
    protected UserSessionManager userSessionManager;

    @Inject
    protected ServerInfoService serverInfoService;

    protected ConcurrentMap<ScheduledTask, Long> runningTasks = new ConcurrentHashMap<>();

    protected Map<ScheduledTask, Long> lastStartCache = new ConcurrentHashMap<>();

    protected Map<ScheduledTask, Long> lastFinishCache = new ConcurrentHashMap<>();

    protected volatile long schedulingStartTime;

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void processScheduledTasks() {
        if (AppContext.isStarted()) {
            processScheduledTasks(true);
        }
    }

    @Override
    public void processScheduledTasks(boolean onlyIfActive) {
        if (onlyIfActive && !isActive())
            return;

        log.debug("Processing scheduled tasks");
        if (schedulingStartTime == 0)
            schedulingStartTime = timeSource.currentTimeMillis();

        authentication.begin();
        try {
            StopWatch sw = new Log4JStopWatch("Scheduling.processTasks");
            Coordinator.Context context = coordinator.begin();
            try {
                for (ScheduledTask task : context.getTasks()) {
                    processTask(task);
                }
            } finally {
                coordinator.end(context);
            }
            sw.stop();
        } finally {
            authentication.end();
        }
    }

    @Override
    public boolean setRunning(ScheduledTask task, boolean running) {
        log.trace(task + ": mark running=" + running);
        if (running) {
            task.setCurrentStartTimestamp(timeSource.currentTimeMillis());
            Long prev = runningTasks.putIfAbsent(task, task.getCurrentStartTimestamp());
            return prev != null;
        } else {
            Long startTime = runningTasks.get(task);
            if (ObjectUtils.equals(task.getCurrentStartTimestamp(), startTime)) {
                runningTasks.remove(task);
            }
            return false;
        }
    }

    @Override
    public void setFinished(ScheduledTask task) {
        lastFinishCache.put(task, timeSource.currentTimeMillis());
    }

    @Override
    public boolean isActive() {
        return configuration.getConfig(ServerConfig.class).getSchedulingActive();
    }

    @Override
    public void setActive(boolean value) {
        configuration.getConfig(ServerConfig.class).setSchedulingActive(value);
    }

    @Override
    public List<ScheduledTask> getActiveTasks() {
        Coordinator.Context context = coordinator.begin();
        coordinator.end(context);

        List<ScheduledTask> tasks = context.getTasks();
        for (ScheduledTask task : tasks) {
            if (!BooleanUtils.isTrue(task.getSingleton())) {
                Long time = lastStartCache.get(task);
                if (time != null)
                    task.setLastStartTime(new Date(time));
            }
        }
        return tasks;
    }

    protected long getSchedulingInterval() {
        return configuration.getConfig(ServerConfig.class).getSchedulingInterval();
    }

    protected void processTask(ScheduledTask task) {
        if (isRunning(task)) {
            log.trace("{} is running", task);
            return;
        }

        try {
            long now = timeSource.currentTimeMillis();
            String me = serverInfo.getServerId();

            Integer serverPriority = getServerPriority(task, me);

            if (!checkFirst(task, serverPriority, now))
                return;

            long period = task.getPeriod() != null ? task.getPeriod() * 1000 : 0;
            long frame = task.getTimeFrame() != null ? task.getTimeFrame() * 1000 : period / 2;
            if (frame == 0) {//for cron tasks, where period is null we set default frame as scheduling interval
                frame = getSchedulingInterval();
            }

            if (BooleanUtils.isTrue(task.getSingleton())) {
                if (task.getStartDate() != null || SchedulingType.CRON == task.getSchedulingType()) {
                    long currentStart;
                    if (SchedulingType.FIXED_DELAY == task.getSchedulingType()) {
                        currentStart = calculateNextDelayDate(task, task.getLastStart(), coordinator.getLastFinished(task), now, frame, period);
                    } else if (SchedulingType.CRON == task.getSchedulingType()) {
                        currentStart = calculateNextCronDate(task, task.getLastStart(), now, frame);
                    } else {
                        currentStart = calculateNextPeriodDate(task, task.getLastStart(), now, frame, period);
                    }
                    if (needToStartInTimeFrame(now, frame, task.getLastStart(), currentStart)) {
                        runSingletonTask(task, now, me);
                    } else {
                        log.trace("{}\n not in time frame to start", task);
                    }
                } else {
                    Integer lastServerPriority = task.getLastStartServer() == null ?
                            null : getServerPriority(task, task.getLastStartServer());

                    // We should switch to me if the last server wasn't me and I have higher priority
                    boolean shouldSwitch = lastServerWasNotMe(task, me)
                            && (lastServerPriority == null || serverPriority.compareTo(lastServerPriority) < 0);

                    // The last server wasn't me and it has higher priority
                    boolean giveChanceToPreviousHost = lastServerWasNotMe(task, me)
                            && (lastServerPriority != null && serverPriority.compareTo(lastServerPriority) > 0);

                    log.trace("{}\n now={} lastStart={} lastServer={} shouldSwitch={} giveChanceToPreviousHost={}",
                            task, now, task.getLastStart(), task.getLastStartServer(), shouldSwitch, giveChanceToPreviousHost);

                    if (task.getLastStart() == 0 || shouldSwitch) {
                        runSingletonTask(task, now, me);
                    } else {
                        long delay = giveChanceToPreviousHost ? period + period / 2 : period;
                        if (SchedulingType.FIXED_DELAY == task.getSchedulingType()) {
                            long lastFinish = coordinator.getLastFinished(task);
                            if ((task.getLastStart() < lastFinish || !lastFinishCache.containsKey(task)) && lastFinish + delay < now) {
                                runSingletonTask(task, now, me);
                            } else {
                                log.trace("{}\n time has not come and we shouldn't switch", task);
                            }
                        } else if (task.getLastStart() + delay <= now) {
                            runSingletonTask(task, now, me);
                        } else {
                            log.trace("{}\n time has not come and we shouldn't switch", task);
                        }
                    }
                }
            } else {
                Long lastStart = lastStartCache.getOrDefault(task, 0L);
                Long lastFinish = lastFinishCache.getOrDefault(task, 0L);
                if (task.getStartDate() != null || SchedulingType.CRON == task.getSchedulingType()) {
                    long currentStart;
                    if (SchedulingType.FIXED_DELAY == task.getSchedulingType()) {
                        currentStart = calculateNextDelayDate(task, lastStart, lastFinish, now, frame, period);
                    } else if (SchedulingType.CRON == task.getSchedulingType()) {
                        currentStart = calculateNextCronDate(task, lastStart, now, frame);
                    } else {
                        currentStart = calculateNextPeriodDate(task, lastStart, now, frame, period);
                    }
                    if (needToStartInTimeFrame(now, frame, lastStart, currentStart)) {
                        runTask(task, now);
                    } else {
                        log.trace("{}\n not in time frame to start", task);
                    }
                } else {
                    log.trace("{}\n now={} lastStart={} lastFinish={}", task, now, lastStart, lastFinish);
                    if (SchedulingType.FIXED_DELAY == task.getSchedulingType()) {
                        if ((lastStart == 0 || lastStart < lastFinish) && now >= lastFinish + period) {
                            runTask(task, now);
                        } else {
                            log.trace("{}\n time has not come", task);
                        }
                    } else if (now >= lastStart + period) {
                        runTask(task, now);
                    } else {
                        log.trace("{}\n time has not come", task);
                    }
                }
            }
        } catch (Throwable throwable) {
            log.error("Unable to process " + task, throwable);
        }
    }

    protected boolean needToStartInTimeFrame(long now, long frame, long lastStart, long currentStart) {
        return currentStart <= now && now < currentStart + frame && lastStart < currentStart;
    }

    protected long calculateNextCronDate(ScheduledTask task, long date, long currentDate, long frame) {
        StopWatch sw = new Log4JStopWatch("Cron next date calculations");
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(task.getCron(), getCurrentTimeZone());
        //if last start = 0 (task never has run) or to far in the past, we use (NOW - FRAME) timestamp for pivot time
        //this approach should work fine cause cron works with absolute time
        long pivotPreviousTime = Math.max(date, currentDate - frame);

        Date currentStart = null;
        Date nextDate = cronSequenceGenerator.next(new Date(pivotPreviousTime));
        while (nextDate.getTime() < currentDate) {//if next date is in past try to find next date nearest to now
            currentStart = nextDate;
            nextDate = cronSequenceGenerator.next(nextDate);
        }

        if (currentStart == null) {
            currentStart = nextDate;
        }
        log.trace("{}\n now={} frame={} currentStart={} lastStart={} cron={}",
                task, currentDate, frame, currentStart, task.getCron());
        sw.stop();
        return currentStart.getTime();
    }

    protected long calculateNextPeriodDate(ScheduledTask task, long date, long currentDate, long frame, long period) {
        long repetitions = (currentDate - task.getStartDate().getTime()) / period;
        long currentStart = task.getStartDate().getTime() + repetitions * period;
        log.trace("{}\n now={} frame={} repetitions={} currentStart={} lastStart={}",
                task, currentDate, frame, repetitions, currentStart, date);
        return currentStart;
    }

    protected long calculateNextDelayDate(ScheduledTask task, long lastStart, long lastFinish, long currentDate, long frame, long period) {
        long fromDate = lastFinish != 0 ? lastFinish : task.getStartDate().getTime();
        long repetitions = (currentDate - fromDate) / period;
        long currentStart = fromDate + repetitions * period;
        log.trace("{}\n now={} frame={} repetitions={} currentStart={} lastStart={} lastFinish={}",
                task, currentDate, frame, repetitions, currentStart, lastStart, lastFinish);
        return currentStart;
    }

    protected TimeZone getCurrentTimeZone() {
        return serverInfoService.getTimeZone();
    }

    protected boolean lastServerWasNotMe(ScheduledTask task, String me) {
        return task.getLastStartServer() != null && !task.getLastStartServer().equals(me);
    }

    protected void runSingletonTask(ScheduledTask task, long now, String server) throws LoginException {
        boolean finished = true;
        if (task.getLastStart() > 0 && lastServerWasNotMe(task, server)) {
            // Check whether the task is finished if the last execution was from another server
            finished = coordinator.isLastExecutionFinished(task, now);
        }
        if (finished) {
            task.setLastStartTime(new Date(now));
            task.setLastStartServer(server);
            runner.runTask(task, now, getUserSession(task));
        } else
            log.trace(task + "\n not finished");
    }

    protected void runTask(ScheduledTask task, long time) throws LoginException {
        lastStartCache.put(task, time);
        runner.runTask(task, time, getUserSession(task));
    }

    protected boolean checkFirst(ScheduledTask task, Integer serverPriority, long now) {
        if (serverPriority == null) {
            log.trace(task + ": not in permitted hosts or not a master");
            return false;
        }
        if (task.getStartDelay() != null) {
            if ((schedulingStartTime + task.getStartDelay() * 1000) < now) {
                log.trace(task + ": delayed");
                return false;
            }
        }
        if (task.getStartDate() != null && task.getStartDate().getTime() > now) {
            log.trace(task + ": startDate is in the future");
            return false;
        }
        return true;
    }

    protected Integer getServerPriority(ScheduledTask task, String serverId) {
        String permittedServers = task.getPermittedServers();

        if (StringUtils.isBlank(permittedServers)) {
            if (BooleanUtils.isTrue(task.getSingleton()) && !clusterManager.isMaster())
                return null;
            else
                return 0;
        }

        String[] parts = permittedServers.trim().split("[,;]");
        for (int i = 0; i < parts.length; i++) {
            if (serverId.equals(parts[i].trim())) {
                return i + 1;
            }
        }

        return null;
    }

    protected UserSession getUserSession(ScheduledTask task) throws LoginException {
        if (StringUtils.isBlank(task.getUserName())) {
            return userSessionManager.findSession(AppContext.getSecurityContextNN().getSessionId());
        } else {
            return null;
        }
    }

    protected boolean isRunning(ScheduledTask task) {
        Long startTime = runningTasks.get(task);
        if (startTime != null) {
            boolean timedOut;
            if (task.getTimeout() != null && task.getTimeout() != 0) {
                timedOut = (startTime + task.getTimeout() * 1000) < timeSource.currentTimeMillis();
            } else {
                timedOut = (startTime + 1000 * 60 * 60 * 3) < timeSource.currentTimeMillis();
            }

            if (timedOut) {
                runningTasks.remove(task);
            } else {
                return true;
            }
        }
        return false;
    }
}