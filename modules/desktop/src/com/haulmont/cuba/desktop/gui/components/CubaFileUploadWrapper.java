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
import com.haulmont.cuba.desktop.gui.data.ComponentSize;
import com.haulmont.cuba.desktop.sys.vcl.FocusableComponent;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LinkButton;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

public class CubaFileUploadWrapper extends JComponent implements FocusableComponent {
    protected Messages messages = AppBeans.get(Messages.NAME);

    protected DesktopHBox container;
    protected LinkButton fileNameButton;
    protected Button uploadButton;
    protected Button clearButton;

    protected boolean showFileName = false;
    protected boolean showClearButton = false;
    protected boolean editable = true;
    protected boolean required = false;

    protected String fileName;

    protected String caption;
    protected String description;

    protected Runnable clearButtonListener;
    protected Runnable fileNameButtonClickListener;

    private ComponentSize wrapperWidth;
    private ComponentSize wrapperHeight;

    public CubaFileUploadWrapper(Button uploadButton) {
        setLayout(new BorderLayout());

        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);
        container = (DesktopHBox) componentsFactory.createComponent(DesktopHBox.NAME);
        container.setSpacing(true);
        add(container.unwrap(JPanel.class));

        fileNameButton = (LinkButton) componentsFactory.createComponent(LinkButton.NAME);
        fileNameButton.setVisible(false);
        fileNameButton.setAction(new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                if (fileNameButtonClickListener != null) {
                    fileNameButtonClickListener.run();
                }
            }
        });
        setFileName(null);
        container.add(fileNameButton);
        fileNameButton.setAlignment(Component.Alignment.MIDDLE_LEFT);

        this.uploadButton = uploadButton;
        JButton jUploadButton = uploadButton.unwrap(JButton.class);
        jUploadButton.setMargin(new Insets(0, 0, 0, 0));
        container.add(uploadButton);
        uploadButton.setAlignment(Component.Alignment.MIDDLE_RIGHT);

        clearButton = (Button) componentsFactory.createComponent(Button.NAME);
        clearButton.setAction(new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                if (clearButtonListener != null) {
                    clearButtonListener.run();
                }
            }
        });
        clearButton.setCaption(messages.getMainMessage("FileUploadField.clearButtonCaption"));
        clearButton.setVisible(false);
        JButton jClearButton = clearButton.unwrap(JButton.class);
        jClearButton.setMargin(new Insets(0, 0, 0, 0));
        container.add(clearButton);
        clearButton.setAlignment(Component.Alignment.MIDDLE_RIGHT);

        setShowFileName(false);
    }

    protected void setWidth(String width) {
        wrapperWidth = ComponentSize.parse(width);
        updateComponentWidth();
    }

    protected void setHeight(String height) {
        wrapperHeight = ComponentSize.parse(height);
        updateComponentHeight();
    }

    protected void updateComponentWidth() {
        if (container == null || wrapperWidth == null)
            return;

        if (wrapperWidth.value > 0) {
            container.setWidth("100%");
            if (isShowFileName()) {
                container.expand(fileNameButton);
            } else {
                container.resetExpanded();
                if (isShowClearButton() && !isRequired()) {
                    uploadButton.setWidth("50%");
                    clearButton.setWidth("50%");
                } else {
                    uploadButton.setWidth("100%");
                }
            }
        } else {
            container.setWidth("-1");
            fileNameButton.setWidth("-1");
            uploadButton.setWidth("-1");
            clearButton.setWidth("-1");
        }
    }

    protected void updateComponentHeight() {
        if (container == null || wrapperHeight == null)
            return;

        if (wrapperHeight.value > 0) {
            container.setHeight("100%");
            uploadButton.setHeight("100%");
        } else {
            container.setHeight("-1");
            fileNameButton.setHeight("-1");
            uploadButton.setHeight("-1");
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        fileNameButton.setEnabled(enabled);
        uploadButton.setEnabled(enabled);
        clearButton.setEnabled(enabled);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;

        updateButtonsVisibility();
        updateComponentWidth();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setRequired(boolean required) {
        this.required = required;

        updateButtonsVisibility();
        updateComponentWidth();
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public void focus() {
        uploadButton.focus();
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

    protected void updateButtonsVisibility() {
        uploadButton.setVisible(editable);

        if (editable && !isRequired() && showClearButton) {
            clearButton.setVisible(true);
        } else {
            clearButton.setVisible(false);
        }
    }

    /*
    * File name button
    * */

    public void setFileName(String fileName) {
        this.fileName = fileName;

        if (StringUtils.isNotEmpty(fileName)) {
            fileNameButton.setCaption(fileName);
        } else {
            fileNameButton.setCaption(messages.getMainMessage("FileUploadField.fileNotSelected"));
        }
    }

    public boolean isShowFileName() {
        return showFileName;
    }

    public void setShowFileName(boolean showFileName) {
        this.showFileName = showFileName;

        fileNameButton.setVisible(showFileName);
        updateComponentWidth();
    }

    public Runnable getFileNameButtonClickListener() {
        return fileNameButtonClickListener;
    }

    public void setFileNameButtonClickListener(Runnable fileNameButtonClickListener) {
        this.fileNameButtonClickListener = fileNameButtonClickListener;
    }

    /*
    * Upload button
    * */

    public void setUploadButtonDescription(String description) {
        uploadButton.setDescription(description);
    }

    public String getUploadButtonDescription() {
        return uploadButton.getDescription();
    }

    public void setUploadButtonCaption(String caption) {
        uploadButton.setCaption(caption);
    }

    public String getUploadButtonCaption() {
        return uploadButton.getCaption();
    }

    public void setUploadButtonIcon(String icon) {
        uploadButton.setIcon(icon);
    }

    public String getUploadButtonIcon() {
        return uploadButton.getIcon();
    }

    /*
    * Clear button
    * */

    public boolean isShowClearButton() {
        return showClearButton;
    }

    public void setShowClearButton(boolean showClearButton) {
        this.showClearButton = showClearButton;

        updateButtonsVisibility();
        updateComponentWidth();
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

    public void setClearButtonDescription(String description) {
        clearButton.setDescription(description);
    }

    public String getClearButtonDescription() {
        return clearButton.getDescription();
    }

    public Runnable getClearButtonListener() {
        return clearButtonListener;
    }

    public void setClearButtonListener(Runnable clearButtonListener) {
        this.clearButtonListener = clearButtonListener;
    }
}