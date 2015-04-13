/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.serverlogviewer;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.logging.LogArchiver;
import com.haulmont.cuba.core.sys.logging.LogControlException;
import com.haulmont.cuba.core.sys.logging.LoggingHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.app.ui.jmxinstance.edit.JmxInstanceEditor;
import com.haulmont.cuba.web.export.LogDataProvider;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.JmxControlException;
import com.haulmont.cuba.web.jmx.JmxRemoteLoggingAPI;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Panel;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * @author shatokhin
 * @version $Id$
 */
public class ServerLogWindow extends AbstractWindow {

    private final Log log = LogFactory.getLog(getClass());

    @Inject
    protected CollectionDatasource<JmxInstance, UUID> jmxInstancesDs;

    @Inject
    protected Label localJmxField;

    @Inject
    protected LookupPickerField jmxConnectionField;

    @Inject
    protected JmxRemoteLoggingAPI jmxRemoteLoggingAPI;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    @Inject
    protected GroupBoxLayout logFieldBox;

    @Inject
    protected LookupField logFileNameField;

    @Inject
    protected LookupField loggerLevelField;

    @Inject
    protected LookupField loggerNameField;

    @Inject
    protected LookupField appenderNameField;

    @Inject
    protected LookupField appenderLevelField;

    @Inject
    protected CheckBox autoRefreshCheck;

    @Inject
    protected Label logTailLabel;

    @Inject
    protected ScrollBoxLayout logContainer;

    @Inject
    protected Button downloadButton;

    @Inject
    protected Button showTailButton;

    @Inject
    protected Timer updateLogTailTimer;

    protected JmxInstance localJmxInstance;

