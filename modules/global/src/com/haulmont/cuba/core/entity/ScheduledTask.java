/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import org.apache.commons.lang.BooleanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Entity that stores an information about a scheduled task.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Entity(name = "core$ScheduledTask")
@Table(name = "SYS_SCHEDULED_TASK")
@NamePattern("%s.%s|beanName,methodName")
public class ScheduledTask extends BaseUuidEntity implements Updatable, SoftDelete {

    private static final long serialVersionUID = -2330884126746644884L;

    // ScheduledTask has no @Version field because it is locked pessimistically when processed.
    // Moreover unfortunately OpenJPA issues a lot of unnecessary "select version from ..." when loads versioned
    // objects with PESSIMISTIC lock type.

    @Column(name = "UPDATE_TS")
    protected Date updateTs;

    @Column(name = "UPDATED_BY", length = LOGIN_FIELD_LEN)
    protected String updatedBy;

    @Column(name = "DELETE_TS")
    protected Date deleteTs;

    @Column(name = "DELETED_BY", length = LOGIN_FIELD_LEN)
    protected String deletedBy;

    @Column(name = "BEAN_NAME")
    protected String beanName;

    @Column(name = "METHOD_NAME")
    protected String methodName;

    @Column(name = "USER_NAME")
    protected String userName;

    @Column(name = "USER_PASSWORD")
    protected String userPassword;

    @Column(name = "IS_SINGLETON")
    protected Boolean singleton;

    @Column(name = "IS_ACTIVE")
    protected Boolean active;

    @Column(name = "PERIOD")
    protected Integer period;

    @Column(name = "TIMEOUT")
    protected Integer timeout;

    @Column(name = "START_DATE")
    protected Date startDate;

    @Column(name = "TIME_FRAME")
    protected Integer timeFrame;

    @Column(name = "START_DELAY")
    protected Integer startDelay;

    @Column(name = "PERMITTED_SERVERS")
    protected String permittedServers;

    @Column(name = "LOG_START")
    protected Boolean logStart;

    @Column(name = "LOG_FINISH")
    protected Boolean logFinish;

    @Column(name = "LAST_START_TIME")
    protected Date lastStartTime;

    @Column(name = "LAST_START_SERVER")
    protected String lastStartServer;

    public Date getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean isDeleted() {
        return deleteTs != null;
    }

    public Date getDeleteTs() {
        return deleteTs;
    }

    public void setDeleteTs(Date deleteTs) {
        this.deleteTs = deleteTs;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Boolean getSingleton() {
        return singleton;
    }

    public void setSingleton(Boolean singleton) {
        this.singleton = singleton;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(Integer timeFrame) {
        this.timeFrame = timeFrame;
    }

    public Integer getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(Integer startDelay) {
        this.startDelay = startDelay;
    }

    public String getPermittedServers() {
        return permittedServers;
    }

    public void setPermittedServers(String permittedServers) {
        this.permittedServers = permittedServers;
    }

    public Boolean getLogStart() {
        return logStart;
    }

    public void setLogStart(Boolean logStart) {
        this.logStart = logStart;
    }

    public Boolean getLogFinish() {
        return logFinish;
    }

    public void setLogFinish(Boolean logFinish) {
        this.logFinish = logFinish;
    }

    public Date getLastStartTime() {
        return lastStartTime;
    }

    public void setLastStartTime(Date lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public long getLastStart() {
        return lastStartTime == null ? 0 : lastStartTime.getTime();
    }

    public String getLastStartServer() {
        return lastStartServer;
    }

    public void setLastStartServer(String lastStartServer) {
        this.lastStartServer = lastStartServer;
    }

    @Override
    public String toString() {
        return "ScheduledTask{" +
                beanName + '.' + methodName +
                (BooleanUtils.isTrue(singleton) ? ", singleton" : "") +
                ", period=" + period +
                (startDate != null ? ", startDate=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(startDate) : "") +
                '}';
    }
}
