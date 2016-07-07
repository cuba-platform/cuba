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
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.sys.logging.LoggingHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ControlLoggerWindow extends AbstractWindow {

    @Inject
    protected TextField newLoggerTextField;

    @Inject
    protected GridLayout loggersGrid;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected ComponentsFactory componentsFactory;

    protected final Map<String, HBoxLayout> fieldMap = new HashMap<>();

    protected final Map<String, Level> levels = new HashMap<>();

    protected Map<String, Level> loggersMap = new HashMap<>();

    @Override
    public void init(Map<String, Object> params) {
        getDialogOptions()
                .setWidth(themeConstants.getInt("cuba.web.ControlLoggerWindow.width"))
                .setHeight(themeConstants.getInt("cuba.web.ControlLoggerWindow.height"))
                .setResizable(true);

        loggersMap = (Map<String, Level>) params.get("loggersMap");
        fillLoggerGrid();

        WebComponentsHelper.addEnterShortcut(newLoggerTextField, this::filterLogger);
    }

    protected void fillLoggerGrid() {
        loggersGrid.removeAll();

        for (Map.Entry<String, Level> levelEntry : loggersMap.entrySet()) {
            String loggerName = levelEntry.getKey();
            Level level;
            if (levels.get(loggerName) != null && levelEntry.getValue() != levels.get(loggerName)) {
                level = levels.get(loggerName);
            } else {
                level = levelEntry.getValue();
            }

            Pair<TextField, HBoxLayout> editComponents = createEditComponents(loggerName, level);
            fieldMap.put(loggerName, editComponents.getSecond());

            loggersGrid.add(editComponents.getFirst());
            loggersGrid.add(editComponents.getSecond());
        }
    }

    public void apply() {
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

    protected Pair<TextField, HBoxLayout> createEditComponents(String loggerName, Level level) {
        final TextField loggerNameField = componentsFactory.createComponent(TextField.class);
        loggerNameField.setValue(loggerName);
        loggerNameField.setEditable(false);
        loggerNameField.setFrame(this);
        loggerNameField.setWidth("100%");

        HBoxLayout buttonField = componentsFactory.createComponent(HBoxLayout.class);
        buttonField.setSpacing(true);

        for (Level logLevel : LoggingHelper.getLevels()) {
            if (logLevel != Level.OFF && logLevel != Level.ALL) {
                Button button = componentsFactory.createComponent(Button.class);
                button.setAction(
                        new AbstractAction("setLevel") {
                            @Override
                            public void actionPerform(Component component) {
                                levels.put(loggerName, logLevel);
                                HBoxLayout buttonPanel = (HBoxLayout) button.getParent();
                                for (Component childButton : buttonPanel.getComponents()) {
                                    if (childButton instanceof Button) {
                                        childButton.setStyleName("cuba-logger-level loglevel-" + logLevel.toString());
                                    }
                                }
                                button.setStyleName("cuba-logger-level loglevel-" + logLevel.toString() + " currentlevel");
                            }
                        });

                button.setCaption(logLevel.toString());
                if (logLevel == level) {
                    button.setStyleName("cuba-logger-level loglevel-" + logLevel.toString() + " currentlevel");
                } else {
                    button.setStyleName("cuba-logger-level loglevel-" + logLevel.toString());
                }
                buttonField.add(button);
            }
        }

        return new Pair<>(loggerNameField, buttonField);
    }

    protected void addLogger(String loggerName, Level level) {
        Pair<TextField, HBoxLayout> editComponents = createEditComponents(loggerName, level);
        fieldMap.put(loggerName, editComponents.getSecond());

        com.vaadin.ui.GridLayout vGrid = (com.vaadin.ui.GridLayout) WebComponentsHelper.unwrap(loggersGrid);
        vGrid.insertRow(1);
        loggersGrid.add(editComponents.getFirst(), 0, 1);
        loggersGrid.add(editComponents.getSecond(), 1, 1);
    }

    public void filterLogger() {
        if (newLoggerTextField.getValue() == null) {
            fillLoggerGrid();
        } else {
            loggersGrid.removeAll();

            for (Map.Entry<String, Level> levelEntry : loggersMap.entrySet()) {
                String keyword = newLoggerTextField.getValue().toString();
                String loggerName = levelEntry.getKey();

                if (loggerName.toLowerCase().contains(keyword.toLowerCase())) {
                    Level level = levelEntry.getValue();

                    Pair<TextField, HBoxLayout> editComponents = createEditComponents(loggerName, level);

                    fieldMap.put(loggerName, editComponents.getSecond());

                    loggersGrid.add(editComponents.getFirst());
                    loggersGrid.add(editComponents.getSecond());
                }
            }
        }
    }
}