/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.toolkit;

import com.haulmont.cuba.core.global.FileTypesHelper;
import org.apache.commons.lang.StringUtils;

/**
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
