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

package com.haulmont.cuba.core.app.filestorage.amazon;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.testsupport.TestContainer;
import org.apache.commons.io.IOUtils;
import org.junit.*;

import java.io.InputStream;
import java.util.Date;

@Ignore
public class AmazonS3FileStorageTest {
    public static final String FILE_CONTENT = "This text is for Amazon S3 service test. Second version.";
    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    protected AmazonS3FileStorage fileStorageAPI;

    protected FileDescriptor fileDescr;
    protected FileDescriptor fileDescr2;

    @Before
    public void setUp() throws Exception {
        fileDescr = new FileDescriptor();
        fileDescr.setCreateDate(new Date());
        fileDescr.setSize((long) FILE_CONTENT.length());
        fileDescr.setName("AmazonFileStorageTest");
        fileDescr.setExtension("txt");

        fileDescr2 = new FileDescriptor();
        fileDescr2.setCreateDate(new Date());
        fileDescr2.setSize((long) FILE_CONTENT.length());
        fileDescr2.setName("AmazonFileStorageTest");

        fileStorageAPI = new AmazonS3FileStorage();
        fileStorageAPI.configuration = AppBeans.get(Configuration.NAME);
    }

    @Test
    public void testWithExtension() throws Exception {
        fileStorageAPI.saveFile(fileDescr, FILE_CONTENT.getBytes());

        InputStream inputStream = fileStorageAPI.openStream(fileDescr);
        Assert.assertEquals(FILE_CONTENT, IOUtils.toString(inputStream));

        boolean fileExists = fileStorageAPI.fileExists(fileDescr);
        Assert.assertTrue(fileExists);

        fileStorageAPI.removeFile(fileDescr);
    }

    @Test
    public void testWithoutExtension() throws Exception {
        fileStorageAPI.saveFile(fileDescr2, FILE_CONTENT.getBytes());

        InputStream inputStream = fileStorageAPI.openStream(fileDescr2);
        Assert.assertEquals(FILE_CONTENT, IOUtils.toString(inputStream));

        boolean fileExists = fileStorageAPI.fileExists(fileDescr2);
        Assert.assertTrue(fileExists);

        fileStorageAPI.removeFile(fileDescr2);
    }
}