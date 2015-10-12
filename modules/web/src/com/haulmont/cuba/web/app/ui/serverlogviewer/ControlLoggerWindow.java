/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.serverlogviewer;

import ch.qos.logback.classic.Level;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.sys.logging.LoggingHelper;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.GridLayout;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import org.apache.commons.lang.StringUtils;

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

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected ComponentsFactory componentsFactory;

    protected final Map<String, LookupField> fieldMap = new HashMap<>();

    protected final Map<String, Level> levels = new HashMap<>();

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams()
                .setWidth(themeConstants.getInt("cuba.web.ControlLoggerWindow.width"))
                .setHeight(themeConstants.getInt("cuba.web.ControlLoggerWindow.height"))
                .setResizable(true);

        @SuppressWarnings("unchecked")
        Map<String, Level> loggersMap = (Map<String, Level>) params.get("loggersMap");

        for (Map.Entry<String, Level> levelEntry : loggersMap.entrySet()) {
            String loggerName = levelEntry.getKey();
            Level level = levelEntry.getValue();

            Pair<TextField, LookupField> editComponents = createEditComponents(loggerName, level);

            fieldMap.put(loggerName, editComponents.getSecond());

            loggersGrid.add(editComponents.getFirst());
            loggersGrid.add(editComponents.getSecond());
        }
    }

    public void apply() {
        levels.clear();

        for (Map.Entry<String, LookupField> logEntry : fieldMap.entrySet()) {
            String loggerName = logEntry.getKey();
            Level logLevel = logEntry.getValue().getValue();
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
        final TextField loggerNameField = componentsFactory.createComponent(TextField.class);
        loggerNameField.setValue(loggerName);
        loggerNameField.setEditable(false);
        loggerNameField.setFrame(this);
        loggerNameField.setWidth("100%");

        final LookupField logLevelField = componentsFactory.createComponent(LookupField.class);
        logLevelField.setWidth(themeConstants.get("cuba.web.ControlLoggerWindow.logLevelField.width"));
        logLevelField.setOptionsList(LoggingHelper.getLevels());
        logLevelField.setValue(level);
        logLevelField.setFrame(this);

        return new Pair<>(loggerNameField, logLevelField);
    }

    protected void addLogger(String loggerName, Level level) {
        Pair<TextField, LookupField> editComponents = createEditComponents(loggerName, level);

        fieldMap.put(loggerName, editComponents.getSecond());

        com.vaadin.ui.GridLayout vGrid = (com.vaadin.ui.GridLayout) WebComponentsHelper.unwrap(loggersGrid);
        vGrid.insertRow(1);
        loggersGrid.add(editComponents.getFirst(), 0, 1);
        loggersGrid.add(editComponents.getSecond(), 1, 1);
    }
}