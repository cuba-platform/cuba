/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.export.FileDataProvider;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import javax.annotation.ManagedBean;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@ManagedBean(ExportDisplay.NAME)
@Scope("prototype")
public class DesktopExportDisplay implements ExportDisplay {

    private final JFileChooser fileChooser = new JFileChooser();

    @Override
    public void show(ExportDataProvider dataProvider, String resourceName, ExportFormat format) {
        JFrame mainFrame = App.getInstance().getMainFrame();

        String fileName = resourceName;
        if (format != null) {
            if (StringUtils.isEmpty(getFileExt(fileName)))
                fileName += "." + format.getFileExt();
        }

        String dialogCaption = MessageProvider.getMessage(getClass(), "saveFile");
        String fileCaption = MessageProvider.getMessage(getClass(), "fileCaption") + " : " + fileName;
        int result = JOptionPane.showConfirmDialog(mainFrame, fileCaption, dialogCaption,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            if (fileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                saveFile(dataProvider, fileChooser.getSelectedFile());
            }
        }
    }

    /**
     * Show/Download resource at client side
     *
     * @param dataProvider ExportDataProvider
     * @param resourceName ResourceName for client side
     * @see com.haulmont.cuba.gui.export.FileDataProvider
     * @see com.haulmont.cuba.gui.export.ByteArrayDataProvider
     */
    public void show(ExportDataProvider dataProvider, String resourceName) {
        String extension = getFileExt(resourceName);
        ExportFormat format = ExportFormat.getByExtension(extension);
        show(dataProvider, resourceName, format);
    }

    public void show(FileDescriptor fileDescriptor, ExportFormat format) {
        show(new FileDataProvider(fileDescriptor), fileDescriptor.getName(), format);
    }

    private void saveFile(ExportDataProvider dataProvider, File destinationFile) {
        try {
            if (!destinationFile.exists()) {
                boolean crateResult = destinationFile.createNewFile();
                if (!crateResult)
                    throw new IOException("Couldn't create file");
            }

            try {
                InputStream fileInput = dataProvider.provide();
                FileOutputStream outputStream = new FileOutputStream(destinationFile);

                // TODO Need progress window
                IOUtils.copy(fileInput, outputStream);

                IOUtils.closeQuietly(fileInput);
                IOUtils.closeQuietly(outputStream);
            } finally {
                dataProvider.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileExt(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > -1)
            return StringUtils.substring(fileName, i + 1, i + 20);
        else
            return "";
    }
}