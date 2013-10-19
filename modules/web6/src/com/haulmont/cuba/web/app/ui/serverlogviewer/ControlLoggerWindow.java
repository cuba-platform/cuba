/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.serverlogviewer;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.sys.logging.LoggingHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.GridLayout;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.web.gui.components.WebGridLayout;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shatokhin
 * @version $Id$
 */
public class ControlLoggerWindow extends AbstractWindow {

    @Inject
    protected TextField newLoggerTextField;

    @Inject
    protected GridLayout loggersGrid;

    protected final Map<String, LookupField> fieldMap = new HashMap<>();

    protected final Map<String, Level> levels = new HashMap<>();

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams()
                .setWidth(480)
                .setHeight(420)
                .setResizable(true);

        Map<String, Level> loggersMap = (Map<String, Level>) params.get("loggersMap");

        for (String loggerName : loggersMap.keySet()) {
            Level level = loggersMap.get(loggerName);

            Pair<TextField, LookupField> editComponents = createEditComponents(loggerName, level);

            fieldMap.put(loggerName, editComponents.getSecond());

            loggersGrid.add(editComponents.getFirst());
            loggersGrid.add(editComponents.getSecond());
        }
    }

    public void apply() {
        levels.clear();
        for (String loggerName : fieldMap.keySet()) {
            Level logLevel = fieldMap.get(loggerName).getValue();

            levels.put(loggerName, logLevel);
        }
        close(COMMIT_ACTION_ID);
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }

    public void addLogger() {
        String loggerName = newLoggerTextField.getValue();
        if (StringUtils.isNotBlank(loggerName) && !fieldMap.containsKey(loggerName)) {
            addLogger(loggerName, Level.INFO);
        }
    }

    public Map<String, Level> getLevels() {
        return Collections.unmodifiableMap(levels);
    }

    protected Pair<TextField, LookupField> createEditComponents(String loggerName, Level level) {
        final TextField loggerNameField = AppConfig.getFactory().createComponent(TextField.NAME);
        loggerNameField.setValue(loggerName);
        loggerNameField.setEditable(false);
        loggerNameField.setFrame(this);
        loggerNameField.setWidth("100%");

        final LookupField logLevelField = AppConfig.getFactory().createComponent(LookupField.NAME);
        logLevelField.setWidth("80px");
        logLevelField.setOptionsList(LoggingHelper.getLevels());
        logLevelField.setValue(level);
        logLevelField.setFrame(this);

        return new Pair<>(loggerNameField, logLevelField);
    }

    protected void addLogger(String loggerName, Level level) {
        Pair<TextField, LookupField> editComponents = createEditComponents(loggerName, level);

        fieldMap.put(loggerName, editComponents.getSecond());

        ((WebGridLayout) loggersGrid).insertRow(1);
        loggersGrid.add(editComponents.getFirst(), 0, 1);
        loggersGrid.add(editComponents.getSecond(), 1, 1);
    }
}