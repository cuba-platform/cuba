/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 18.03.11 13:11
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.ReportOutputType;
import com.haulmont.cuba.report.exception.ReportingException;
import com.haulmont.cuba.report.exception.UnsupportedFormatException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Engine for create reports with HTML templates and FreeMarker markup
 */
public class HtmlFormatter extends AbstractFormatter {

    public HtmlFormatter() {
        registerReportExtension("htm");
        registerReportExtension("html");

        registerReportOutput(ReportOutputType.HTML);
        registerReportOutput(ReportOutputType.PDF);

        defaultOutputType = ReportOutputType.HTML;
    }

    public void createDocument(Band rootBand, ReportOutputType outputType, OutputStream outputStream) {

        if (templateFile == null)
            throw new NullPointerException();

        switch (outputType) {
            case HTML:
                writeHtmlDocument(rootBand, outputStream);
                break;

            case PDF:
                ByteArrayOutputStream htmlOuputStream = new ByteArrayOutputStream();
                writeHtmlDocument(rootBand, htmlOuputStream);

                String htmlContent = new String(htmlOuputStream.toByteArray());
                renderPdfDocument(htmlContent, outputStream);
                break;

            default:
                throw new UnsupportedFormatException();
        }
    }

    private void renderPdfDocument(String htmlContent, OutputStream outputStream) {
        ITextRenderer renderer = new ITextRenderer();
        try {
            File tmpFile = File.createTempFile("htmlReport",".htm");
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(tmpFile));
            dataOutputStream.write(htmlContent.getBytes(Charset.forName("UTF-8")));
            dataOutputStream.close();

            String url = tmpFile.toURI().toURL().toString();
            renderer.setDocument(url);

            renderer.layout();
            renderer.createPDF(outputStream);

            FileUtils.deleteQuietly(tmpFile);
        } catch (Exception e) {
            throw new ReportingException(e);
        }
    }

    private void writeHtmlDocument(Band rootBand, OutputStream outputStream) {
        Map templateModel = getTemplateModel(rootBand);

        Template htmlTemplate = getTemplate();
        Writer htmlWriter = new OutputStreamWriter(outputStream);

        try {
            htmlTemplate.process(templateModel, htmlWriter);
            htmlWriter.close();
        } catch (ReportingException e) {
            throw e;
        } catch (Exception e) {
            throw new ReportingException(e);
        }
    }

    private Map getTemplateModel(Band rootBand) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(rootBand.getName(), getBandModel(rootBand));
        return model;
    }

    private Map getBandModel(Band band) {
        Map<String, Object> model = new HashMap<String, Object>();

        Map<String, Object> bands = new HashMap<String, Object>();
        for (String bandName : band.getChildrenBands().keySet()) {
            List<Band> subBands = band.getChildrenBands().get(bandName);
            List<Map> bandModels = new ArrayList<Map>();
            for (Band child : subBands)
                bandModels.add(getBandModel(child));

            bands.put(bandName, bandModels);
        }
        model.put("bands", bands);

        model.put("fields", band.getData());

        return model;
    }

    private Template getTemplate() {
        if (templateFile == null)
            throw new NullPointerException();

        String templateContent;
        FileStorageAPI storageAPI = Locator.lookup(FileStorageAPI.NAME);
        try {
            byte[] templateBytes = storageAPI.loadFile(templateFile);
            templateContent = new String(templateBytes);
        } catch (FileStorageException e) {
            throw new ReportingException(e);
        }
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate(templateFile.getFileName(), templateContent);

        Configuration fmConfiguration = new Configuration();
        fmConfiguration.setTemplateLoader(stringLoader);
        fmConfiguration.setDefaultEncoding("UTF-8");

        Template htmlTemplate;
        try {
            htmlTemplate = fmConfiguration.getTemplate(templateFile.getFileName());
        } catch (Exception e) {
            throw new ReportingException(e);
        }
        return htmlTemplate;
    }
}
