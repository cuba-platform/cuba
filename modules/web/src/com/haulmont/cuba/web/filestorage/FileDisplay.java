/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 05.11.2009 19:09:00
 *
 * $Id$
 */
package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.web.App;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.vaadin.ui.Window;
import com.vaadin.terminal.ExternalResource;

import java.net.URL;
import java.io.File;

public class FileDisplay {
    private boolean newWindow;

    public FileDisplay() {
        this(false);
    }

    public FileDisplay(boolean newWindow) {
        this.newWindow = newWindow;
    }

    public void show(String windowName, FileDescriptor fd, boolean attachment) {
        App app = App.getInstance();
        cleanOpenedWindows(app);
        FileWindow window = attachment ? createDownloadWindow(windowName, fd)
                : createDisplayWindow(windowName, fd);
        show(app, window);
    }

     public void show(String windowName, File f, boolean attachment) {
        App app = App.getInstance();
        cleanOpenedWindows(app);
        FileWindow window = attachment ? createDownloadWindow(windowName, f)
                : createDisplayWindow(windowName, f);
        show(app, window);
    }

    public void show(String windowName, URL url) {
        App app = App.getInstance();
        cleanOpenedWindows(app);
        FileWindow window = createDisplayWindow(windowName, url);
        show(app, window);
    }

    protected FileWindow createDisplayWindow(String windowName, FileDescriptor fd) {
        return new FileDisplayWindow(windowName, fd);
    }

    protected FileWindow createDisplayWindow(String windowName, URL url) {
        return new FileDisplayWindow(windowName, url);
    }

    protected FileWindow createDisplayWindow(String windowName, File f) {
        return new FileDisplayWindow(windowName, f);
    }

    protected FileWindow createDownloadWindow(String windowName, FileDescriptor fd) {
        return new FileDownloadWindow(windowName, fd);
    }

     protected FileWindow createDownloadWindow(String windowName, File f) {
        return new FileDownloadWindow(windowName, f);
    }

    private void show(final App application, final FileWindow window) {
        // this listener is useless, it doesn't work for closing of browser window
        window.addListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                window.dispose();
                application.removeWindow(window);
            }
        });

        application.addWindow(window);
        if (newWindow)
            application.getAppWindow().open(
                    new ExternalResource(window.getURL()),
                    "_blank",
                    800,
                    600,
                    Window.BORDER_DEFAULT
            );
        else
            application.getAppWindow().open(
                    new ExternalResource(window.getURL()),
                    "_top"
            );
    }

    private void cleanOpenedWindows(App application) {
        FileWindow window = null;
        for (Object obj : application.getWindows()) {
            if (obj instanceof FileWindow) {
                window = (FileWindow) obj;
            }
        }
        if (window != null) {
            application.removeWindow(window);
        }
    }
}
