/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.export.*;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.toolkit.ui.CubaFileDownloader;
import com.vaadin.server.StreamResource;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import javax.annotation.ManagedBean;
import java.io.InputStream;

/**
 * Allows to show exported data in web browser or download it
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(ExportDisplay.NAME)
@Scope("prototype")
public class WebExportDisplay implements ExportDisplay {

    private boolean newWindow;

    // Use flags from app.properties for show/download files
    private boolean useViewList = false;

    /**
     * Constructor with newWindow=false
     */
    public WebExportDisplay() {
        this(false);
        useViewList = true;
    }

    /**
     * @param newWindow  if true, show data in the same browser window;
     *                   if false, open new browser window
     */
    public WebExportDisplay(boolean newWindow) {
        this.newWindow = newWindow;
    }

    /**
     * Show/Download resource at client side
     *
     * @param dataProvider ExportDataProvider
     * @param resourceName ResourceName for client side
     * @param exportFormat ExportFormat
     * @see com.haulmont.cuba.gui.export.FileDataProvider
     * @see com.haulmont.cuba.gui.export.ByteArrayDataProvider
     */
    @Override
    public void show(ExportDataProvider dataProvider, String resourceName, final ExportFormat exportFormat) {
        if (useViewList) {
            String fileExt;

            if (exportFormat != null)
                fileExt = exportFormat.getFileExt();
            else
                fileExt = FilenameUtils.getExtension(resourceName);

            Configuration configuration = AppBeans.get(Configuration.NAME);
            WebConfig webConfig = configuration.getConfig(WebConfig.class);
            boolean viewFlag = webConfig.getViewFileExtensions().contains(fileExt);
            newWindow = viewFlag;
        }

        // Try to get stream
        final ProxyDataProvider proxyDataProvider = new ProxyDataProvider(dataProvider);

        if (exportFormat != null) {
            if (StringUtils.isEmpty(FilenameUtils.getExtension(resourceName)))
                resourceName += "." + exportFormat.getFileExt();
        }

        CubaFileDownloader fileDownloader = App.getInstance().getAppWindow().getFileDownloader();

        StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                return proxyDataProvider.provide();
            }
        };
        StreamResource resource = new StreamResource(streamSource, resourceName);

        if (newWindow)
            fileDownloader.viewDocument(resource);
        else
            fileDownloader.downloadFile(resource);
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
}