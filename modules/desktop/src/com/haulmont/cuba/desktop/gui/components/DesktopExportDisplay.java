/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.export.FileDataProvider;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import javax.annotation.ManagedBean;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(ExportDisplay.NAME)
@Scope("prototype")
public class DesktopExportDisplay implements ExportDisplay {

    private final JFileChooser fileChooser = new JFileChooser();

    /**
     * Show/Download resource at client side
     *
     * @param dataProvider {@link ExportDataProvider}
     * @param resourceName ResourceName for client side
     * @param format       {@link ExportFormat}
     * @see com.haulmont.cuba.gui.export.FileDataProvider
     * @see com.haulmont.cuba.gui.export.ByteArrayDataProvider
     */
    @Override
    public void show(final ExportDataProvider dataProvider, String resourceName, ExportFormat format) {
        final JFrame mainFrame = App.getInstance().getMainFrame();

        String fileName = resourceName;
        if (format != null) {
            if (StringUtils.isEmpty(getFileExt(fileName)))
                fileName += "." + format.getFileExt();
        }

        String dialogMessage = MessageProvider.getMessage(getClass(), "export.saveFile");
        String fileCaption = MessageProvider.getMessage(getClass(), "export.fileCaption");

        dialogMessage = String.format(dialogMessage, fileName);

        final String finalFileName = fileName;
        App.getInstance().getWindowManager().showOptionDialog(fileCaption, dialogMessage, IFrame.MessageType.CONFIRMATION,
                new com.haulmont.cuba.gui.components.Action[]{
                        new AbstractAction("action.openFile") {
                            @Override
                            public void actionPerform(Component component) {
                                openFileAction(finalFileName, dataProvider);
                            }
                        },
                        new AbstractAction("action.saveFile") {
                            @Override
                            public void actionPerform(Component component) {
                                saveFileAction(finalFileName, mainFrame, dataProvider);
                            }
                        },
                        new AbstractAction("action.cancel") {
                            @Override
                            public void actionPerform(Component component) {
                                // do nothing
                            }
                        }
                });
    }

    private void saveFileAction(String fileName, JFrame mainFrame, ExportDataProvider dataProvider) {
        fileChooser.setSelectedFile(new File(fileName));
        if (fileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveFile(dataProvider, selectedFile);
        }
    }

    private void openFileAction(String finalFileName, ExportDataProvider dataProvider) {
        File destFile = null;
        try {
            destFile = File.createTempFile("tempCubaFile", "." + getFileExt(finalFileName));
        } catch (IOException e) {
            String message = MessageProvider.getMessage(DesktopExportDisplay.class, "export.tempFileError");
            App.getInstance().getWindowManager().showNotification(message, IFrame.NotificationType.WARNING);
        }

        if (destFile != null) {
            if (saveFile(dataProvider, destFile) && Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(destFile);
                } catch (IOException ex) {
                    String message = MessageProvider.getMessage(DesktopExportDisplay.class, "export.openError");
                    App.getInstance().getWindowManager().showNotification(message,
                            IFrame.NotificationType.WARNING);
                }
            }
        }
    }

    /**
     * Show/Download resource at client side
     *
     * @param dataProvider {@link ExportDataProvider}
     * @param resourceName ResourceName for client side
     * @see com.haulmont.cuba.gui.export.FileDataProvider
     * @see com.haulmont.cuba.gui.export.ByteArrayDataProvider
     */
    @Override
    public void show(ExportDataProvider dataProvider, String resourceName) {
        String extension = getFileExt(resourceName);
        ExportFormat format = ExportFormat.getByExtension(extension);
        show(dataProvider, resourceName, format);
    }

    /**
     * Show/Download file at client side
     *
     * @param fileDescriptor File descriptor
     * @param format         {@link ExportFormat}
     */
    @Override
    public void show(FileDescriptor fileDescriptor, ExportFormat format) {
        show(new FileDataProvider(fileDescriptor), fileDescriptor.getName(), format);
    }

    private boolean saveFile(ExportDataProvider dataProvider, File destinationFile) {
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
            String message = MessageProvider.getMessage(DesktopExportDisplay.class, "export.saveError");
            App.getInstance().getWindowManager().showNotification(message, IFrame.NotificationType.WARNING);
            return false;
        }
        return true;
    }

    private String getFileExt(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > -1)
            return StringUtils.substring(fileName, i + 1, i + 20);
        else
            return "";
    }
}