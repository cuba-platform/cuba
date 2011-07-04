/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.FileUploadField;

import javax.swing.*;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class DesktopFileUploadField extends DesktopAbstractComponent<JPanel> implements FileUploadField {

    public DesktopFileUploadField() {
        impl = new JPanel(new java.awt.FlowLayout());
        impl.setBorder(BorderFactory.createLineBorder(java.awt.Color.gray));
        impl.add(new JLabel("TODO: file upload"));
        // todo stub
    }

    @Override
    public String getFilePath() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getFileName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isUploading() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public UUID getFileId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getBytesRead() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void release() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addListener(Listener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeListener(Listener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCaption() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCaption(String caption) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDescription(String description) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
