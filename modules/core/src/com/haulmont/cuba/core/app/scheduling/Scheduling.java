/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.ManagementBean;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.app.ServerInfoAPI;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that manages {@link ScheduledTask}s in distributed environment.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(SchedulingAPI.NAME)
public class Scheduling extends ManagementBean implements SchedulingAPI, SchedulingMBean {

    @Inject
    private Configuration configuration;

    @Inject
    private ServerInfoAPI serverInfo;

    @Inject
    private ClusterManagerAPI clusterManager;

    @Inject
    private TimeSource timeSource;

    @Inject
    private Coordinator coordinator;

    @Inject
    private Runner runner;

    @Inject
    private UserSessionManager userSessionManager;

    private Map<ScheduledTask, Long> runningTasks = new ConcurrentHashMap<ScheduledTask, Long>();

    private Map<ScheduledTask, Long> lastStartCache = new HashMap<ScheduledTask, Long>();

    private volatile long schedulingStartTime;

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void processScheduledTasks() {
        if (!AppContext.isStarted() || !isActive())
            return;

        log.debug("Processing scheduled tasks");
        if (schedulingStartTime == 0)
            schedulingStartTime = timeSource.currentTimeMillis();

        try {
            loginOnce();
        } catch (LoginException e) {
            log.error("Unable to login", e);
            return;
        }

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
    public String printActiveScheduledTasks() {
        Coordinator.Context context = coordinator.begin();
        coordinator.end(context);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        StringBuilder sb = new StringBuilder();
        List<ScheduledTask> tasks = context.getTasks();
        for (ScheduledTask task : tasks) {
            sb.append(task);
            if (BooleanUtils.isTrue(task.getSingleton())) {
                sb.append(", lastStart=").append(dateFormat.format(task.getLastStartTime()))
                        .append(" on ").append(task.getLastStartServer());
            } else {
                Long time = lastStartCache.get(task);
                if (time != null) {
                    sb.append(", lastStart=").append(dateFormat.format(new Date(time)));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void processTask(ScheduledTask task) {
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

            long period = task.getPeriod() * 1000;
            long frame = task.getTimeFrame() != null ? task.getTimeFrame() * 1000 : period / 2;

            if (BooleanUtils.isTrue(task.getSingleton())) {
                if (task.getStartDate() != null) {
                    long repetitions = (now - task.getStartDate().getTime()) / period;
                    long currentStart = task.getStartDate().getTime() + repetitions * period;

                    log.trace(task + "\n now=" + now + " frame=" + frame + " repetitions=" + repetitions +
                            " currentStart=" + currentStart + " lastStart=" + task.getLastStart());

                    if (now >= currentStart && now < (currentStart + frame) && task.getLastStart() < currentStart) {
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
                            || (task.getLastStart() + (giveChanceToPreviousHost ? period + period / 2 : period) <= now))
                    {
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
                if (task.getStartDate() != null) {
                    long repetitions = (now - task.getStartDate().getTime()) / period;
                    long currentStart = task.getStartDate().getTime() + repetitions * period;

                    if (log.isTraceEnabled())
                        log.trace(task + "\n now=" + now + " lastStart=" + lastStart + " frame=" + frame
                                + " repetitions=" + repetitions + " currentStart=" + currentStart);

                    if (now >= currentStart && now < (currentStart + frame) && lastStart < currentStart) {
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

    private boolean lastServerWasNotMe(ScheduledTask task, String me) {
        return task.getLastStartServer() != null && !task.getLastStartServer().equals(me);
    }

    private void runSingletonTask(ScheduledTask task, long now, String server) throws LoginException {
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

    private void runTask(ScheduledTask task, long time) throws LoginException {
        lastStartCache.put(task, time);
        runner.runTask(task, time, getUserSession(task));
    }

    private boolean checkFirst(ScheduledTask task, Integer serverPriority, long now) {
        if (serverPriority == null) {
            log.trace(task + ": not in permitted hosts");
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

    private Integer getServerPriority(ScheduledTask task, String serverId) {
        String permittedServers = task.getPermittedServers();

        if (StringUtils.isBlank(permittedServers))
            return 0;

        String[] parts = permittedServers.trim().split("[,;]");
        for (int i = 0; i < parts.length; i++) {
            if (serverId.equals(parts[i].trim())) {
                return i + 1;
            }
        }

        return null;
    }

    private UserSession getUserSession(ScheduledTask task) throws LoginException {
        if (StringUtils.isBlank(task.getUserName())) {
            return userSessionManager.findSession(this.sessionId);
        } else {
            return null;
        }
    }

    private boolean isRunning(ScheduledTask task) {
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
