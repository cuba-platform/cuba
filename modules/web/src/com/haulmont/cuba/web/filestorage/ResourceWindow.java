/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.app.FileDownloadHelper;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringUtils;

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

    /**
     * @param dataProvider ExportDataprovider
     * @param resourceName Resource name for client side
     * @param format       Resource type, can be null
     * @param attachment   If (true) then { download file } else { view }
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
        ApplicationContext appContext = App.getInstance().getContext();
        if (appContext instanceof WebApplicationContext) {
            isFirefox = ((WebApplicationContext) appContext).getBrowser().isFirefox();
        }

        String[] strings = resourceName.split("[/\\\\]");
        String fileName = strings[strings.length - 1];

        String contentType;
        if (exportFormat != null) {
            contentType = exportFormat.getContentType();
            if (StringUtils.isEmpty(FileDownloadHelper.getFileExt(fileName)))
                fileName += "." + exportFormat.getFileExt();
        } else {
            contentType = "application/octet-stream";
        }

        contentType += ";charset=\"UTF-8\"";

        try {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        DownloadStream downloadStream = new CloseableDownloadStream(dataProvider, contentType, fileName);

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

        downloadStream.setParameter("Cache-Control", "no-cache");
        downloadStream.setParameter("Pragma", "no-cache");
        downloadStream.setParameter("Expires", "-1");

        downloadStream.setParameter("Content-Disposition", contentDisposition);

        return downloadStream;
    }
}