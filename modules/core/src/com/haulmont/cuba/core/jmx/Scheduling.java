/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.scheduling.SchedulingAPI;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_SchedulingMBean")
public class Scheduling implements SchedulingMBean {

    @Inject
    protected SchedulingAPI scheduling;

    @Override
    public boolean isActive() {
        return scheduling.isActive();
    }

    @Override
    public void setActive(boolean value) {
        scheduling.setActive(value);
    }

    @Override
    public String printActiveScheduledTasks() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        StringBuilder sb = new StringBuilder();
        List<ScheduledTask> tasks = scheduling.getActiveTasks();
        for (ScheduledTask task : tasks) {
            sb.append(task).append(", lastStart=");
            if (task.getLastStartTime() != null) {
                sb.append(dateFormat.format(task.getLastStartTime()));
                if (BooleanUtils.isTrue(task.getSingleton()))
                    sb.append(" on ").append(task.getLastStartServer());
            } else {
                sb.append("<never>");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    @Override
    public String processScheduledTasks() {
        if (!AppContext.isStarted())
            return "Not started yet";

        try {
            scheduling.processScheduledTasks(false);
            return "Done";
        } catch (Throwable e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

}
