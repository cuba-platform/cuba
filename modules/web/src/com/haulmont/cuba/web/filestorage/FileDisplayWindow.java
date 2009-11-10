/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 05.11.2009 18:48:33
 *
 * $Id$
 */
package com.haulmont.cuba.web.filestorage;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;
import com.vaadin.terminal.ExternalResource;

import java.net.URL;

public class FileDisplayWindow extends Window {

    private URL sourceURL;

    public FileDisplayWindow(String windowName, URL url) {
        super(windowName);
        sourceURL = url;
        initUI();
    }

    private void initUI() {
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);

        final Embedded embedded = new Embedded();
        embedded.setSizeFull();
        embedded.setSource(new ExternalResource(sourceURL));
        embedded.setType(Embedded.TYPE_BROWSER);
        mainLayout.addComponent(embedded);

        setContent(mainLayout);
    }

    public void dispose() {
    }
}
