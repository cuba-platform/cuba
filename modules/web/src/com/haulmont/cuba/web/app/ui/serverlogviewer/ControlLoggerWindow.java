/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.serverlogviewer;

import com.haulmont.cuba.core.app.LogManagerService;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.web.gui.components.WebGridLayout;
import com.haulmont.cuba.web.gui.components.WebLookupField;
import com.haulmont.cuba.web.gui.components.WebTextField;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import java.util.*;

/**
 * @author shatokhin
 * @version $Id$
 */
public class ControlLoggerWindow extends AbstractWindow {

    int amountLog;
    private WebGridLayout loggersGrid;
    private String PREFIX_ID = "logger";
    private Map<String, Component> loggerMap = new HashMap<>();
    private ServerLogWindow serverLogWindow;
    private List<String> listNewLoggers = new ArrayList<>();

    @Inject
    private LogManagerService logManagerService;

    @Inject
    private OptionsField lookupFieldLogger;

    public ControlLoggerWindow(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        serverLogWindow = ((ServerLogWindow) params.get("controlWin"));

        final ScrollBoxLayout loggersBox;
        loggersBox = getComponent("loggersGroupBox");
        loggersGrid = getComponent("loggersGrid");
        lookupFieldLogger.setOptionsList(getOptionsLoggers());
        lookupFieldLogger.addListener(new ValueListener<Object>() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                for (String id : listNewLoggers) {
                    TextField textField = (TextField) loggersGrid.getComponent(id);
                    if (textField.getValue() == null || textField.getValue().toString().trim().length() == 0) {
                        textField.setValue(value);
                    }
                }
            }
        });

        createFieldsLoggers();
        loggersBox.add(loggersGrid);
    }

    public void setButton() {
        Collection<Component> components = loggersGrid.getComponents();
        for (Component component : components) {
            if (component.getId() != null ? component.getId().startsWith(PREFIX_ID) : false) {
                Object value = ((TextField) component).getValue();
                String logName = value != null ? value.toString() : null;
                if (logName == null || logName.trim().length() == 0) {
                    close(this.getId());
                    continue;
                }
                Level logLevel = logManagerService.getLogLevel(logName);
                Level newLevel = ((LookupField) loggerMap.get(component.getId())).getValue();
                if (logLevel != null) {
                    if (!logLevel.equals(newLevel)) {
                        logManagerService.setLogLevel(logName, newLevel);
                    }
                } else {
                    logManagerService.setLogLevel(logName, newLevel);
                }
            }
        }
        serverLogWindow.showNotification(getMessage("addLogger"), NotificationType.HUMANIZED);
        serverLogWindow.refreshLogs();
        close(this.getId());

    }

    public void cancelButton() {
        close(this.getId());
    }

    public void addLogger() {
        Object value = lookupFieldLogger.getValue();
        if (value != null)
            listNewLoggers.add(addLogger(value.toString(), null, 1, true));
        else
            listNewLoggers.add(addLogger(null, null, 1, true));
    }

    private String addLogger(String name, Level level, int position, boolean isEditable) {
        final WebTextField loggerNameField = new WebTextField();
        loggerNameField.setWidth("300px");
        loggerNameField.setValue(name);
        loggerNameField.setEditable(isEditable);
        String id = PREFIX_ID + UUID.randomUUID().toString();
        loggerNameField.setId(id);

        final WebLookupField levelsField = new WebLookupField();
        levelsField.setWidth("80px");
        levelsField.setOptionsList(ServerLogWindow.getAllLevels());
        levelsField.setValue(level);

        loggerMap.put(id, levelsField);

        loggersGrid.insertRow(position);
        loggersGrid.add(loggerNameField, 0, position);
        loggersGrid.add(levelsField, 1, position);
        return id;
    }

    private List<String> getLogs() {
        List<Logger> listLogs = Collections.list(LogManager.getCurrentLoggers());
        List<String> listNameLogs = new ArrayList<>();
        for (Logger logger : listLogs) {
            if (logger.getLevel() != null) {
                listNameLogs.add(logger.getName());
            }
        }
        Collections.sort(listNameLogs);
        return listNameLogs;
    }

    private void createFieldsLoggers() {
        Level level;
        for (String logName : getLogs()) {
            level = logManagerService.getLogLevel(logName);
            amountLog++;
            addLogger(logName, level, amountLog, false);
        }
        if (amountLog == 0) {
            addLogger(null, null, 1, true);
        }
    }

    private List<String> getOptionsLoggers() {
        List<String> list = getLogs();
        List<String> newList = new ArrayList<>();
        StringTokenizer tokenizer;
        for (String str : list) {
            String nextVariant = "";
            tokenizer = new StringTokenizer(str, ".");
            while (tokenizer.hasMoreTokens()) {
                nextVariant += nextVariant.equals("") ? tokenizer.nextToken() : "." + tokenizer.nextToken();
                if (!list.contains(nextVariant) && !newList.contains(nextVariant)) {
                    newList.add(nextVariant);
                }
            }
            newList.add(str);
        }
        Collections.sort(newList);
        return newList;
    }

}
