/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

/**
 * @author krivenko
 * @version $Id$
 */
@MetaClass(name = "stat$ThreadSnapshot")
@SystemLevel
public class ThreadSnapshot extends AbstractNotPersistentEntity {

    private Long threadId;

    @MetaProperty
    private String name;

    @MetaProperty
    private String status;

    @MetaProperty
    private Double cpu;

    @MetaProperty
    private Boolean deadLocked;

    @MetaProperty
    private String stackTrace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Boolean getDeadLocked() {
        return deadLocked;
    }

    public void setDeadLocked(Boolean deadLocked) {
        this.deadLocked = deadLocked;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
