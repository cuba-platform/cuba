/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.scheduling.support.CronSequenceGenerator;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that manages {@link ScheduledTask}s in distributed environment.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(SchedulingAPI.NAME)
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

    protected Map<ScheduledTask, Long> runningTasks = new ConcurrentHashMap<>();

    protected Map<ScheduledTask, Long> lastStartCache = new HashMap<>();

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
    public void setRunning(ScheduledTask task, boolean running) {
        log.trace(task + ": mark running=" + running);
        if (running)
            runningTasks.put(task, timeSource.currentTimeMillis());
        else
            runningTasks.remove(task);
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
            log.trace(task + " is running");
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
                    long currentStart = calculateCurrentStart(task, task.getLastStart(), now, period, frame);

                    if (needToStartNow(now, frame, task.getLastStart(), currentStart)) {
                        runSingletonTask(task, now, me);
                    } else {
                        log.trace(task + "\n not in time frame to start");
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

                    if (log.isTraceEnabled())
                        log.trace(task + "\n now=" + now + " lastStart=" + task.getLastStart()
                                + " lastServer=" + task.getLastStartServer() + " shouldSwitch=" + shouldSwitch
                                + " giveChanceToPreviousHost=" + giveChanceToPreviousHost);

                    if (task.getLastStart() == 0
                            || shouldSwitch
                            || (task.getLastStart() + (giveChanceToPreviousHost ? period + period / 2 : period) <= now)) {
                        runSingletonTask(task, now, me);
                    } else {
                        log.trace(task + "\n time has not come and we shouldn't switch");
                    }
                }
            } else {
                Long lastStart = lastStartCache.get(task);
                if (lastStart == null) {
                    lastStart = 0L;
                }
                if (task.getStartDate() != null || SchedulingType.CRON == task.getSchedulingType()) {
                    long currentStart = calculateCurrentStart(task, lastStart, now, period, frame);

                    if (needToStartNow(now, frame, lastStart, currentStart)) {
                        runTask(task, now);
                    } else {
                        log.trace(task + "\n not in time frame to start");
                    }
                } else {
                    if (log.isTraceEnabled())
                        log.trace(task + "\n now=" + now + " lastStart= " + lastStart);

                    if (now >= lastStart + period) {
                        runTask(task, now);
                    } else {
                        log.trace(task + "\n time has not come");
                    }
                }
            }
        } catch (Throwable throwable) {
            log.error("Unable to process " + task, throwable);
        }
    }

    private boolean needToStartNow(long now, long frame, long lastStart, long currentStart) {
        return currentStart <= now && now < currentStart + frame && lastStart < currentStart;
    }

    protected long calculateCurrentStart(ScheduledTask task, long lastStart, long now, long period, long frame) {
        String cron = task.getCron();
        if (SchedulingType.CRON == task.getSchedulingType()) {
            StopWatch sw = new Log4JStopWatch("Cron next date calculations");
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron, getCurrentTimeZone());
            //if last start = 0 (task never has run) or to far in the past, we use (NOW - FRAME) timestamp for pivot time
            //this approach should work fine cause cron works with absolute time
            long pivotPreviousTime = Math.max(lastStart, now - frame);

            Date currentStart = null;
            Date nextDate = cronSequenceGenerator.next(new Date(pivotPreviousTime));
            while (nextDate.getTime() < now) {//if next date is in past try to find next date nearest to now
                currentStart = nextDate;
                nextDate = cronSequenceGenerator.next(nextDate);
            }

            if (currentStart == null) {
                currentStart = nextDate;
            }

            log.trace(task + "\n now=" + now + " frame=" + frame
                    + " currentStart=" + currentStart + " lastStart=" + lastStart + " cron=" + cron);
            sw.stop();
            return currentStart.getTime();
        } else {
            long repetitions = (now - task.getStartDate().getTime()) / period;
            long currentStart = task.getStartDate().getTime() + repetitions * period;

            log.trace(task + "\n now=" + now + " frame=" + frame + " repetitions=" + repetitions +
                    " currentStart=" + currentStart + " lastStart=" + lastStart);
            return currentStart;
        }
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
            return userSessionManager.findSession(AppContext.getSecurityContext().getSessionId());
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

            if (timedOut)
                runningTasks.remove(task);
            else
                return true;
        }
        return false;
    }
}