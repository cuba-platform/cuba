/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 05.11.2009 18:48:33
 *
 * $Id: FileDisplayWindow.java 1057 2009-11-20 13:54:45Z gorodnov $
 */
package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.app.FileDownloadHelper;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;

import java.net.MalformedURLException;
import java.net.URL;

public class FileDisplayWindow extends FileWindow {

    protected URL sourceURL;

    public FileDisplayWindow(String windowName, FileDescriptor fd) {
        super(windowName, fd);
        initUI();
    }

    public FileDisplayWindow(String windowName, URL url) {
        super(windowName, null);
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
        embedded.setSource(new ExternalResource(fd != null ? createURL() : sourceURL));
        embedded.setType(Embedded.TYPE_BROWSER);
        mainLayout.addComponent(embedded);

        setLayout(mainLayout);
    }

    protected URL createURL() {
        try {
            App app = App.getInstance();
            return new URL(app.getURL() + FileDownloadHelper.makeUrl(fd, false));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
