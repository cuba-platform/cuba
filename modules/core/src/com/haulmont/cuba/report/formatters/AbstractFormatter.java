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
import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.report.Band;
import org.apache.commons.lang.RandomStringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractFormatter implements Formatter {
    public static final String VALUE_EXPRESSION_PATTERN = "\\$\\{.+?\\..+?\\}";

    public abstract byte[] createDocument(Band rootBand);

    protected String createTemporaryFile(FileDescriptor fd) {
        FileStorageService fss = Locator.lookup(FileStorageService.NAME);
        try {
            byte[] arr = fss.loadFile(fd);
            File tmpFile = new File(getTempFileName());
            String filePath = tmpFile.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(tmpFile);
            fos.write(arr);
            fos.close();
            return filePath;
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTempFileName() {
        return ConfigProvider.getConfig(ServerConfig.class).getServerTempDir() + "/" + RandomStringUtils.randomNumeric(10) + ".tmp";
    }

    protected InputStream getFileInputStream(FileDescriptor fd) {
        FileStorageService fss = Locator.lookup(FileStorageService.NAME);
        try {
            byte[] arr = fss.loadFile(fd);
            ByteArrayInputStream bis = new ByteArrayInputStream(arr);
            return bis;
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }

    protected String insertBandDataToString(Band band, String resultStr) {
        List<String> parametersToInsert = new ArrayList<String>();
        Pattern namePattern = Pattern.compile("\\$\\{.+?\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = namePattern.matcher(resultStr);
        while (matcher.find()) {
            parametersToInsert.add(matcher.group().replace("${", "").replace("}", ""));
        }
        for (String parameterName : parametersToInsert) {
            Object value = band.getData().get(parameterName);
            String valueStr = value != null ? value.toString() : "";
            resultStr = resultStr.replaceAll("\\$\\{" + parameterName + "\\}", valueStr);
        }
        return resultStr;
    }

    /**
     * Parse value expression string and extract band name and property name from it.
     *
     * @param valueExpression Value expression like ${bandName.propertyName}
     * @return String array with 2 items. First is band name, second is property name.
     */
    protected String[] parseValueExpression(String valueExpression) {
        if (!valueExpression.matches(VALUE_EXPRESSION_PATTERN)) {
            throw new IllegalArgumentException("Invalid value expression (" + valueExpression + ")");
        }
        return valueExpression.replace("${", "").replace("}", "").split("\\.");
    }
}
