/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopResources;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.IFrame;
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

    private static final String DEFAULT_ICON = "/components/multiupload/multiupload-button.png";

    protected FileUploadingAPI fileUploading;

    private List<UploadListener> listeners = new ArrayList<>();

    private Map<UUID, String> filesMap = new HashMap<>();

    private String description = "";

    public DesktopFileMultiUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        DesktopResources resources = App.getInstance().getResources();
        String caption = AppBeans.get(Messages.class).getMessage(getClass(), "upload.selectFiles");
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
        ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
        final Integer maxUploadSizeMb = clientConfig.getMaxUploadSizeMb();
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
            uploadListener.uploadError(file.getName());
    }

    private void notifyFileSizeExceedLimit(File file) {
        Messages messages = AppBeans.get(Messages.class);

        ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
        final Integer maxUploadSizeMb = clientConfig.getMaxUploadSizeMb();

        String warningMsg = messages.formatMessage(AppConfig.getMessagesPack(), "upload.fileTooBig.message", file.getName(), maxUploadSizeMb);
        WindowManager wm = AppBeans.get(WindowManagerProvider.class).get();
        wm.showNotification(warningMsg, IFrame.NotificationType.WARNING);
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
    public void clearUploads() {
        filesMap.clear();
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
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
    }
}