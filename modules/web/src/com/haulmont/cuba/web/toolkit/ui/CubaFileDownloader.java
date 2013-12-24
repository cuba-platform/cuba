/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.downloader.CubaFileDownloaderClientRPC;
import com.vaadin.server.*;
import org.apache.commons.lang.StringUtils;

import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFileDownloader extends AbstractExtension {

    public static final String DOWNLOAD_RESOURCE_PREFIX = "download-";
    public static final String VIEW_RESOURCE_PREFIX = "view-";
    private boolean overrideContentType = true;

    public void downloadFile(Resource resource) {
        String resourceId = DOWNLOAD_RESOURCE_PREFIX + UUID.randomUUID().toString();
        setResource(resourceId, resource);

        getRpcProxy(CubaFileDownloaderClientRPC.class).downloadFile(resourceId);
    }

    public void viewDocument(Resource resource) {
        String resourceId = VIEW_RESOURCE_PREFIX + UUID.randomUUID().toString();
        setResource(resourceId, resource);

        getRpcProxy(CubaFileDownloaderClientRPC.class).viewDocument(resourceId);
    }

    /**
     * Sets whether the content type of served resources should be overriden to
     * <code>application/octet-stream</code> to reduce the risk of a browser
     * plugin choosing to display the resource instead of downloading it. This
     * is by default set to <code>true</code>.
     * <p>
     * Please note that this only affects Connector resources (e.g.
     * {@link com.vaadin.server.FileResource} and {@link com.vaadin.server.ClassResource}) but not other resource
     * types (e.g. {@link com.vaadin.server.ExternalResource} or {@link com.vaadin.server.ThemeResource}).
     * </p>
     *
     * @param overrideContentType <code>true</code> to override the content type if possible;
     *                            <code>false</code> to use the original content type.
     */
    public void setOverrideContentType(boolean overrideContentType) {
        this.overrideContentType = overrideContentType;
    }

    /**
     * Checks whether the content type should be overridden.
     *
     * @return <code>true</code> if the content type will be overridden when
     *         possible; <code>false</code> if the original content type will be
     *         used.
     * @see #setOverrideContentType(boolean)
     */
    public boolean isOverrideContentType() {
        return overrideContentType;
    }

    @Override
    public void extend(AbstractClientConnector target) {
        super.extend(target);
    }

    @Override
    public boolean handleConnectorRequest(VaadinRequest request,
                                          VaadinResponse response, String path) throws IOException {
        String targetResourceKey = null;

        synchronized (getState()) {
            Iterator<String> resourceIterator = getState().resources.keySet().iterator();
            while (resourceIterator.hasNext() && targetResourceKey == null) {
                String resourceKey = resourceIterator.next();
                if (path.matches(resourceKey + "(/.*)?"))
                    targetResourceKey = resourceKey;
            }
        }

        if (targetResourceKey == null)
            return false;

        Resource resource = getResource(targetResourceKey);
        boolean isViewDocumentRequest = targetResourceKey.startsWith(VIEW_RESOURCE_PREFIX);

        WebBrowser browser = Page.getCurrent().getWebBrowser();
        boolean isFirefox = browser.isFirefox();
        boolean isChrome = browser.isChrome();

        try {
            if (resource instanceof ConnectorResource) {
                DownloadStream stream = ((ConnectorResource) resource).getStream();

                String fileName;

                if (isChrome)
                    fileName = MimeUtility.encodeWord(stream.getFileName(), "UTF-8", "Q");
                else
                    fileName = URLEncoder.encode(stream.getFileName(), "UTF-8").replaceAll("\\+", "%20");

                if (stream.getParameter("Content-Disposition") == null) {
                    // Content-Disposition: attachment generally forces download
                    stream.setParameter("Content-Disposition",
                            (isViewDocumentRequest ? "inline" : "attachment") + "; " +
                            (isFirefox ? "filename*=UTF-8''" + fileName : "filename=\"" + fileName + "\""));
                }

                // Content-Type to block eager browser plug-ins from hijacking the
                // file
                if (isOverrideContentType() && !isViewDocumentRequest) {
                    stream.setContentType("application/octet-stream;charset=UTF-8");
                } else {
                    if (StringUtils.isNotEmpty(stream.getContentType())) {
                        stream.setContentType(stream.getContentType() + ";charset=UTF-8\"");
                    } else {
                        stream.setContentType(";charset=UTF-8\"");
                    }
                }
                stream.writeResponse(request, response);
                return true;
            } else {
                return false;
            }
        } finally {
            synchronized (getState()) {
                getState().resources.remove(targetResourceKey);
            }
        }
    }
}