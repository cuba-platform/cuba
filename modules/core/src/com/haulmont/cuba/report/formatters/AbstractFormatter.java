/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 25.06.2010 14:07:49
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.ReportOutputType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractFormatter implements Formatter, ReportEngine {
    public static final String UNIVERSAL_ALIAS_PATTERN = "\\$\\{[a-z|A-Z|0-9|_|\\.]+?\\}";
    public static final String ALIAS_WITH_BAND_NAME_PATTERN = "\\$\\{[a-z|A-Z|0-9|_]+?\\.[a-z|A-Z|0-9|_|\\.]+?\\}";

    protected static Pattern namePattern;
    protected FileDescriptor templateFile;
    protected ReportOutputType defaultOutputType = null;

    private Set<String> extensions = new HashSet<String>();
    private Set<ReportOutputType> outputTypes = new HashSet<ReportOutputType>();

    static {
        namePattern = Pattern.compile(UNIVERSAL_ALIAS_PATTERN, Pattern.CASE_INSENSITIVE);
    }

    protected void registerReportExtension(String extension) {
        if (StringUtils.isNotEmpty(extension))
            extensions.add(extension.toLowerCase());
    }

    protected void registerReportOutput(ReportOutputType outputType){
        outputTypes.add(outputType);
    }

    @Override
    public FileDescriptor getTemplateFile() {
        return templateFile;
    }

    @Override
    public void setTemplateFile(FileDescriptor templateFile) {
        this.templateFile = templateFile;
    }

    @Override
    public byte[] createDocument(Band rootBand) {
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        createDocument(rootBand, getDefaultOutputType(), resultStream);
        return resultStream.toByteArray();
    }

    @Override
    public boolean hasSupportReport(String reportExtension, ReportOutputType outputType) {
        return extensions.contains(reportExtension) && outputTypes.contains(outputType);
    }

    @Override
    public ReportOutputType getDefaultOutputType() {
        return defaultOutputType;
    }

    protected InputStream getFileInputStream(FileDescriptor fd) {
        FileStorageAPI storageAPI = Locator.lookup(FileStorageAPI.NAME);
        try {
            byte[] arr = IOUtils.toByteArray(storageAPI.openFileInputStream(fd));
            return new ByteArrayInputStream(arr);
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String insertBandDataToString(Band band, String resultStr) {
        List<String> parametersToInsert = new ArrayList<String>();
        Matcher matcher = namePattern.matcher(resultStr);
        while (matcher.find()) {
            parametersToInsert.add(unwrapParameterName(matcher.group()));
        }
        for (String parameterName : parametersToInsert) {
            Object value = band.getData().get(parameterName);
            String valueStr = value != null ? value.toString() : "";
            resultStr = inlineParameterValue(resultStr, parameterName, valueStr);
        }
        return resultStr;
    }

    public static String unwrapParameterName(String nameWithAlias) {
        return nameWithAlias.replaceAll("[\\$|\\{|\\}]", "");
    }

    public static String inlineParameterValue(String template, String parameterName, String value) {
        return template.replaceAll("\\$\\{" + parameterName + "\\}", value);
    }
}