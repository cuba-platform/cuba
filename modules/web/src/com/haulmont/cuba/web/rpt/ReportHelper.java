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

import com.haulmont.cuba.core.app.ReportService;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.web.App;
import com.itmill.toolkit.terminal.gwt.server.WebApplicationContext;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        WebExportDisplay display = new WebExportDisplay(output.isAttachment(), output.isNewWindow());

        if (output.getFormat() == ExportFormat.PDF || output.getFormat() == ExportFormat.XLS) {
            JRAbstractExporter exporter;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (output.getFormat() == ExportFormat.PDF) {
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
            display.show(outputStream.toByteArray(), name, output.getFormat());
        } else if (output.getFormat() == ExportFormat.HTML) {
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
            display.showHtml(writer.toString(), name);
        }
    }


    public static byte[] JasperReportToByte(String name, Map<String, Object> params, ReportOutput output) {
        JasperPrint print = executeJasperReport(name, params);
        return JasperReportToByte(name, print, output);
    }

    public static byte[] JasperReportToByte(String name, JasperPrint jasperPrint, ReportOutput output) {
        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        jasperPrints.add(jasperPrint);
        return JasperReportToByte(name, jasperPrints, output);
    }
    
    public static byte[] JasperReportToByte(String name, List<JasperPrint> jasperPrint, ReportOutput output) {
        WebExportDisplay display = new WebExportDisplay(output.isAttachment(), output.isNewWindow());

        byte[] reportBytes = new byte[]{};

        if (output.getFormat() == ExportFormat.PDF || output.getFormat() == ExportFormat.XLS) {
            JRAbstractExporter exporter;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (output.getFormat() == ExportFormat.PDF) {
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
            reportBytes = outputStream.toByteArray();
        } else if (output.getFormat() == ExportFormat.HTML) {
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
            reportBytes = writer.toString().getBytes();
        }
        return reportBytes;
    }
}
