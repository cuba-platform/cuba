/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopResources;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopFileMultiUploadField extends DesktopAbstractComponent<JButton> implements FileMultiUploadField {

    private static final int BYTES_IN_MEGABYTE = 1048576;

    private static final String DEFAULT_ICON = "/multiupload/button.png";

    protected FileUploadingAPI fileUploading;

    private List<UploadListener> listeners = new ArrayList<>();

    private Map<UUID, String> filesMap = new HashMap<>();

    private String description = "";

    public DesktopFileMultiUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        DesktopResources resources = App.getInstance().getResources();
        String caption = MessageProvider.getMessage(getClass(), "upload.selectFiles");
        impl = new JButton();
        impl.setAction(new AbstractAction(caption, resources.getIcon(DEFAULT_ICON)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(impl) == JFileChooser.APPROVE_OPTION) {
                    processFiles(fileChooser.getSelectedFiles());
                }
            }
        });
        DesktopComponentsHelper.adjustSize(impl);
    }

    private void processFiles(File[] files) {
        if (checkFiles(files)) {
            uploadFiles(files);
        }
    }

    private void uploadFiles(File[] files) {
        for (File file : files) {
            try {
                notifyStartListeners(file);

                UUID tempFileId = fileUploading.createEmptyFile();

                File tmpFile = fileUploading.getFile(tempFileId);
                FileUtils.copyFile(file, tmpFile);

                filesMap.put(tempFileId, file.getName());

                notifyEndListeners(file);
            } catch (Exception ex) {
                notifyErrorListeners(file, ex.getMessage());
                return;
            }
        }
        notifyQuerCompleteListeners();
    }

    private boolean checkFiles(File[] files) {
        final Integer maxUploadSizeMb = ConfigProvider.getConfig(ClientConfig.class).getMaxUploadSizeMb();
        final long maxSize = maxUploadSizeMb * BYTES_IN_MEGABYTE;

        for (File file : files) {
            if (file.length() > maxSize) {
                notifyFileSizeExceedLimit(file);
                return false;
            }
        }
        return true;
    }

    private void notifyStartListeners(File file) {
        for (UploadListener uploadListener : listeners)
            uploadListener.fileUploadStart(file.getName());
    }

    private void notifyEndListeners(File file) {
        for (UploadListener uploadListener : listeners)
            uploadListener.fileUploaded(file.getName());
    }

    private void notifyQuerCompleteListeners() {
        for (UploadListener uploadListener : listeners)
            uploadListener.queueUploadComplete();
    }

    private void notifyErrorListeners(File file, String message) {
        for (UploadListener uploadListener : listeners)
            uploadListener.errorNotify(file.getName(), message, 0);
    }

    private void notifyFileSizeExceedLimit(File file) {
        String warningMsg = MessageProvider.getMessage(AppConfig.getMessagesPack(), "upload.fileTooBig.message");
        for (UploadListener uploadListener : listeners)
            uploadListener.errorNotify(file.getName(), warningMsg, FileMultiUploadField.FILE_EXCEEDS_SIZE_LIMIT);
    }

    @Override
    public void addListener(UploadListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    @Override
    public void removeListener(UploadListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Map<UUID, String> getUploadsMap() {
        return filesMap;
    }

    @Override
    public String getCaption() {
        return impl.getText();
    }

    @Override
    public void setCaption(String caption) {
        impl.setText(caption);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
