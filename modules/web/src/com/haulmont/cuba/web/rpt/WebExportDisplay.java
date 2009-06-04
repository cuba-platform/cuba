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
import com.haulmont.cuba.web.App;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.ui.Window;

public class WebExportDisplay implements ExportDisplay
{
    private boolean newWindow;
    private boolean attachment;

    public WebExportDisplay() {
        this(true, false);
    }

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

    public void show(byte[] data, String name, ExportFormat format) {
        if (format != ExportFormat.XLS && format != ExportFormat.PDF)
            throw new UnsupportedOperationException("Method WebExportDisplay.show doesn't support this format: " + format);

        ReportOutput reportOutput = new ReportOutput(format)
                .setAttachment(attachment).setNewWindow(newWindow);

        App app = App.getInstance();
        cleanupWindows(app);
        ReportOutputWindow window = new ReportDownloadWindow(data, name, reportOutput);
        showWindow(app, window);
    }

    public void showHtml(String html, String name) {
        App app = App.getInstance();
        cleanupWindows(app);
        ReportOutputWindow window = new ReportHtmlWindow(name, html);
        showWindow(app, window);
    }

    private void showWindow(App app, ReportOutputWindow window) {
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
