/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.report.formatters.oo;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.report.exception.FailedToConnectToOpenOfficeException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.io.IOException;
import com.sun.star.io.XInputStream;
import com.sun.star.lang.XComponent;

import java.io.OutputStream;

import static com.haulmont.cuba.report.formatters.oo.ODTHelper.*;

public class XlsToPdfConverter {
    private static final String XLS_TO_PDF_OUTPUT_FILE = "calc_pdf_Export";
    private OOOConnection connection;
    private XComponent xComponent;

    public XlsToPdfConverter() {
        connectToOffice();
    }

    private void connectToOffice() {
        String openOfficePath = ConfigProvider.getConfig(ServerConfig.class).getOpenOfficePath();
        try {
            connection = ((OOOConnector) Locator.lookup(OOOConnector.NAME)).createConnection(openOfficePath);
        } catch (Exception ex) {
            throw new FailedToConnectToOpenOfficeException("Please check OpenOffice path: " + openOfficePath);
        }
    }

    public void convertXlsToPdf(final byte[] documentBytes, final OutputStream outputStream) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    XInputStream xis = new OOInputStream(documentBytes);
                    XComponentLoader xComponentLoader = connection.createXComponentLoader();
                    xComponent = loadXComponent(xComponentLoader, xis);
                    saveAndClose(xComponent, outputStream, XLS_TO_PDF_OUTPUT_FILE);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        runWithTimeoutAndCloseConnection(connection, runnable);
    }

    private void saveAndClose(XComponent xComponent, OutputStream outputStream, String filterName) throws IOException {
        OOOutputStream ooos = new OOOutputStream(outputStream);
        saveXComponent(xComponent, ooos, filterName);
        closeXComponent(xComponent);
    }
}