/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.05.2009 11:15:24
 *
 * $Id$
 */
package com.haulmont.cuba.web.rpt;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.core.app.ReportService;
import com.haulmont.cuba.web.App;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.terminal.gwt.server.WebApplicationContext;
import com.itmill.toolkit.ui.Window;

import javax.servlet.http.HttpSession;

public class ReportHelper
{
    public static JasperPrint executeJasperReport(String name, Map<String, Object> params) {
        ReportService rs = ServiceLocator.lookup(ReportService.JNDI_NAME);
        return rs.executeJasperReport(name, params);
    }

    public static JasperPrint executeJasperReport(String name, Map<String, Object> params, JRDataSource dataSource) {
        ReportService rs = ServiceLocator.lookup(ReportService.JNDI_NAME);
        return rs.executeJasperReport(name, params, dataSource);
    }

    public static void printJasperReport(String name, ReportOutput output) {
        printJasperReport(name, new HashMap(), output);
    }

    public static void printJasperReport(String name, Map<String, Object> params, ReportOutput output) {
        JasperPrint print = executeJasperReport(name, params);
        printJasperReport(name, print, output);
    }

    public static void printJasperReport(List<String> names, Map<String, Object> params, ReportOutput output, JRDataSource dataSource) {
        List<JasperPrint> prints = new ArrayList<JasperPrint>();
        for (String str : names) {
            prints.add(executeJasperReport(str, params, dataSource));
        }
        if (!names.isEmpty()) {
            printJasperReport(names.iterator().next(), prints, output);
        }
    }

    public static void printJasperReport(String name, JasperPrint jasperPrint, ReportOutput output) {
        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        jasperPrints.add(jasperPrint);
        printJasperReport(name, jasperPrints, output);
    }

    public static void printJasperReport(String name, List<JasperPrint> jasperPrint, ReportOutput output) {
        App app = App.getInstance();

        ReportOutputWindow window = null;
        for (Object obj : app.getWindows()) {
            if (obj instanceof ReportOutputWindow) {
                window = (ReportOutputWindow) obj;
            }
        }
        if (window != null) {
            app.removeWindow(window);
        }

        if (output.getFormat() == ReportOutput.Format.PDF || output.getFormat() == ReportOutput.Format.XLS) {
            JRAbstractExporter exporter;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (output.getFormat() == ReportOutput.Format.PDF) {
                exporter = new JRPdfExporter();
            } else {
                exporter = new JRXlsExporter();
                exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            }
            if (jasperPrint.size() == 1) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint.iterator().next());
            } else {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrint);
            }
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
            try {
                exporter.exportReport();
            } catch (JRException e) {
                throw new RuntimeException("Error printing report " + name, e);
            }
            byte[] bytes = outputStream.toByteArray();

            window = new ReportDownloadWindow(bytes, name, output);
        } else if (output.getFormat() == ReportOutput.Format.HTML) {
            HttpSession httpSession = ((WebApplicationContext) App.getInstance().getContext()).getHttpSession();
            httpSession.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);

            StringWriter writer = new StringWriter();
            JRHtmlExporter exporter = new JRHtmlExporter();
            if (jasperPrint.size() == 1) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint.iterator().next());
            } else {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrint);
            }
            exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, writer);
            exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
            try {
                exporter.exportReport();
            } catch (JRException e) {
                throw new RuntimeException("Error printing report " + name, e);
            }
            window = new ReportHtmlWindow(name, writer.toString());
        }

        app.addWindow(window);
        if (output.isNewWindow())
            App.getInstance().getAppWindow().open(
                    new ExternalResource(window.getURL()),
                    "_blank",
                    800,
                    600,
                    Window.BORDER_DEFAULT
            );
        else
            App.getInstance().getAppWindow().open(
                    new ExternalResource(window.getURL()),
                    "_top"
            );
    }
}
