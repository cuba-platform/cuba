/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.serverlogviewer;

import com.haulmont.cuba.core.app.LogManagerService;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.gui.components.TextField;
import org.apache.log4j.Level;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author shatokhin
 * @version $Id$
 */
public class AdditionLoggerWindow extends AbstractWindow {

    @Inject
    private TextField loggerNameField;

    @Inject
    private OptionsField logLevelField;

    @Inject
    private LogManagerService logManagerService;

    private ServerLogWindow serverLogWindow;

    public AdditionLoggerWindow(IFrame frame) {
        super(frame);

    }

    @Override
    public void init(Map<String, Object> params) {
        logLevelField.setOptionsList(ServerLogWindow.getAllLevels());
        serverLogWindow = (ServerLogWindow) params.get("winLog");
    }

    public void setButton() {
        if (loggerNameField.getValue() != null && loggerNameField.getValue().toString().trim().length() != 0) {
            if (logLevelField.getValue() != null) {
                String logName = loggerNameField.getValue();
                Level level = logLevelField.getValue();
                logManagerService.setLogLevel(logName, level);
                serverLogWindow.refreshLogs();
                serverLogWindow.showNotification(String.format(getMessage("logSetMessage"), logName, level.toString()), NotificationType.HUMANIZED);
                close(this.getId());
            } else
                showNotification(getMessage("noSelectedLevel"), NotificationType.HUMANIZED);
        } else {
            loggerNameField.setValue(null);
            showNotification(getMessage("noRegName"), NotificationType.HUMANIZED);
        }
    }

    public void cancelButton() {
        close(this.getId());
    }
}
