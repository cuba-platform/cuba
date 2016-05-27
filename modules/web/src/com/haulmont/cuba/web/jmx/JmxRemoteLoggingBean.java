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

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.sys.jmx.JmxLogControl;
import com.haulmont.cuba.core.sys.jmx.JmxLogControlMBean;
import com.haulmont.cuba.core.sys.logging.LogControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.haulmont.cuba.web.jmx.JmxConnectionHelper.withConnection;

@Component(JmxRemoteLoggingAPI.NAME)
public class JmxRemoteLoggingBean implements JmxRemoteLoggingAPI {

    private static final Logger log = LoggerFactory.getLogger(JmxRemoteLoggingBean.class);

    @Override
    public List<String> getLogFileNames(JmxInstance instance) {
        return withConnection(instance, new JmxAction<List<String>>() {
            @Override
            public List<String> perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                return logControlMBean.getLogFileNames();
            }
        });
    }

    @Override
    public String getTail(JmxInstance instance, final String fileName) throws LogControlException {
        return withConnection(instance, new JmxAction<String>() {
            @Override
            public String perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                return logControlMBean.getTail(fileName);
            }
        });
    }

    @Override
    public String getLogFileLink(JmxInstance instance, final String fileName) throws LogControlException {
        return withConnection(instance, new JmxAction<String>() {
            @Override
            public String perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                return logControlMBean.getLogFileLink(fileName);
            }
        });
    }

    @Override
    public String getLogFileLink(JmxInstance instance, final String remoteContext,
                                 final String fileName) throws LogControlException {
        return withConnection(instance, new JmxAction<String>() {
            @Override
            public String perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection, remoteContext);
                return logControlMBean.getLogFileLink(fileName);
            }
        });
    }

    @Override
    public long getLogFileSize(JmxInstance instance, final String fileName) throws LogControlException {
        return withConnection(instance, new JmxAction<Long>() {
            @Override
            public Long perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                return logControlMBean.getLogFileSize(fileName);
            }
        });
    }

    @Override
    public List<String> getLoggerNames(JmxInstance instance) {
        return withConnection(instance, new JmxAction<List<String>>() {
            @Override
            public List<String> perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                return logControlMBean.getLoggerNames();
            }
        });
    }

    @Override
    public Map<String, String> getLoggersLevels(JmxInstance instance) {
        return withConnection(instance, new JmxAction<Map<String, String>>() {
            @Override
            public Map<String, String> perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                return logControlMBean.getLoggersLevels();
            }
        });
    }

    @Override
    public String getLoggerLevel(JmxInstance instance, final String loggerName) throws LogControlException {
        return withConnection(instance, new JmxAction<String>() {
            @Override
            public String perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                return logControlMBean.getLoggerLevel(loggerName);
            }
        });
    }

    @Override
    public void setLoggerLevel(JmxInstance instance, final String loggerName, final String level)
            throws LogControlException {
        withConnection(instance, new JmxAction<Void>() {
            @Override
            public Void perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                logControlMBean.setLoggerLevel(loggerName, level);
                return null;
            }
        });

        log.info(String.format("Level for logger '%s' set to '%s' on '%s'", loggerName, level, instance.getNodeName()));
    }

    @Override
    public void setLoggersLevels(JmxInstance instance, final Map<String, String> updates) throws LogControlException {
        withConnection(instance, new JmxAction<Void>() {
            @Override
            public Void perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                logControlMBean.setLoggersLevels(updates);
                return null;
            }
        });

        for (Map.Entry<String, String> logger : updates.entrySet()) {
            log.info(String.format("Level for logger '%s' set to '%s' on '%s'",
                    logger.getKey(), logger.getKey(), instance.getNodeName()));
        }
    }

    @Override
    public List<String> getAppenders(JmxInstance instance) {
        return withConnection(instance, new JmxAction<List<String>>() {
            @Override
            public List<String> perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                logControlMBean.getAppenders();
                return null;
            }
        });
    }

    @Override
    public String getAppenderThreshold(JmxInstance instance, final String appenderName) throws LogControlException {
        return withConnection(instance, new JmxAction<String>() {
            @Override
            public String perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                return logControlMBean.getAppenderThreshold(appenderName);
            }
        });
    }

    @Override
    public void setAppenderThreshold(JmxInstance instance, final String appenderName, final String threshold)
            throws LogControlException {
        withConnection(instance, new JmxAction<Void>() {
            @Override
            public Void perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                logControlMBean.setAppenderThreshold(appenderName, threshold);
                return null;
            }
        });

        log.info(String.format("Threshold for appender '%s' set to '%s' on '%s'", appenderName, threshold, instance.getNodeName()));
    }

    @Override
    public LoggingHostInfo getHostInfo(JmxInstance instance) {
        return withConnection(instance, new JmxAction<LoggingHostInfo>() {
            @Override
            public LoggingHostInfo perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                JmxLogControlMBean logControlMBean = getRemoteLogControl(connection);
                return new LoggingHostInfo(
                        logControlMBean.getLoggerNames(),
                        logControlMBean.getAppenders(),
                        logControlMBean.getLogFileNames()
                );
            }
        });
    }

    @Override
    public List<String> getAvailableContexts(JmxInstance instance) {
        return withConnection(instance, new JmxAction<List<String>>() {
            @Override
            public List<String> perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                Collection<ObjectName> objectNames =
                        JmxConnectionHelper.getSuitableObjectNames(connection, JmxLogControl.class);
                List<String> contexts = new ArrayList<>();
                for (ObjectName objectName : objectNames) {
                    contexts.add(objectName.getDomain());
                }
                return contexts;
            }
        });
    }

    protected JmxLogControlMBean getRemoteLogControl(MBeanServerConnection connection) {
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

    protected JmxLogControlMBean getRemoteLogControl(MBeanServerConnection connection, String remoteContext) {
        ObjectName objectName;
        try {
            objectName = JmxConnectionHelper.getObjectName(connection, remoteContext, JmxLogControl.class);
        } catch (IOException e) {
            throw new JmxControlException(e);
        }

        if (objectName == null) {
            throw new JmxControlException("Could not find JmxLogControl implementation");
        }

        return JmxConnectionHelper.getProxy(connection, objectName, JmxLogControlMBean.class);
    }
}