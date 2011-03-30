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
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.ReportOutputType;
import com.haulmont.cuba.report.exception.ReportFormatterException;
import com.haulmont.cuba.report.formatters.exception.UnsupportedFormatException;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.html.simpleparser.ChainedProperties;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.ImageProvider;
import com.lowagie.text.pdf.PdfWriter;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Engine for create reports with HTML tamplates and FreeMarker markup
 */
public class HtmlFormatter extends AbstractFormatter {

    private static final String HTML_IMAGE_PROVIDER = "img_provider";
    private static final String HTML_FONT_FACTORY = "font_factory";

    private static final String PDF_DEFAULT_ENCODING = "Cp1251";
    private static final String PDF_DEFAULT_FONT = "Times New Roman";

    private static final String CUBA_FONTS_DIR = "/cuba/fonts";

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

                ByteArrayInputStream htmlInputStream = new ByteArrayInputStream(
                        htmlContent.getBytes(Charset.forName(PDF_DEFAULT_ENCODING)));

                writePdfDocument(htmlInputStream, outputStream);
                break;

            default:
                throw new UnsupportedFormatException();
        }
    }

    private void writePdfDocument(InputStream htmlInput, OutputStream outputStream) {
//        String htmlContent = htmlWriter.getBuffer().toString();
        /*ByteArrayInputStream htmlInput = new ByteArrayInputStream(
                htmlContent.getBytes(Charset.forName(PDF_DEFAULT_ENCODING)));*/
        InputStreamReader htmlReader = new InputStreamReader(htmlInput, Charset.forName(PDF_DEFAULT_ENCODING));

        GlobalConfig config = ConfigProvider.getConfig(GlobalConfig.class);
        String fontsDir = config.getConfDir() + CUBA_FONTS_DIR;
        FontsLoader fontsLoader = new FontsLoader();

        int registeredFonts = fontsLoader.registerDirectory(fontsDir);
        if (registeredFonts == 0)
            return;

        Document document = new Document(PageSize.A4);
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
            document.open();
            document.addCreationDate();

            HashMap workerProps = new HashMap();
            workerProps.put(HTML_IMAGE_PROVIDER, new HtmlImageLoader());
            workerProps.put(HTML_FONT_FACTORY, fontsLoader);

            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.setInterfaceProps(workerProps);
            htmlWorker.parse(htmlReader);

            document.close();
            pdfWriter.close();
        } catch (ReportFormatterException e) {
            throw e;
        } catch (Exception e) {
            throw new ReportFormatterException(e);
        }
    }

    private void writeHtmlDocument(Band rootBand, OutputStream outputStream) {
        Map templateModel = getTemplateModel(rootBand);

        Template htmlTemplate = getTemplate();
        Writer htmlWriter = new OutputStreamWriter(outputStream);

        try {
            htmlTemplate.process(templateModel, htmlWriter);
            htmlWriter.close();
        } catch (ReportFormatterException e) {
            throw e;
        } catch (Exception e) {
            throw new ReportFormatterException(e);
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
            throw new ReportFormatterException(e);
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
            throw new ReportFormatterException(e);
        }
        return htmlTemplate;
    }

    private class FontsLoader extends FontFactoryImp {

        public FontsLoader() {
            defaultEncoding = PDF_DEFAULT_ENCODING;
            defaultEmbedding = true;
        }

        public Font getFont(String fontName, String encoding, boolean embedded, float size,
                            int style, Color color, boolean cached) {
            if (fontName == null || size == 0) {
                fontName = PDF_DEFAULT_FONT;
            }

            return super.getFont(fontName, PDF_DEFAULT_ENCODING, embedded, size, style, color, cached);
        }
    }

    private class HtmlImageLoader implements ImageProvider {
        public Image getImage(String src, HashMap h, ChainedProperties cprops, DocListener doc) {
            Image image = null;
            try {
                image = Image.getInstance(new URL(src));
            } catch (Exception ignored) {
            }
            return image;
        }
    }
}
