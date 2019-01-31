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
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.export.*;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.widgets.CubaFileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.io.PrintWriter;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * Allows to show exported data in web browser or download it.
 */
@Component(ExportDisplay.NAME)
@Scope("prototype")
public class WebExportDisplay implements ExportDisplay {

    private static final Logger log = LoggerFactory.getLogger(WebExportDisplay.class);

    @Inject
    protected BackgroundWorker backgroundWorker;

    @Inject
    protected Configuration configuration;

    protected Messages messages;

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

    @Inject
    public void setMessages(Messages messages) {
        this.messages = messages;
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

        boolean showNewWindow = this.newWindow;

        if (useViewList) {
            String fileExt;

            if (exportFormat != null) {
                fileExt = exportFormat.getFileExt();
            } else {
                fileExt = FilenameUtils.getExtension(resourceName);
            }

            WebConfig webConfig = configuration.getConfig(WebConfig.class);
            showNewWindow = webConfig.getViewFileExtensions().contains(StringUtils.lowerCase(fileExt));
        }

        if (exportFormat != null) {
            if (StringUtils.isEmpty(FilenameUtils.getExtension(resourceName))) {
                resourceName += "." + exportFormat.getFileExt();
            }
        }

        CubaFileDownloader fileDownloader = AppUI.getCurrent().getFileDownloader();
        fileDownloader.setFileNotFoundExceptionListener(this::handleFileNotFoundException);

        StreamResource resource = new StreamResource(dataProvider::provide, resourceName);

        if (exportFormat != null && StringUtils.isNotEmpty(exportFormat.getContentType())) {
            resource.setMIMEType(exportFormat.getContentType());
        } else {
            resource.setMIMEType(FileTypesHelper.getMIMEType(resourceName));
        }

        if ((showNewWindow && isBrowserSupportsPopups()) || isIOS()) {
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
    public boolean isShowNewWindow() {
        return newWindow;
    }

    @Override
    public void setShowNewWindow(boolean showNewWindow) {
        this.newWindow = showNewWindow;

        // newWindow is set explicitly
        this.useViewList = false;
    }

    @Override
    public void show(FileDescriptor fileDescriptor) {
        ExportFormat format = ExportFormat.getByExtension(fileDescriptor.getExtension());
        show(fileDescriptor, format);
    }

    public void show(byte[] content, String resourceName, ExportFormat format) {
        show(new ByteArrayDataProvider(content), resourceName, format);
    }

    /**
     * @deprecated Use {@link #isShowNewWindow()}
     */
    @Deprecated
    public boolean isNewWindow() {
        return isShowNewWindow();
    }

    /**
     * @deprecated Use {@link #setShowNewWindow(boolean)}
     */
    @Deprecated
    public void setNewWindow(boolean newWindow) {
        this.setShowNewWindow(newWindow);
    }

    public boolean isBrowserSupportsPopups() {
        return !Page.getCurrent().getWebBrowser().isSafari();
    }

    protected boolean isIOS() {
        return Page.getCurrent().getWebBrowser().isIOS();
    }

    protected boolean handleFileNotFoundException(Exception exception, VaadinResponse response) {
        if (!(exception instanceof RuntimeFileStorageException)) {
            return false;
        }

        FileStorageException storageException = ((RuntimeFileStorageException) exception).getCause();
        if (storageException.getType() == FileStorageException.Type.FILE_NOT_FOUND) {
            try {
                writeFileNotFoundException(response, messages.formatMessage(
                        getClass(), "fileNotFound.message", storageException.getFileName()));
                return true;
            } catch (IOException e) {
                log.debug("Can't write file not found exception to the response body for: {}",
                        storageException.getFileName(), e);
                return false;
            }
        } else {
            return false;
        }
    }

    protected void writeFileNotFoundException(VaadinResponse response, String message) throws IOException {
        response.setStatus(SC_NOT_FOUND);
        response.setHeader("Content-Type", "text/html; charset=utf-8");

        PrintWriter writer = response.getWriter();
        writer.write("<h1 style=\"font-size:40px;\">404</h1><p style=\"font-size: 25px\">" + message + "</p>");
        writer.flush();
    }
}