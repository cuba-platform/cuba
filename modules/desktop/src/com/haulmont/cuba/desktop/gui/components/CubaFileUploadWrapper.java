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
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.sys.vcl.FocusableComponent;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;

public class CubaFileUploadWrapper extends JComponent implements FocusableComponent {
    protected Messages messages = AppBeans.get(Messages.NAME);

    protected DesktopHBox container;
    protected LinkButton fileNameButton;
    protected Button uploadButton;
    protected Button clearButton;

    protected boolean showFileName = false;
    protected boolean showClearButton = true;
    protected boolean editable = true;
    protected boolean required = false;

    protected String fileName;

    private String caption;
    private String description;

    public CubaFileUploadWrapper(Button uploadButton) {
        setLayout(new BorderLayout());

        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);
        container = (DesktopHBox) componentsFactory.createComponent(DesktopHBox.NAME);
        add(container.unwrap(JPanel.class));

        fileNameButton = (LinkButton) componentsFactory.createComponent(LinkButton.NAME);
        setButtonCaption(null);
        container.add(fileNameButton);

        this.uploadButton = uploadButton;
        JButton jUploadButton = uploadButton.unwrap(JButton.class);
        jUploadButton.setMargin(new Insets(0, 0, 0, 0));
        container.add(uploadButton);

        clearButton = (Button) componentsFactory.createComponent(Button.NAME);
        JButton jClearButton = clearButton.unwrap(JButton.class);
        jClearButton.setMargin(new Insets(0, 0, 0, 0));
        container.add(clearButton);

        setShowFileName(false);
    }

    public boolean isShowFileName() {
        return showFileName;
    }

    public void setShowFileName(boolean showFileName) {
        this.showFileName = showFileName;

        fileNameButton.setVisible(showFileName);
        if (showFileName) {
            container.expand(fileNameButton);
        } else {
            container.expand(uploadButton);
        }
    }

    public void setButtonCaption(String fileName) {
        this.fileName = fileName;

        if (StringUtils.isNotEmpty(fileName)) {
            fileNameButton.setCaption(fileName);
        } else {
            fileNameButton.setCaption(messages.getMainMessage("FileUploadField.fileNotSelected"));
        }
    }

    public void setFileNameButtonClickAction(com.haulmont.cuba.gui.components.AbstractAction action) {
        fileNameButton.setAction(action);
        fileNameButton.setVisible(isShowFileName());
    }

    public void setEditable(boolean editable) {
        this.editable = editable;

        updateButtonsVisibility();
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isShowClearButton() {
        return showClearButton;
    }

    public void setShowClearButton(boolean showClearButton) {
        this.showClearButton = showClearButton;

        updateButtonsVisibility();
    }

    public void setClearButtonCaption(String caption) {
        clearButton.setCaption(caption);
    }

    public String getClearButtonCaption() {
        return clearButton.getCaption();
    }

    public void setClearButtonIcon(String icon) {
        clearButton.setIcon(icon);
    }

    public String getClearButtonIcon() {
        return clearButton.getIcon();
    }

    public void setClearButtonAction(Action clearButtonAction) {
        clearButton.setAction(clearButtonAction);
    }

    public void setClearButtonDescription(String description) {
        clearButton.setDescription(description);
    }

    public String getClearButtonDescription() {
        return clearButton.getDescription();
    }

    public void setUploadButtonDescription(String description) {
        uploadButton.setDescription(description);
    }

    public String getUploadButtonDescription() {
        return uploadButton.getDescription();
    }

    public void setRequired(boolean required) {
        this.required = required;

        updateButtonsVisibility();
    }

    public boolean isRequired() {
        return required;
    }

    protected void updateButtonsVisibility() {
        uploadButton.setVisible(editable);

        if (editable && !isRequired() && showClearButton) {
            clearButton.setVisible(true);
        } else {
            clearButton.setVisible(false);
        }
    }

    @Override
    public void focus() {
        uploadButton.requestFocus();
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}