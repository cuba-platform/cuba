/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.Resources;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class DesktopFileMultiUploadField extends DesktopAbstractComponent<JButton> implements FileMultiUploadField {

    private static final String DEFAULT_ICON = "/multiupload/button.png";

    protected FileUploadingAPI fileUploading;

    private List<UploadListener> listeners = new ArrayList<UploadListener>();

    private Map<UUID, String> files = new HashMap<UUID, String>();

    private String description = "";

    public DesktopFileMultiUploadField() {
        fileUploading = AppContext.getBean(FileUploadingAPI.NAME);

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        Resources resources = App.getInstance().getResources();
        String caption = MessageProvider.getMessage(getClass(), "selectFiles");
        impl = new JButton();
        impl.setAction(new AbstractAction(caption, resources.getIcon(DEFAULT_ICON)) {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        expandable = true;
        DesktopComponentsHelper.adjustSize(impl);
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
        return files;
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
