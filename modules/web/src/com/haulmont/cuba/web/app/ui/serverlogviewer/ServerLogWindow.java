/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.serverlogviewer;

import com.haulmont.cuba.core.app.LogManagerService;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.SimpleFileDataProvider;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import org.apache.log4j.Level;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author shatokhin
 */
public class ServerLogWindow extends AbstractWindow {

    @Inject
    private LogManagerService logManagerService;

    @Inject
    private BoxLayout logFieldBox;

    @Inject
    private OptionsField logFileNamesField;

    @Inject
    private OptionsField levelField;

    @Inject
    private Field logNameField;

    @Inject
    private CheckBox autoRefreshCheck;

    private final Label vLabel = new Label();
    private final Panel panel = new Panel();


    public ServerLogWindow(IFrame frame) {
        super(frame);
        setCaption(getMessage("serverLog"));
    }

    @Override
    public void init(Map<String, Object> params) {
        logFileNamesField.setOptionsList(logManagerService.getLogFileNames());

        panel.setSizeFull();
        panel.setScrollable(true);

        AbstractOrderedLayout vBox = (AbstractOrderedLayout) WebComponentsHelper.unwrap(logFieldBox);
        panel.addComponent(vLabel);
        vBox.addComponent(panel);
        vLabel.setReadOnly(true);
        vLabel.setSizeFull();
        vLabel.setContentMode(Label.CONTENT_XHTML);

        levelField.setOptionsList(getAllLevels());

        initTimers();
    }

    public void showTail() {
        fillingTextArea();
    }

    public void getLevel() {
        Level level = logManagerService.getLogLevel(logNameField.<String>getValue());
        levelField.setValue(level);
    }

    public void setLevel() {
        String logName = logNameField.getValue();
        Level level = levelField.getValue();
        logManagerService.setLogLevel(logName, level);
        showNotification(String.format(getMessage("logSetMessage"), logName, level.toString()), NotificationType.HUMANIZED);
    }

    public void download() {
        String pathZipFile = logManagerService.packLog(logFileNamesField.<String>getValue());
        if (pathZipFile != null) {
            AppConfig.createExportDisplay().show(new SimpleFileDataProvider(pathZipFile), pathZipFile);
            logManagerService.deleteTempFile(pathZipFile);
        } else {
            showNotification(getMessage("fileDownload"), NotificationType.HUMANIZED);
        }
    }

    private void initTimers() {
        Timer timer = getTimer("timer");

        timer.addTimerListener(new Timer.TimerListener() {
            @Override
            public void onTimer(Timer timer) {
                boolean flag = (Boolean.parseBoolean(autoRefreshCheck.getValue().toString()));
                if (flag) {
                    fillingTextArea();
                }
            }

            @Override
            public void onStopTimer(Timer timer) {

            }
        });
    }

    private void fillingTextArea() {
        vLabel.setReadOnly(false);

        if (logFileNamesField.getValue() != null) {
            String selectValue = logFileNamesField.getValue();
            String value = logManagerService.getTail(selectValue);
            vLabel.setValue(value);
        } else {
            showNotification(getMessage("logSelect"), NotificationType.HUMANIZED);
        }
        int scrollPos = panel.getScrollTop() + 30000;
        panel.setScrollTop(scrollPos);

    }

    private List<Level> getAllLevels() {
        List<Level> levelList = new ArrayList<Level>();
        levelList.add(Level.TRACE);
        levelList.add(Level.DEBUG);
        levelList.add(Level.INFO);
        levelList.add(Level.WARN);
        levelList.add(Level.ERROR);
        levelList.add(Level.FATAL);
        return levelList;
    }


}
