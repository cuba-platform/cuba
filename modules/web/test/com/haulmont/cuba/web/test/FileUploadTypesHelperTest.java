/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.test;

import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.web.toolkit.FileUploadTypesHelper;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gorelov
 * @version $Id$
 */
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
