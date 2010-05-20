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

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.app.FileDownloadHelper;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;

public class FileDisplayWindow extends FileWindow {

    protected URL sourceURL;

    public FileDisplayWindow(String windowName, FileDescriptor fd) {
        super(windowName, fd);
        initUI();
    }

    public FileDisplayWindow(String windowName, URL url) {
        super(windowName, (FileDescriptor) null);
        sourceURL = url;
        initUI();
    }

    public FileDisplayWindow(String windowName, File f) {
        super(windowName, f);
        initUI();
    }

    @Override
    protected void close() {
        //Hack the situation when a user are refreshing the window
        fireClose();
    }

    private void initUI() {
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);

        final Embedded embedded = new Embedded();
        embedded.setSizeFull();
        if (fd != null) {
            embedded.setSource(new ExternalResource(createURL()));
        } else if (sourceURL != null) {
            embedded.setSource(new ExternalResource(sourceURL));
        } else if (f != null) {
            embedded.setSource(new FileResource(f, App.getInstance()));
        } else {
            throw new RuntimeException("there is no resourse for display");
        }
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
