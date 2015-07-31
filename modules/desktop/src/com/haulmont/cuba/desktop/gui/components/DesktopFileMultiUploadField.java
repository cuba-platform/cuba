/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopResources;
import com.haulmont.cuba.desktop.TopLevelFrame;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;

import static com.haulmont.cuba.gui.upload.FileUploadingAPI.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopFileMultiUploadField extends DesktopAbstractComponent<JButton> implements FileMultiUploadField {

    protected static final int BYTES_IN_MEGABYTE = 1048576;

    protected static final String DEFAULT_ICON = "/components/multiupload/multiupload-button.png";

    protected FileUploadingAPI fileUploading;

    protected List<UploadListener> listeners = new ArrayList<>();

    protected Map<UUID, String> filesMap = new HashMap<>();

    public DesktopFileMultiUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        DesktopResources resources = App.getInstance().getResources();
        Messages messages = AppBeans.get(Messages.NAME);
        String caption = messages.getMessage(getClass(), "upload.selectFiles");
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

    protected void processFiles(File[] files) {
        if (checkFiles(files)) {
            uploadFiles(files);
        }
    }

    protected void uploadFiles(File[] files) {
        for (File file : files) {
            try {
                notifyStartListeners(file);

                FileInfo fileInfo = fileUploading.createFile();
                UUID tempFileId = fileInfo.getId();
                File tmpFile = fileInfo.getFile();

                FileUtils.copyFile(file, tmpFile);

                filesMap.put(tempFileId, file.getName());

                notifyEndListeners(file);
            } catch (Exception ex) {
                if (!notifyErrorListeners(file)) {
                    Messages messages = AppBeans.get(Messages.NAME);
                    String uploadError = messages.formatMessage(DesktopFileMultiUploadField.class,
                            "multiupload.uploadError", file.getName());

                    TopLevelFrame topLevelFrame = DesktopComponentsHelper.getTopLevelFrame(this);
                    topLevelFrame.showNotification(uploadError, IFrame.NotificationType.ERROR);
                }
                return;
            }
        }
        notifyQuerCompleteListeners();
    }

    protected boolean checkFiles(File[] files) {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        final long maxUploadSizeMb = clientConfig.getMaxUploadSizeMb();
        final long maxSize = maxUploadSizeMb * BYTES_IN_MEGABYTE;

        for (File file : files) {
            if (file.length() > maxSize) {
                notifyFileSizeExceedLimit(file);
                return false;
            }
        }
        return true;
    }

    protected void notifyStartListeners(File file) {
        for (UploadListener uploadListener : listeners)
            uploadListener.fileUploadStart(file.getName());
    }

    protected void notifyEndListeners(File file) {
        for (UploadListener uploadListener : listeners)
            uploadListener.fileUploaded(file.getName());
    }

    protected void notifyQuerCompleteListeners() {
        for (UploadListener uploadListener : listeners)
            uploadListener.queueUploadComplete();
    }

    protected boolean notifyErrorListeners(File file) {
        boolean handled = false;
        for (UploadListener uploadListener : listeners)
            handled = handled | uploadListener.uploadError(file.getName());
        return handled;
    }

    protected void notifyFileSizeExceedLimit(File file) {
        Messages messages = AppBeans.get(Messages.NAME);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        final int maxUploadSizeMb = clientConfig.getMaxUploadSizeMb();

        String warningMsg = messages.formatMainMessage("upload.fileTooBig.message", file.getName(), maxUploadSizeMb);

        getFrame().showNotification(warningMsg, IFrame.NotificationType.WARNING);
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
        return Collections.unmodifiableMap(filesMap);
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