    @Override
    public void init(Map<String, Object> params) {
        localJmxField.setValue(jmxControlAPI.getLocalNodeName());
        localJmxField.setEditable(false);

        localJmxInstance = jmxControlAPI.getLocalInstance();

        jmxInstancesDs.refresh();
        jmxConnectionField.setValue(localJmxInstance);
        jmxConnectionField.setRequired(true);
        jmxConnectionField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                JmxInstance jmxInstance = jmxConnectionField.getValue();
                try {
                    refreshHostInfo();
                } catch (JmxControlException e) {
                    showNotification(getMessage("exception.unableToConnectToInterface"), NotificationType.WARNING);
                    if (jmxInstance != localJmxInstance) {
                        jmxConnectionField.setValue(localJmxInstance);
                    }
                }
            }
        });

        autoRefreshCheck.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                if (Boolean.TRUE.equals(value)) {
                    updateLogTailTimer.start();
                } else {
                    updateLogTailTimer.stop();
                }
            }
        });

        jmxConnectionField.removeAllActions();

        jmxConnectionField.addAction(new PickerField.LookupAction(jmxConnectionField) {
            @Override
            public void afterCloseLookup(String actionId) {
                jmxInstancesDs.refresh();
            }
        });

        jmxConnectionField.addAction(new AbstractAction("actions.Add") {
            @Override
            public void actionPerform(Component component) {
                final JmxInstanceEditor instanceEditor = openEditor("sys$JmxInstance.edit", new JmxInstance(), WindowManager.OpenType.DIALOG);
                instanceEditor.addListener(new CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        if (COMMIT_ACTION_ID.equals(actionId)) {
                            jmxInstancesDs.refresh();
                            jmxConnectionField.setValue(instanceEditor.getItem());
                        }
                    }
                });
            }

            @Override
            public String getIcon() {
                return "icons/plus-btn.png";
            }
        });

        com.vaadin.ui.Label vlogTailLabel = WebComponentsHelper.unwrap(logTailLabel);

        vlogTailLabel.setSizeUndefined();
        vlogTailLabel.setContentMode(ContentMode.HTML);
        vlogTailLabel.setStyleName("cuba-log-content");

        loggerLevelField.setOptionsList(LoggingHelper.getLevels());
        appenderLevelField.setOptionsList(LoggingHelper.getLevels());

        refreshHostInfo();

        loggerNameField.addListener(new ValueListener<Object>() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                List<String> currentLoggers = new ArrayList<>(jmxRemoteLoggingAPI.getLoggerNames(getSelectedConnection()));

                Collections.sort(currentLoggers);
                currentLoggers.add(0, getMessage("logger.new"));
                if (value != null && value.equals(currentLoggers.get(0))) {
                    openAddLoggerDialog();
                }
            }
        });

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        UserSession userSession = sessionSource.getUserSession();
        downloadButton.setEnabled(userSession.isSpecificPermitted("cuba.gui.administration.downloadlogs"));

        ComboBox comboBox = WebComponentsHelper.unwrap(logFileNameField);
        comboBox.addShortcutListener(new ShortcutListener("", ShortcutAction.KeyCode.D,
                new int[] {ShortcutAction.ModifierKey.CTRL, ShortcutAction.ModifierKey.SHIFT}) {
            @Override
            public void handleAction(Object sender, Object target) {
                downloadLog();
            }
        });
        comboBox.addShortcutListener(new ShortcutListener("", ShortcutAction.KeyCode.S,
                new int[] {ShortcutAction.ModifierKey.CTRL, ShortcutAction.ModifierKey.SHIFT}) {
            @Override
            public void handleAction(Object sender, Object target) {
                showLogTail();
            }
        });

        downloadButton.setDescription("CTRL-SHIFT-D");
        showTailButton.setDescription("CTRL-SHIFT-S");
    }

    private void refreshHostInfo() {
        JmxRemoteLoggingAPI.LoggingHostInfo hostInfo = jmxRemoteLoggingAPI.getHostInfo(getSelectedConnection());
        refreshLoggers(hostInfo);
        refreshAppenders(hostInfo);
        refreshLogFileNames(hostInfo);
    }

    protected void openAddLoggerDialog() {
        final AdditionLoggerWindow additionLogger = openWindow("serverLogAddLoggerDialog", WindowManager.OpenType.DIALOG);
        additionLogger.addListener(new CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                if (COMMIT_ACTION_ID.equals(actionId)) {
                    Level level = additionLogger.getSelectedLevel();
                    String loggerName = additionLogger.getSelectedLoggerName();

                    try {
                        jmxRemoteLoggingAPI.setLoggerLevel(getSelectedConnection(), loggerName, level.toString());
                    } catch (LogControlException | JmxControlException e) {
                        log.error(e);
                        showNotification(getMessage("exception.logControl"), NotificationType.ERROR);
                    }

                    showNotification(String.format(getMessage("logger.setMessage"), loggerName, level.toString()),
                            NotificationType.HUMANIZED);

                    refreshLoggers();
                }
            }
        });
        loggerNameField.setValue(null);
        loggerLevelField.setValue(null);
    }

    public void showLogTail() {
        updateLogTail(false);
    }

    public void updateLogTail(boolean isTimedEvent) {
        if (logFileNameField.getValue() != null) {
            String logFileName = logFileNameField.getValue();
            String value;

            try {
                value = jmxRemoteLoggingAPI.getTail(getSelectedConnection(), logFileName);
            } catch (LogControlException | JmxControlException e) {
                log.error(e);
                if (!isTimedEvent)
                    showNotification(getMessage("exception.logControl"), NotificationType.ERROR);
                return;
            }

            // transform to XHTML
            value = StringEscapeUtils.escapeHtml(value);
            value = StringUtils.replace(value, " ", "&nbsp;");

            // highlight log
            StringBuilder coloredLog = new StringBuilder();
            BufferedReader reader = new BufferedReader(new StringReader(value));
            try {
                List<String> logLevels = new LinkedList<>();
                // highlight tomcat catalina levels
                logLevels.add("WARNING");
                logLevels.add("SEVERE");
                // highlight log4j levels
                for (Level level : LoggingHelper.getLevels()) {
                    logLevels.add(level.toString());
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    // replace one level per line
                    for (String level : logLevels) {
                        String highlightedLine = highlightLevel(line, level);
                        if (!StringUtils.equals(highlightedLine, line)) {
                            line = highlightedLine;
                            break;
                        }
                    }
                    coloredLog.append(line).append("<br/>");
                }
            } catch (IOException e) {
                log.warn(e);
                return;
            }
            logTailLabel.setValue(coloredLog.toString());
        } else {
            showNotification(getMessage("log.notSelected"), NotificationType.HUMANIZED);
        }

        Panel vlogContainer = WebComponentsHelper.unwrap(logContainer);

        int scrollPos = vlogContainer.getScrollTop() + 30000;
        vlogContainer.setScrollTop(scrollPos);
    }

    public void getLoggerLevel() {
        if (StringUtils.isNotEmpty(loggerNameField.<String>getValue())) {
            String loggerName = loggerNameField.getValue();
            String level;

            try {
                level = jmxRemoteLoggingAPI.getLoggerLevel(getSelectedConnection(), loggerName);
            } catch (LogControlException | JmxControlException e) {
                log.error(e);
                showNotification(getMessage("exception.logControl"), NotificationType.ERROR);
                return;
            }

            if (level != null)
                loggerLevelField.setValue(LoggingHelper.getLevelFromString(level));
        } else {
            loggerNameField.setValue(null);
            showNotification(getMessage("logger.notSelected"), NotificationType.HUMANIZED);
        }
    }

    public void setLoggerLevel() {
        if (StringUtils.isNotEmpty(loggerNameField.<String>getValue())) {
            if (loggerLevelField.getValue() != null) {
                String loggerName = loggerNameField.getValue();
                Level level = loggerLevelField.getValue();

                try {
                    jmxRemoteLoggingAPI.setLoggerLevel(getSelectedConnection(), loggerName, level.toString());
                } catch (LogControlException | JmxControlException e) {
                    log.error(e);
                    showNotification(getMessage("exception.logControl"), NotificationType.ERROR);
                    return;
                }

                showNotification(formatMessage("logger.setMessage", loggerName, level.toString()), NotificationType.HUMANIZED);
            } else
                showNotification(getMessage("logger.notSelectedLevel"), NotificationType.HUMANIZED);
        } else {
            loggerNameField.setValue(null);
            showNotification(getMessage("logger.notSelected"), NotificationType.HUMANIZED);
        }
    }

    public void getAppenderLevel() {
        if (StringUtils.isNotEmpty(appenderNameField.<String>getValue())) {
            String appenderName = appenderNameField.getValue();
            String threshold;

            try {
                threshold = jmxRemoteLoggingAPI.getAppenderThreshold(getSelectedConnection(), appenderName);
            } catch (LogControlException | JmxControlException e) {
                log.error(e);
                showNotification(getMessage("exception.logControl"), NotificationType.ERROR);
                return;
            }

            if (threshold != null)
                appenderLevelField.setValue(LoggingHelper.getLevelFromString(threshold));
        } else {
            appenderLevelField.setValue(null);
            showNotification(getMessage("appender.notSelected"), NotificationType.HUMANIZED);
        }
    }

    public void setAppenderLevel() {
        if (StringUtils.isNotEmpty(appenderNameField.<String>getValue())) {
            if (appenderLevelField.getValue() != null) {
                String appenderName = appenderNameField.getValue();
                Level threshold = appenderLevelField.getValue();

                try {
                    jmxRemoteLoggingAPI.setAppenderThreshold(getSelectedConnection(), appenderName, threshold.toString());
                } catch (LogControlException | JmxControlException e) {
                    log.error(e);
                    showNotification(getMessage("exception.logControl"), NotificationType.ERROR);
                    return;
                }

                showNotification(formatMessage("appender.setMessage", appenderName, threshold.toString()), NotificationType.HUMANIZED);
            } else
                showNotification(getMessage("appender.notSelectedThreshold"), NotificationType.HUMANIZED);
        } else {
            appenderNameField.setValue(null);
            showNotification(getMessage("appender.notSelected"), NotificationType.HUMANIZED);
        }
    }

    public void downloadLog() {
        final String fileName = logFileNameField.getValue();
        if (fileName != null) {
            try {
                final JmxInstance selectedConnection = getSelectedConnection();
                // check if we have many suitable JmxControlBean instances
                // show dialog with context select and size options if needed
                List<String> availableContexts = jmxRemoteLoggingAPI.getAvailableContexts(selectedConnection);

                long size = jmxRemoteLoggingAPI.getLogFileSize(selectedConnection, fileName);

                if (size <= LogArchiver.LOG_TAIL_FOR_PACKING_SIZE && availableContexts.size() == 1) {
                    ExportDisplay exportDisplay = AppConfig.createExportDisplay(this);
                    exportDisplay.show(new LogDataProvider(selectedConnection, fileName), fileName + ".zip");
                } else {
                    openWindow("serverLogDownloadOptionsDialog",
                               WindowManager.OpenType.DIALOG,
                               ParamsMap.of("logFileName", fileName,
                                            "connection", selectedConnection,
                                            "logFileSize", size,
                                            "remoteContextList", availableContexts));
                }
            } catch (RuntimeException | LogControlException e) {
                showNotification(getMessage("exception.logControl"), NotificationType.ERROR);
                log.error(e);
            }
        } else {
            showNotification(getMessage("log.notSelected"), NotificationType.HUMANIZED);
        }
    }

    public void updateLogTail(@SuppressWarnings("unused") Timer timer) {
        updateLogTail(true);
    }

    public void openLoggerControlDialog() {
        Map<String, Object> params = new HashMap<>();
        final Map<String, Level> loggersMap = new HashMap<>();
        Map<String, String> loggersLevels = jmxRemoteLoggingAPI.getLoggersLevels(getSelectedConnection());

        for (Map.Entry<String, String> log : loggersLevels.entrySet()) {
            loggersMap.put(log.getKey(), LoggingHelper.getLevelFromString(log.getValue()));
        }

        params.put("loggersMap", loggersMap);

        final ControlLoggerWindow controlLogger = openWindow("serverLogLoggerControlDialog", WindowManager.OpenType.DIALOG, params);
        controlLogger.addListener(new CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                if (COMMIT_ACTION_ID.equals(actionId)) {
                    Map<String, Level> levels = controlLogger.getLevels();
                    try {
                        Map<String, String> updates = new HashMap<>();
                        for (Map.Entry<String, Level> levelEntry : levels.entrySet()) {
                            String loggerName = levelEntry.getKey();
                            Level newLogLevel = levelEntry.getValue();

                            Level prevLevel = loggersMap.get(loggerName);
                            String logLevel = prevLevel == null ? null : prevLevel.toString();

                            if (!StringUtils.equals(logLevel, newLogLevel.toString())) {
                                updates.put(loggerName, newLogLevel.toString());
                            }
                        }

                        if (!updates.isEmpty()) {
                            jmxRemoteLoggingAPI.setLoggersLevels(getSelectedConnection(), updates);
                        }
                    } catch (LogControlException | JmxControlException e) {
                        log.error(e);
                        showNotification(getMessage("exception.logControl"), NotificationType.ERROR);
                    }
                    showNotification(getMessage("logger.control.apply"), NotificationType.HUMANIZED);
                    refreshLoggers();
                }
            }
        });
    }

    protected String highlightLevel(String line, String level) {
        // use css classes for highlight different log levels
        return line.replaceFirst(level,
                "<span class='cuba-log-level cuba-log-level-" + level + "'>" + level + "</span>");
    }

    protected JmxInstance getSelectedConnection() {
        return jmxConnectionField.getValue();
    }

    protected void refreshLoggers() {
        refreshLoggers(jmxRemoteLoggingAPI.getHostInfo(getSelectedConnection()));
    }

    protected void refreshLoggers(JmxRemoteLoggingAPI.LoggingHostInfo hostInfo) {
        List<String> loggers = new ArrayList<>(hostInfo.getLoggerNames());

        Collections.sort(loggers);
        loggers.add(0, getMessage("logger.new"));
        loggerNameField.setOptionsList(loggers);

        loggerNameField.setValue(null);
        loggerLevelField.setValue(null);
    }

    protected void refreshAppenders(JmxRemoteLoggingAPI.LoggingHostInfo hostInfo) {
        List<String> appenders = hostInfo.getAppenders();

        Collections.sort(appenders);
        appenderNameField.setOptionsList(appenders);

        appenderNameField.setValue(null);
        appenderLevelField.setValue(null);
    }

    protected void refreshLogFileNames(JmxRemoteLoggingAPI.LoggingHostInfo hostInfo) {
        // try to keep previously selected file name
        String selectedFileName = logFileNameField.getValue();

        List<String> logFiles = hostInfo.getLogFileNames();
        logFileNameField.setValue(null);
        logFileNameField.setOptionsList(logFiles);

        autoRefreshCheck.setValue(false);
        if (selectedFileName != null && logFiles.contains(selectedFileName)) {
            logFileNameField.setValue(selectedFileName);
        }

        logTailLabel.setValue("");
    }
}