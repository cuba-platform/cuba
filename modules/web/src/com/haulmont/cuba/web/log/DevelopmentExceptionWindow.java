/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.log;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * @author hasanov
 * @version $Id$
 */
public class DevelopmentExceptionWindow extends Window {

    public DevelopmentExceptionWindow(String logMessage) {
        super("Exception");
        setHeight("80%");
        setWidth("80%");
        center();

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();
        setContent(layout);

        Panel scrollablePanel = new Panel();
        scrollablePanel.setSizeFull();
        VerticalLayout scrollContent = new VerticalLayout();
        scrollContent.setSizeUndefined();
        scrollablePanel.setContent(scrollContent);

        final Label label = new Label();
        label.setContentMode(ContentMode.HTML);
        label.setValue(logMessage);
        label.setSizeUndefined();
        label.setStyleName("cuba-log-content");

        ((Layout) scrollablePanel.getContent()).addComponent(label);
        layout.addComponent(scrollablePanel);
        layout.setExpandRatio(scrollablePanel, 1.0f);
    }
}
