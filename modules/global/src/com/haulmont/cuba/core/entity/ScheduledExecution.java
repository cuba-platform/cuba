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

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Entity that reflects the fact of a {@link ScheduledTask} execution.
 *
 */
@Entity(name = "sys$ScheduledExecution")
@Table(name = "SYS_SCHEDULED_EXECUTION")
@SystemLevel
public class ScheduledExecution extends BaseUuidEntity {

    private static final long serialVersionUID = -3891325977986519747L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID")
    protected ScheduledTask task;

    @Column(name = "SERVER")
    protected String server;

    @Column(name = "START_TIME")
    protected Date startTime;

    @Column(name = "FINISH_TIME")
    protected Date finishTime;

    @Column(name = "RESULT")
    protected String result;

    public ScheduledTask getTask() {
        return task;
    }

    public void setTask(ScheduledTask task) {
        this.task = task;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @MetaProperty
    public Long getDurationSec() {
        if (finishTime == null || startTime == null)
            return 0L;

        return (finishTime.getTime() - startTime.getTime()) / 1000;
    }

    @Override
    public String toString() {
        return "ScheduledExecution{" +
                "task=" + task +
                ", host='" + server + '\'' +
                ", startTime=" + startTime +
                (finishTime != null ? ", finishTime=" : "") +
                '}';
    }
}