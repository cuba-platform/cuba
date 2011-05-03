/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.gui.export.*;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.FileDownloadHelper;
import com.haulmont.cuba.web.rpt.ReportHtmlWindow;
import com.haulmont.cuba.web.rpt.ReportOutputWindow;
import com.haulmont.cuba.web.toolkit.ui.JavaScriptHost;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Allows to show exported data in web browser or download it
 */
public class WebExportDisplay implements ExportDisplay, Serializable {
    private static final long serialVersionUID = -5284064787054836702L;

    private boolean newWindow;
    private boolean attachment;

    // Use flags from app.properties for show/download files
    private boolean useViewList = false;

    /**
     * Constructor with attachment=true, newWindow=false
     * (see {@link WebExportDisplay#WebExportDisplay(boolean, boolean)})
     */
    public WebExportDisplay() {
        this(true, false);
        useViewList = true;
    }

    /**
     * @param attachment if true, force download data instead of show in browser
     * @param newWindow  if true, show data in the same browser window;
     *                   if false, open new browser window
     */

    public WebExportDisplay(boolean attachment, boolean newWindow) {
        this.attachment = attachment;
        this.newWindow = newWindow;
    }

    /**
     * Show/Download resource at client side
     *
     * @param dataProvider ExportDataProvider
     * @param resourceName ResourceName for client side
     * @param format       ExportFormat
     * @see com.haulmont.cuba.gui.export.FileDataProvider
     * @see com.haulmont.cuba.gui.export.ByteArrayDataProvider
     */
    public void show(ExportDataProvider dataProvider, String resourceName, final ExportFormat format) {

        if (useViewList) {
            String fileExt;

            if (format != null)
                fileExt = format.getFileExt();
            else
                fileExt = FileDownloadHelper.getFileExt(resourceName);

            WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
            boolean viewFlag = webConfig.getViewFileExtensions().contains(fileExt);
            attachment = !viewFlag;
            newWindow = viewFlag;
        }

        final ResourceWindow window = new ResourceWindow(dataProvider, resourceName, format, attachment);
        final App app = App.getInstance();

        JavaScriptHost webScriptHost = null;
        Iterator<Component> componentIterator = app.getAppWindow().getComponentIterator();
        while (componentIterator.hasNext() && (webScriptHost == null)) {
            Component component = componentIterator.next();
            if (component instanceof JavaScriptHost) {
                webScriptHost = (JavaScriptHost) component;
            }
        }
        if (webScriptHost == null) {
            webScriptHost = new JavaScriptHost();
            app.getAppWindow().addComponent(webScriptHost);
        }

        cleanOpenedWindows(app);
        app.addWindow(window);

        if (newWindow) {
            webScriptHost.viewDocument(window.getURL().toString());
        } else {
            webScriptHost.getResource(window.getURL().toString());
        }
    }

    /**
     * Show/Download resource at client side
     *
     * @param dataProvider ExportDataProvider
     * @param resourceName ResourceName for client side
     * @see com.haulmont.cuba.gui.export.FileDataProvider
     * @see com.haulmont.cuba.gui.export.ByteArrayDataProvider
     */
    public void show(ExportDataProvider dataProvider, String resourceName) {
        String extension = FileDownloadHelper.getFileExt(resourceName);
        ExportFormat format = ExportFormat.getByExtension(extension);
        show(dataProvider, resourceName, format);
    }

    public void show(FileDescriptor fileDescriptor, ExportFormat format) {
        show(new FileDataProvider(fileDescriptor), fileDescriptor.getName(), format);
    }

    public void show(FileDescriptor fileDescriptor) {
        ExportFormat format = ExportFormat.getByExtension(fileDescriptor.getExtension());
        show(fileDescriptor, format);
    }

    public void show(byte[] content, String resourceName, ExportFormat format) {
        show(new ByteArrayDataProvider(content), resourceName, format);
    }

    private void cleanOpenedWindows(App application) {
        ResourceWindow window = null;
        for (Object obj : application.getWindows()) {
            if (obj instanceof ResourceWindow) {
                window = (ResourceWindow) obj;
                break;
            }
        }
        if (window != null) {
            application.removeWindow(window);
        }
    }

    @Deprecated
    public boolean isAttachment() {
        return attachment;
    }

    @Deprecated
    public boolean isNewWindow() {
        return newWindow;
    }

    @Deprecated
    public void showHtml(String html, String name) {
        App app = App.getInstance();
        cleanupWindows(app);
        ReportOutputWindow window = new ReportHtmlWindow(name, html);
        showWindow(app, window);
    }

    @Deprecated
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

    @Deprecated
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
