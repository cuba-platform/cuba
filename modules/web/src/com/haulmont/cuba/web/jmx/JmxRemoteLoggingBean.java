/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.sys.jmx.JmxLogControl;
import com.haulmont.cuba.core.sys.jmx.JmxLogControlMBean;
import com.haulmont.cuba.core.sys.logging.LogControlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.IOException;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(JmxRemoteLoggingAPI.NAME)
public class JmxRemoteLoggingBean implements JmxRemoteLoggingAPI {

    private static final Log log = LogFactory.getLog(JmxRemoteLoggingBean.class);

    @Override
    public List<String> getLogFileNames(JmxInstance instance) {
        final MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);
        JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
        return logControlMBean.getLogFileNames();
    }

    @Override
    public String getTail(JmxInstance instance, String fileName) throws LogControlException {
        final MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);
        JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
        return logControlMBean.getTail(fileName);
    }

    @Override
    public String getLogFileLink(JmxInstance instance, String fileName) throws LogControlException {
        // get link from remote interface and add session id parameter
        final MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);
        JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
        return logControlMBean.getLogFileLink(fileName);
    }

    @Override
    public List<String> getLoggers(JmxInstance instance) {
        final MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);
        JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
        return logControlMBean.getLoggers();
    }

    @Override
    public String getLoggerLevel(JmxInstance instance, String loggerName) throws LogControlException {
        final MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);
        JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
        return logControlMBean.getLoggerLevel(loggerName);
    }

    @Override
    public void setLoggerLevel(JmxInstance instance, String loggerName, String level) throws LogControlException {
        final MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);
        JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
        logControlMBean.setLoggerLevel(loggerName, level);

        log.info(String.format("Level for logger '%s' set to '%s' on '%s'", loggerName, level, instance.getNodeName()));
    }

    @Override
    public List<String> getAppenders(JmxInstance instance) {
        final MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);
        JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
        return logControlMBean.getAppenders();
    }

    @Override
    public String getAppenderThreshold(JmxInstance instance, String appenderName) throws LogControlException {
        final MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);
        JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
        return logControlMBean.getAppenderThreshold(appenderName);
    }

    @Override
    public void setAppenderThreshold(JmxInstance instance, String appenderName, String threshold) throws LogControlException {
        final MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);
        JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
        logControlMBean.setAppenderThreshold(appenderName, threshold);

        log.info(String.format("Threshold for appender '%s' set to '%s' on '%s'", appenderName, threshold, instance.getNodeName()));
    }

    private JmxLogControlMBean getRemoteLogControl(MBeanServerConnection connection) {
        ObjectName objectName;
        try {
            objectName = JmxConnectionHelper.getObjectName(connection, JmxLogControl.class);
        } catch (IOException e) {
            throw new JmxControlException(e);
        }

        if (objectName == null) {
            throw new JmxControlException("Could not find JmxLogControl implementation");
        }

        return JmxConnectionHelper.getProxy(connection, objectName, JmxLogControlMBean.class);
    }
}