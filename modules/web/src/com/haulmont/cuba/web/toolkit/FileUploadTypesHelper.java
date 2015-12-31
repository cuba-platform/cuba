/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit;

import com.haulmont.cuba.core.global.FileTypesHelper;
import org.apache.commons.lang.StringUtils;

/**
 * @author gorelov
 * @version $Id$
 */
public final class FileUploadTypesHelper {

    public static final String TYPES_SPLITTER = ",";

    private FileUploadTypesHelper() {
    }

    public static String convertToMIME(String types) {
        return convertToMIME(types, TYPES_SPLITTER);
    }

    public static String convertToMIME(String types, String separator) {
        return convertToMIME(types, TYPES_SPLITTER, separator);
    }

    public static String convertToMIME(String types, String originSeparator, String separator) {
        return StringUtils.isNotBlank(types) ? convertToMIME(types.split(originSeparator), separator) : null;
    }

    public static String convertToMIME(String[] types, String separator) {
        for (int i = 0; i < types.length; i++) {
            types[i] = FileTypesHelper.getMIMEType(types[i]);
        }
        return String.join(separator, types);
    }

    public static String convertSeparator(String types, String separator) {
        return convertSeparator(types, TYPES_SPLITTER, separator);
    }

    public static String convertSeparator(String types, String originSeparator, String separator) {
        return StringUtils.isNotBlank(types) ? types.replace(originSeparator, separator) : null;
    }
}
