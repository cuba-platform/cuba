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

import com.haulmont.cuba.core.app.JasperReportService;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.report.ReportOutputDocument;
import com.haulmont.cuba.report.ReportOutputType;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.app.ui.report.ReportHelper;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
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

public class JasperReportHelper
{
    public static JasperPrint executeJasperReport(String name, Map<String, Object> params) {
        JasperReportService rs = ServiceLocator.lookup(JasperReportService.JNDI_NAME);
        return rs.executeJasperReport(name, params);
    }

    public static JasperPrint executeJasperReport(String name, Map<String, Object> params, JRDataSource dataSource) {
        JasperReportService rs = ServiceLocator.lookup(JasperReportService.JNDI_NAME);
        return rs.executeJasperReport(name, params, dataSource);
    }

    public static void printJasperReport(String name, ReportOutputDocument output) {
        printJasperReport(name, new HashMap(), output);
    }

    public static void printJasperReport(String name, Map<String, Object> params, ReportOutputDocument output) {
        JasperPrint print = executeJasperReport(name, params);
        printJasperReport(name, print, output);
    }

    public static void printJasperReport(List<String> names, Map<String, Object> params, ReportOutputDocument output, JRDataSource dataSource) {
        List<JasperPrint> prints = new ArrayList<JasperPrint>();
        for (String str : names) {
            prints.add(executeJasperReport(str, params, dataSource));
        }
        if (!names.isEmpty()) {
            printJasperReport(names.iterator().next(), prints, output);
        }
    }

    public static void printJasperReport(String name, JasperPrint jasperPrint, ReportOutputDocument output) {
        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        jasperPrints.add(jasperPrint);
        printJasperReport(name, jasperPrints, output);
    }

    public static void printJasperReport(String name, List<JasperPrint> jasperPrint, ReportOutputDocument output) {
        WebExportDisplay display = new WebExportDisplay();

        switch (output.getOutputType()) {
            case PDF:
            case XLS:
                byte[] bytes = exportJasperReportPdfXls(name, jasperPrint, output);
                display.show(new ByteArrayDataProvider(bytes), name, ReportHelper.getExportFormat(output.getOutputType()));
                break;

            case HTML:
                HttpSession httpSession = ((WebApplicationContext) App.getInstance().getContext()).getHttpSession();
                httpSession.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
                String str = exportJasperReportHtml(name, jasperPrint);
                display.show(new ByteArrayDataProvider(str.getBytes()), name);
                break;
        }
    }

    public static byte[] printJasperReportToBytes(String name, Map<String, Object> params, ReportOutputDocument output) {
        JasperPrint print = executeJasperReport(name, params);
        return printJasperReportToBytes(name, print, output);
    }

    public static byte[] printJasperReportToBytes(String name, JasperPrint jasperPrint, ReportOutputDocument output) {
        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        jasperPrints.add(jasperPrint);
        return printJasperReportToBytes(name, jasperPrints, output);
    }
    
    public static byte[] printJasperReportToBytes(String name, List<JasperPrint> jasperPrint, ReportOutputDocument output) {
        switch (output.getOutputType()) {
            case PDF:
            case XLS:
                return exportJasperReportPdfXls(name, jasperPrint, output);

            case HTML:
                return exportJasperReportHtml(name, jasperPrint).getBytes();

            default:
                throw new UnsupportedOperationException("Format not supported: " + output.getOutputType());
        }
    }

    private static byte[] exportJasperReportPdfXls(String name, List<JasperPrint> jasperPrint, ReportOutputDocument output) {
        JRAbstractExporter exporter;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (output.getOutputType() == ReportOutputType.PDF) {
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
        return outputStream.toByteArray();
    }

    private static String exportJasperReportHtml(String name, List<JasperPrint> jasperPrint) {
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
        return writer.toString();
    }
}
