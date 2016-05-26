/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.export.*;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.toolkit.ui.CubaFileDownloader;
import com.vaadin.server.StreamResource;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.InputStream;

/**
 * Allows to show exported data in web browser or download it.
 */
@Component(ExportDisplay.NAME)
@Scope("prototype")
public class WebExportDisplay implements ExportDisplay {

    @Inject
    protected BackgroundWorker backgroundWorker;

    @Inject
    protected Configuration configuration;

    protected boolean newWindow;

    // Use flags from app.properties for show/download files
    protected boolean useViewList = false;

    /**
     * Constructor with newWindow=false
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
     * @param exportFormat ExportFormat
     * @see com.haulmont.cuba.gui.export.FileDataProvider
     * @see com.haulmont.cuba.gui.export.ByteArrayDataProvider
     */
    @Override
    public void show(ExportDataProvider dataProvider, String resourceName, final ExportFormat exportFormat) {
        backgroundWorker.checkUIAccess();

        if (useViewList) {
            String fileExt;

            if (exportFormat != null) {
                fileExt = exportFormat.getFileExt();
            } else {
                fileExt = FilenameUtils.getExtension(resourceName);
            }

            WebConfig webConfig = configuration.getConfig(WebConfig.class);
            newWindow = webConfig.getViewFileExtensions().contains(StringUtils.lowerCase(fileExt));
        }

        if (exportFormat != null) {
            if (StringUtils.isEmpty(FilenameUtils.getExtension(resourceName))) {
                resourceName += "." + exportFormat.getFileExt();
            }
        }

        CubaFileDownloader fileDownloader = App.getInstance().getAppWindow().getFileDownloader();

        StreamResource resource = new StreamResource(new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                return dataProvider.provide();
            }
        }, resourceName);

        if (exportFormat != null && StringUtils.isNotEmpty(exportFormat.getContentType())) {
            resource.setMIMEType(exportFormat.getContentType());
        } else {
            resource.setMIMEType(FileTypesHelper.getMIMEType(resourceName));
        }

        if (newWindow) {
            fileDownloader.viewDocument(resource);
        } else {
            fileDownloader.downloadFile(resource);
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
    public void setFrame(Frame frame) {
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