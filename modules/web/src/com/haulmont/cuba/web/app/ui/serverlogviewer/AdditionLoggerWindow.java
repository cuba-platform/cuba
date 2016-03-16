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

package com.haulmont.cuba.web.app.ui.serverlogviewer;

import ch.qos.logback.classic.Level;
import com.haulmont.cuba.core.sys.logging.LoggingHelper;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.gui.components.TextField;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.Map;

/**
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