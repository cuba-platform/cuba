/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.gui.export.ClosedDataProviderException;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.web.App;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Window for show/download resources
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ResourceWindow extends Window {

    private static final long serialVersionUID = -7090903621043753813L;

    private ExportDataProvider dataProvider;
    private String resourceName;
    private ExportFormat exportFormat;
    private boolean isAttachment;

    private static final Log log = LogFactory.getLog(ResourceWindow.class);

    /**
     * @param dataProvider ExportDataprovider
     * @param resourceName Resource name for client side
     * @param format       Resource type, can be null
     * @param attachment   If true then download file else view
     */
    public ResourceWindow(ExportDataProvider dataProvider, String resourceName,
                          ExportFormat format, boolean attachment) {
        this.resourceName = resourceName;
        this.dataProvider = dataProvider;
        this.exportFormat = format;
        this.isAttachment = attachment;

        setImmediate(false);
    }

    @Override
    public DownloadStream handleURI(URL context, String relativeUri) {

        // Firefox trick
        boolean isFirefox = false;
        boolean isChrome = false;
        ApplicationContext appContext = App.getInstance().getContext();
        if (appContext instanceof WebApplicationContext) {
            isFirefox = ((WebApplicationContext) appContext).getBrowser().isFirefox();
            isChrome = ((WebApplicationContext) appContext).getBrowser().isChrome();
        }

        String[] strings = resourceName.split("[/\\\\]");
        String fileName = strings[strings.length - 1];

        String contentType;
        if (exportFormat != null) {
            contentType = exportFormat.getContentType();
            if (StringUtils.isEmpty(FilenameUtils.getExtension(fileName)))
                fileName += "." + exportFormat.getFileExt();
        } else {
            contentType = "application/octet-stream";
        }

        contentType += ";charset=\"UTF-8\"";

        try {
            if (isChrome)
                fileName = MimeUtility.encodeWord(fileName, "UTF-8", "Q");
            else
                fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        DownloadStream downloadStream = null;
        try {
            downloadStream = new CloseableDownloadStream(dataProvider, contentType, fileName);
        } catch (ClosedDataProviderException e) {
            log.debug("Closed data provider, return null download stream");
            return null;
        }

        String fileNameParam;
        if (isFirefox)
            fileNameParam = "filename*=UTF-8''" + fileName + ";";
        else
            fileNameParam = "filename=\"" + fileName + "\";";

        String contentDisposition;
        if (isAttachment)
            contentDisposition = "attachment; " + fileNameParam;
        else
            contentDisposition = "inline; " + fileNameParam;

        downloadStream.setParameter("Content-Disposition", contentDisposition);

        return downloadStream;
    }
}