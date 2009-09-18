/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.06.2009 12:41:29
 *
 * $Id$
 */
package com.haulmont.cuba.web.rpt;

import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.web.App;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Window;

/**
 * Allows to show exported data in web browser or download it
 */
public class WebExportDisplay implements ExportDisplay
{
    private boolean newWindow;
    private boolean attachment;

    /**
     * Constructor with attachment=true, newWindow=false 
     * (see {@link com.haulmont.cuba.web.rpt.WebExportDisplay#WebExportDisplay(boolean, boolean)})
     */
    public WebExportDisplay() {
        this(true, false);
    }

    /**
     * Constructor
     * @param attachment if true, force download data instead of show in browser
     * @param newWindow if true, show data in the same browser window;
     * if false, open new browser window
     */
    public WebExportDisplay(boolean attachment, boolean newWindow) {
        this.attachment = attachment;
        this.newWindow = newWindow;
    }

    public boolean isAttachment() {
        return attachment;
    }

    public boolean isNewWindow() {
        return newWindow;
    }

    public void show(ExportDataProvider dataProvider, String name, ExportFormat format) {
        ReportOutput reportOutput = new ReportOutput(format)
                .setAttachment(attachment).setNewWindow(newWindow);

        App app = App.getInstance();
        cleanupWindows(app);
        ReportOutputWindow window = new ReportDownloadWindow(dataProvider, name, reportOutput);
        showWindow(app, window);
    }

    public void showHtml(String html, String name) {
        App app = App.getInstance();
        cleanupWindows(app);
        ReportOutputWindow window = new ReportHtmlWindow(name, html);
        showWindow(app, window);
    }

    private void showWindow(final App app, final ReportOutputWindow window) {

        // this listener is useless, it doesn't work for closing of browser window
        window.addListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                window.dispose();
                app.removeWindow(window);
            }
        });

        app.addWindow(window);
        if (newWindow)
            app.getAppWindow().open(
                    new ExternalResource(window.getURL()),
                    "_blank",
                    800,
                    600,
                    Window.BORDER_DEFAULT
            );
        else
            app.getAppWindow().open(
                    new ExternalResource(window.getURL()),
                    "_top"
            );
    }

    private void cleanupWindows(App app) {
        ReportOutputWindow window = null;
        for (Object obj : app.getWindows()) {
            if (obj instanceof ReportOutputWindow) {
                window = (ReportOutputWindow) obj;
            }
        }
        if (window != null) {
            app.removeWindow(window);
        }
    }
}
