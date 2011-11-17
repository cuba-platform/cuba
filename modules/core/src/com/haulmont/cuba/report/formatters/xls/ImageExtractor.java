/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 20.10.11 16:24
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.xls;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageExtractor {
    private final static Pattern COMMON_PATTERN = Pattern.compile("\\$\\{[A-z]+?:[0-9]+?x[0-9]+?\\}");

    private final static Pattern BITMAP_PATTERN = Pattern.compile("\\$\\{bitmap:([0-9]+?)x([0-9]+?)\\}");
    private final static Pattern IMAGE_PATTERN = Pattern.compile("\\$\\{image:([0-9]+?)x([0-9]+?)\\}");
    private final static Pattern FILE_DESCRIPTOR_PATTERN = Pattern.compile("\\$\\{imageFileId:([0-9]+?)x([0-9]+?)\\}");

    private String formatString;
    private Object paramValue;

    public ImageExtractor(String formatString, Object paramValue) {
        this.formatString = formatString;
        this.paramValue = paramValue;
    }

    public static boolean isImage(String formatString) {
        return COMMON_PATTERN.matcher(formatString).find();
    }

    public Image extract() {
        try {
            Matcher bitmapMatcher = BITMAP_PATTERN.matcher(formatString);
            Matcher imageMatcher = IMAGE_PATTERN.matcher(formatString);
            Matcher fileIdMatcher = FILE_DESCRIPTOR_PATTERN.matcher(formatString);


            Matcher resultMatcher = null;
            byte[] content = null;
            if (bitmapMatcher.find()) {
                resultMatcher = bitmapMatcher;
                content = (byte[]) paramValue;
            } else if (imageMatcher.find()) {
                resultMatcher = imageMatcher;
                content = FileUtils.readFileToByteArray(new File(paramValue.toString()));
            } else if (fileIdMatcher.find()) {
                resultMatcher = fileIdMatcher;
                FileDescriptor fileDescriptor = null;
                if (paramValue instanceof FileDescriptor) {
                    fileDescriptor = (FileDescriptor) paramValue;
                } else {
                    Transaction tx = PersistenceProvider.createTransaction();
                    try {
                        EntityManager em = PersistenceProvider.getEntityManager();
                        em.setView(MetadataProvider.getViewRepository().getView(FileDescriptor.class, View.LOCAL));
                        fileDescriptor = em.find(FileDescriptor.class, UUID.fromString(paramValue.toString()));
                        tx.commit();
                    } finally {
                        tx.end();
                    }
                }

                FileStorageAPI fileStorageAPI = Locator.lookup(FileStorageAPI.NAME);
                content = fileStorageAPI.loadFile(fileDescriptor);
            }

            return paintImage(resultMatcher, content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Image paintImage(Matcher resultMatcher, byte[] content) {
        if (resultMatcher == null) {
            return null;
        } else {
            Integer width = Integer.valueOf(resultMatcher.group(1));
            Integer height = Integer.valueOf(resultMatcher.group(2));
            return new Image(width, height, content);
        }
    }

    public static class Image {
        private int width;
        private int height;
        private byte[] content;

        public Image(int width, int height, byte[] content) {
            this.width = width;
            this.height = height;
            this.content = content;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public byte[] getContent() {
            return content;
        }
    }
}