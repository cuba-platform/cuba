/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.serverlogviewer;

import com.haulmont.cuba.core.sys.logging.LoggingHelper;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.gui.components.TextField;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author shatokhin
 * @version $Id$
 */
public class AdditionLoggerWindow extends AbstractWindow {

    @Inject
    protected TextField loggerNameField;

    @Inject
    protected OptionsField logLevelField;

    protected Level selectedLevel;

    protected String selectedLoggerName;

    @Override
    public void init(Map<String, Object> params) {
        logLevelField.setOptionsList(LoggingHelper.getLevels());
    }

    public void addLogger() {
        if (StringUtils.isNotBlank(loggerNameField.<String>getValue())) {
            if (logLevelField.getValue() != null) {
                this.selectedLoggerName = loggerNameField.getValue();
                this.selectedLevel = logLevelField.getValue();

                close(COMMIT_ACTION_ID);
            } else
                showNotification(getMessage("logger.notSelectedLevel"), NotificationType.HUMANIZED);
        } else {
            loggerNameField.setValue(null);
            showNotification(getMessage("logger.notSelected"), NotificationType.HUMANIZED);
        }
    }

    public Level getSelectedLevel() {
        return selectedLevel;
    }

    public String getSelectedLoggerName() {
        return selectedLoggerName;
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }
}