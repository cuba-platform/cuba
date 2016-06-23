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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.TopLevelFrame;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.export.FileDataProvider;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Allows to show exported data in external desktop app or download it.
 */
@org.springframework.stereotype.Component(ExportDisplay.NAME)
@Scope("prototype")
public class DesktopExportDisplay implements ExportDisplay {

    @Inject
    protected BackgroundWorker backgroundWorker;

    @Inject
    protected Messages messages;

    protected Frame frame;

    public DesktopExportDisplay() {
    }

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
        backgroundWorker.checkUIAccess();

        String fileName = resourceName;
        if (format != null) {
            if (StringUtils.isEmpty(getFileExt(fileName)))
                fileName += "." + format.getFileExt();
        }

        String dialogMessage = messages.getMessage(getClass(), "export.saveFile");
        String fileCaption = messages.getMessage(getClass(), "export.fileCaption");

        dialogMessage = String.format(dialogMessage, fileName);

        final String finalFileName = fileName;
        getFrame().getWindowManager().showOptionDialog(fileCaption, dialogMessage, Frame.MessageType.CONFIRMATION,
                new com.haulmont.cuba.gui.components.Action[]{
                        new AbstractAction("action.openFile", Status.PRIMARY) {
                            @Override
                            public void actionPerform(Component component) {
                                openFileAction(finalFileName, dataProvider);
                            }
                        },
                        new AbstractAction("action.saveFile") {
                            @Override
                            public void actionPerform(Component component) {
                                saveFileAction(finalFileName, getFrame(), dataProvider);
                            }
                        },
                        new AbstractAction("actions.Cancel") {
                            @Override
                            public void actionPerform(Component component) {
                                // do nothing
                            }
                        }
                });
    }

    private void saveFileAction(String fileName, JFrame frame, ExportDataProvider dataProvider) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(fileName));
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            boolean success = saveFile(dataProvider, selectedFile);
            TopLevelFrame mainFrame = App.getInstance().getMainFrame();
            if (success) {
                mainFrame.showNotification(messages.getMessage(DesktopExportDisplay.class, "export.saveSuccess"), Frame.NotificationType.TRAY);
            } else {
                mainFrame.showNotification(messages.getMessage(DesktopExportDisplay.class, "export.saveError"), Frame.NotificationType.ERROR);
            }
        }
    }

    private void openFileAction(String finalFileName, ExportDataProvider dataProvider) {
        File destFile = null;
        try {
            destFile = File.createTempFile("get_" + FilenameUtils.getBaseName(finalFileName), "." + getFileExt(finalFileName));
        } catch (IOException e) {
            String message = messages.getMessage(DesktopExportDisplay.class, "export.tempFileError");
            getFrame().getWindowManager().showNotification(message, Frame.NotificationType.WARNING);
        }

        if (destFile != null) {
            if (Desktop.isDesktopSupported() && saveFile(dataProvider, destFile)) {
                try {
                    Desktop.getDesktop().open(destFile);
                } catch (IOException ex) {
                    String message = messages.getMessage(DesktopExportDisplay.class, "export.openError");
                    getFrame().getWindowManager().showNotification(message,
                            Frame.NotificationType.WARNING);
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

    @Override
    public void show(FileDescriptor fileDescriptor) {
        ExportFormat format = ExportFormat.getByExtension(fileDescriptor.getExtension());
        show(fileDescriptor, format);
    }

    @Override
    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    protected boolean saveFile(ExportDataProvider dataProvider, File destinationFile) {
        try {
            if (!destinationFile.exists()) {
                boolean crateResult = destinationFile.createNewFile();
                if (!crateResult)
                    throw new IOException("Couldn't create file");
            }

            InputStream fileInput = null;
            try {
                fileInput = dataProvider.provide();
                FileOutputStream outputStream = new FileOutputStream(destinationFile);

                IOUtils.copy(fileInput, outputStream);

                IOUtils.closeQuietly(fileInput);
                IOUtils.closeQuietly(outputStream);
            } finally {
                if (fileInput != null) {
                    IOUtils.closeQuietly(fileInput);
                }
            }
        } catch (IOException e) {
            String message = messages.getMessage(DesktopExportDisplay.class, "export.saveError");
            getFrame().getWindowManager().showNotification(message, com.haulmont.cuba.gui.components.Frame.NotificationType.WARNING);
            return false;
        }
        return true;
    }

    protected String getFileExt(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > -1)
            return StringUtils.substring(fileName, i + 1, i + 20);
        else
            return "";
    }

    protected TopLevelFrame getFrame() {
        if (frame != null) {
            return DesktopComponentsHelper.getTopLevelFrame(frame);
        } else {
            return App.getInstance().getMainFrame();
        }
    }
}