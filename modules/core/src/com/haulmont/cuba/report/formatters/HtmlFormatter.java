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
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.exception.ReportFormatterException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlFormatter extends AbstractFormatter {

    private FileDescriptor templateFileDescriptor;

    public HtmlFormatter(FileDescriptor templateFileDescriptor) {
        this.templateFileDescriptor = templateFileDescriptor;
    }

    @Override
    public byte[] createDocument(Band rootBand) {
        ByteArrayOutputStream resultContent = new ByteArrayOutputStream();

        Map templateModel = getTemplateModel(rootBand);

        Template htmlTemplate = getTemplate();
        OutputStreamWriter resultWriter = new OutputStreamWriter(resultContent);

        try {
            htmlTemplate.process(templateModel, resultWriter);
            resultWriter.close();
        } catch (ReportFormatterException e) {
            throw e;
        } catch (Exception e) {
            throw new ReportFormatterException(e);
        }

        return resultContent.toByteArray();
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
        String templateContent;
        FileStorageAPI storageAPI = Locator.lookup(FileStorageAPI.NAME);
        try {
            byte[] templateBytes = storageAPI.loadFile(templateFileDescriptor);
            templateContent = new String(templateBytes);
        } catch (FileStorageException e) {
            throw new ReportFormatterException(e);
        }
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate(templateFileDescriptor.getFileName(), templateContent);

        Configuration fmConfiguration = new Configuration();
        fmConfiguration.setTemplateLoader(stringLoader);
        fmConfiguration.setDefaultEncoding("UTF-8");

        Template htmlTemplate;
        try {
            htmlTemplate = fmConfiguration.getTemplate(templateFileDescriptor.getFileName());
        } catch (Exception e) {
            throw new ReportFormatterException(e);
        }
        return htmlTemplate;
    }
}
