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

package com.haulmont.cuba.web.test;

import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.web.toolkit.FileUploadTypesHelper;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class FileUploadTypesHelperTest extends TestCase {

    public void testMIMEConversion() {
        Map<String, String> testData = new HashMap<>();
        testData.put("*.png,*.gif,*.jpeg", "image/png,image/gif,image/jpeg");
        testData.put("*.png,*.gif", "image/png,image/gif");
        testData.put("*.png", "image/png");
        testData.put("*.jpeg", "image/jpeg");
        testData.put("*.jpg", "image/jpeg");
        testData.put(".png,.gif,.jpeg", "image/png,image/gif,image/jpeg");
        testData.put(".png,.gif", "image/png,image/gif");
        testData.put(".png", "image/png");
        testData.put("image/png", FileTypesHelper.DEFAULT_MIME_TYPE);

        for (Map.Entry<String, String> entry : testData.entrySet()) {
            assertEquals(entry.getValue(), FileUploadTypesHelper.convertToMIME(entry.getKey()));
            assertEquals(entry.getValue(), FileUploadTypesHelper.convertToMIME(entry.getKey(), ","));
            assertEquals(entry.getValue(), FileUploadTypesHelper.convertToMIME(entry.getKey(), ",", ","));
        }
    }

    public void testSeparatorConversion() {
        Map<String, String> testData = new HashMap<>();
        testData.put("*.png,*.gif,*.jpeg", "*.png;*.gif;*.jpeg");
        testData.put("*.png,*.gif", "*.png;*.gif");
        testData.put("*.png", "*.png");

        for (Map.Entry<String, String> entry : testData.entrySet()) {
            assertEquals(entry.getValue(), FileUploadTypesHelper.convertSeparator(entry.getKey(), ";"));
            assertEquals(entry.getValue(), FileUploadTypesHelper.convertSeparator(entry.getKey(), ",", ";"));
        }

        for (Map.Entry<String, String> entry : testData.entrySet()) {
            assertEquals(entry.getKey(), FileUploadTypesHelper.convertSeparator(entry.getValue(), ";", ","));
        }
    }
}