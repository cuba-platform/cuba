/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.log;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author hasanov
 * @version $Id$
 */
public class DevelopmentExceptionWindow extends Window {

    private VerticalLayout mainLayout;
    private Panel stackTraceScrollablePanel;
    private Button showStackTraceButton;
    private float minWindowHeight = 350;
    private boolean isVisibleStackTrace = false;

    public DevelopmentExceptionWindow(String rootCauseMessage, String stackTrace) {
        this(rootCauseMessage, stackTrace, null);
    }

    public DevelopmentExceptionWindow(String rootCauseMessage, String stackTrace, Map<String, Object> params) {
        super("Exception");
        final Messages messages = AppBeans.get(Messages.class);
        setWidth(750, Unit.PIXELS);
        center();
        mainLayout = new VerticalLayout();
        Label rootCauseMessageLabel = new Label(rootCauseMessage);
        Label infoLabel = new Label(messages.getMessage(getClass(),"exceptionDialog.addInfo"));
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);

        Button closeButton = new Button(messages.getMessage(getClass(), "exceptionDialog.closeBtn"));
        closeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                DevelopmentExceptionWindow.this.close();
            }
        });
        buttonsLayout.addComponent(closeButton);
        final String showStackTraceMessage = messages.getMessage(getClass(), "exceptionDialog.showStackTrace") + " >>";
        showStackTraceButton = new Button(showStackTraceMessage);
        showStackTraceButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!DevelopmentExceptionWindow.this.isVisibleStackTrace) {
                    event.getButton().setCaption(messages.getMessage(getClass(), "exceptionDialog.showStackTrace") + " <<");

                    showStackTrace(true);
                    DevelopmentExceptionWindow.this.isVisibleStackTrace = true;
                } else {
                    event.getButton().setCaption(showStackTraceMessage);
                    showStackTrace(false);
                    DevelopmentExceptionWindow.this.isVisibleStackTrace = false;
                }
            }
        });

        Table infoTable = new Table();
        infoTable.addContainerProperty(messages.getMessage(getClass(),"key"), String.class, null);
        infoTable.addContainerProperty(messages.getMessage(getClass(),"value"), Object.class, null);
        infoTable.setHeight(150, Unit.PIXELS);
        infoTable.setWidth(100, Unit.PERCENTAGE);
        int tableId = 0;
        for (Map.Entry<String, Object> entry : params.entrySet())
            infoTable.addItem(new Object[]{entry.getKey(), entry.getValue()}, tableId++);

        stackTraceScrollablePanel = new Panel();
        stackTraceScrollablePanel.setSizeFull();
        VerticalLayout scrollContent = new VerticalLayout();
        scrollContent.setSizeUndefined();
        stackTraceScrollablePanel.setContent(scrollContent);

        final Label stackTraceLabel = new Label();
        stackTraceLabel.setContentMode(ContentMode.HTML);
        stackTraceLabel.setValue(stackTrace);
        stackTraceLabel.setSizeUndefined();
        stackTraceLabel.setStyleName("cuba-log-content");
        ((Layout) stackTraceScrollablePanel.getContent()).addComponent(stackTraceLabel);

        mainLayout.addComponent(rootCauseMessageLabel);
        mainLayout.addComponent(infoLabel);
        mainLayout.addComponent(infoTable);
        mainLayout.addComponent(showStackTraceButton);
        mainLayout.addComponent(stackTraceScrollablePanel);
        mainLayout.addComponent(buttonsLayout);

        mainLayout.setSpacing(true);
        mainLayout.setHeight(100, Unit.PERCENTAGE);
        mainLayout.setExpandRatio(stackTraceScrollablePanel, 1.0f);
        setContent(mainLayout);
        showStackTrace(isVisibleStackTrace);
    }

    public void showStackTrace(boolean value) {
        if (value) {
            minWindowHeight = getHeight();
            mainLayout.setExpandRatio(showStackTraceButton, 0.0f);
            this.setHeight(650, Unit.PIXELS);
        } else {
            mainLayout.setExpandRatio(showStackTraceButton, 1.0f);
            this.setHeight(minWindowHeight, Unit.PIXELS);
        }
        stackTraceScrollablePanel.setVisible(value);
    }
}
