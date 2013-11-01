/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.export.*;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.toolkit.ui.JavaScriptHost;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.ManagedBean;
import java.util.UUID;

/**
 * Allows to show exported data in web browser or download it
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(ExportDisplay.NAME)
@Scope("prototype")
public class WebExportDisplay implements ExportDisplay {

    private static final Log log = LogFactory.getLog(WebExportDisplay.class);

    protected boolean newWindow;

    // Use flags from app.properties for show/download files
    protected boolean useViewList = false;

    /**
     * Constructor with attachment=true, newWindow=false
     * (see {@link WebExportDisplay#WebExportDisplay(boolean)})
     */
    public WebExportDisplay() {
        this(false);
        useViewList = true;
    }

    /**
     * @param newWindow if true, show data in the same browser window;
     *                  if false, open new browser window
     */
    public WebExportDisplay(boolean newWindow) {
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
    @Override
    public void show(ExportDataProvider dataProvider, String resourceName, final ExportFormat format) {

        if (useViewList) {
            String fileExt;

            if (format != null) {
                fileExt = format.getFileExt();
            } else {
                fileExt = FilenameUtils.getExtension(resourceName);
            }

            Configuration configuration = AppBeans.get(Configuration.NAME);
            WebConfig webConfig = configuration.getConfig(WebConfig.class);
            newWindow = webConfig.getViewFileExtensions().contains(fileExt);
        }

        // Try to get stream
        ProxyDataProvider proxyDataProvider;
        try {
            proxyDataProvider = new ProxyDataProvider(dataProvider);
        } catch (ClosedDataProviderException e) {
            log.error("Unable to open data provider for resource " + resourceName, e);
            return;
        }

        final App app = App.getInstance();
        final ResourceWindow window = new ResourceWindow(proxyDataProvider, resourceName, format, !newWindow);

        cleanOpenedWindows(app);
        app.addWindow(window);

        JavaScriptHost webScriptHost = app.getAppWindow().getScriptHost();
        UUID cacheKey = UUID.randomUUID();

        if (newWindow) {
            webScriptHost.viewDocument(window.getURL().toString() + "?" + cacheKey);
        } else {
            webScriptHost.getResource(window.getURL().toString() + "?" + cacheKey);
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
    @Override
    public void show(ExportDataProvider dataProvider, String resourceName) {
        String extension = FilenameUtils.getExtension(resourceName);
        ExportFormat format = ExportFormat.getByExtension(extension);
        show(dataProvider, resourceName, format);
    }

    @Override
    public void show(FileDescriptor fileDescriptor, ExportFormat format) {
        show(new FileDataProvider(fileDescriptor), fileDescriptor.getName(), format);
    }

    @Override
    public void setFrame(IFrame frame) {
    }

    @Override
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
}