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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.haulmont.cuba.web.widgets.UploadComponent;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;

public class CubaFileUploadWrapper extends CustomField<FileDescriptor> {
    protected static final String FILE_UPLOAD_WRAPPER_STYLENAME = "c-fileupload-wrapper";
    protected static final String EMPTY_VALUE_STYLENAME = "c-fileupload-empty";

    protected CssLayout container;
    protected Button fileNameButton;
    protected Button clearButton;
    protected UploadComponent uploadButton;

    protected boolean showFileName = false;
    protected boolean showClearButton = false;

    protected String fileName;
    protected String fileNotSelectedMessage = "";

    protected FileDescriptor value;

    @Override
    protected Component initContent() {
        return container;
    }

    public String getFileNotSelectedMessage() {
        return fileNotSelectedMessage;
    }

    public void setFileNotSelectedMessage(String fileNotSelectedMessage) {
        this.fileNotSelectedMessage = fileNotSelectedMessage;
    }

    @Override
    protected void doSetValue(FileDescriptor fileDescriptor) {
        this.value = fileDescriptor;

        setFileNameButtonCaption(fileDescriptor == null ? null : fileDescriptor.getName());

        onSetInternalValue(fileDescriptor);
    }

    @Override
    public FileDescriptor getValue() {
        return value;
    }

    protected void onSetInternalValue(Object newValue) {
    }

    private void updateComponentWidth() {
        if (container == null)
            return;

        if (getWidth() >= 0) {
            container.setWidth(100, Unit.PERCENTAGE);
            if (isShowFileName()) {
                fileNameButton.setWidth(100, Unit.PERCENTAGE);
                uploadButton.setWidthUndefined();
                clearButton.setWidthUndefined();
            } else {
                fileNameButton.setWidthUndefined();
                if (isShowClearButton() && !isRequiredIndicatorVisible()) {
                    uploadButton.setWidth(100, Unit.PERCENTAGE);
                    clearButton.setWidth(100, Unit.PERCENTAGE);
                } else {
                    uploadButton.setWidth(100, Unit.PERCENTAGE);
                }
            }
        } else {
            container.setWidthUndefined();
            fileNameButton.setWidthUndefined();
            uploadButton.setWidthUndefined();
            clearButton.setWidthUndefined();
        }
    }

    private void updateComponentHeight() {
        if (container == null)
            return;

        if (getHeight() >= 0) {
            container.setHeight(100, Unit.PERCENTAGE);
            fileNameButton.setHeight(100, Unit.PERCENTAGE);
            uploadButton.setHeight(100, Unit.PERCENTAGE);
            clearButton.setHeight(100, Unit.PERCENTAGE);
        } else {
            container.setHeightUndefined();
            fileNameButton.setHeightUndefined();
            uploadButton.setHeightUndefined();
            clearButton.setHeightUndefined();
        }
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        updateComponentWidth();
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);

        updateComponentHeight();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        updateButtonsVisibility();
        updateComponentWidth();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean visible) {
        super.setRequiredIndicatorVisible(visible);

        updateButtonsVisibility();
        updateComponentWidth();
    }

    @Override
    public void focus() {
        super.focus();
        if (uploadButton != null) {
            uploadButton.focus();
        }
    }

    protected void updateButtonsVisibility() {
        uploadButton.setVisible(!isReadOnly());

        if (!isReadOnly() && !isRequiredIndicatorVisible() && showClearButton) {
            clearButton.setVisible(true);
        } else {
            clearButton.setVisible(false);
        }
    }

    public CubaFileUploadWrapper(UploadComponent uploadButton) {
        setPrimaryStyleName(FILE_UPLOAD_WRAPPER_STYLENAME);
        initLayout(uploadButton);
    }

    protected void initLayout(UploadComponent uploadComponent) {
        this.uploadButton = uploadComponent;

        container = new CssLayout();
        container.addStyleName("c-fileupload-container");

        fileNameButton = new CubaButton();
        fileNameButton.setWidth(100, Unit.PERCENTAGE);
        fileNameButton.addStyleName(ValoTheme.BUTTON_LINK);
        fileNameButton.addStyleName("c-fileupload-filename");
        setFileNameButtonCaption(null);
        container.addComponent(fileNameButton);

        container.addComponent(uploadComponent);

        clearButton = new CubaButton("");
        clearButton.setStyleName("c-fileupload-clear");
        container.addComponent(clearButton);
        setShowClearButton(showClearButton);

        setShowFileName(false);
        setWidthUndefined();
    }

    public boolean isShowFileName() {
        return showFileName;
    }

    public void setShowFileName(boolean showFileName) {
        this.showFileName = showFileName;
        fileNameButton.setVisible(showFileName);

        updateComponentWidth();
    }

    public void setFileNameButtonCaption(String title) {
        this.fileName = title;

        if (StringUtils.isNotEmpty(title)) {
            fileNameButton.setCaption(title);
            fileNameButton.removeStyleName(EMPTY_VALUE_STYLENAME);
        } else {
            fileNameButton.setCaption(fileNotSelectedMessage);
            fileNameButton.addStyleName(EMPTY_VALUE_STYLENAME);
        }
    }

    public void addFileNameClickListener(Button.ClickListener clickListener) {
        fileNameButton.addClickListener(clickListener);
    }

    public void removeFileNameClickListener(Button.ClickListener clickListener) {
        fileNameButton.removeClickListener(clickListener);
    }

    public void setFileNameButtonEnabled(boolean enabled) {
        fileNameButton.setEnabled(enabled);
    }

    public void setClearButtonEnabled(boolean enabled) {
        clearButton.setEnabled(enabled);
    }

    public void setUploadButtonEnabled(boolean enabled) {
        uploadButton.setEnabled(enabled);
    }

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

    public void setUploadButtonIcon(Resource icon) {
        uploadButton.setIcon(icon);
    }

    public String getUploadButtonIcon() {
        return uploadButton.getIcon().toString();
    }

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

    public void setClearButtonIcon(Resource icon) {
        clearButton.setIcon(icon);
    }

    public String getClearButtonIcon() {
        return clearButton.getIcon().toString();
    }

    public void setClearButtonListener(Button.ClickListener listener) {
        clearButton.addClickListener(listener);
    }

    public void removeClearButtonAction(Button.ClickListener listener) {
        clearButton.removeClickListener(listener);
    }

    public void setClearButtonDescription(String description) {
        clearButton.setDescription(description);
    }

    public String getClearButtonDescription() {
        return clearButton.getDescription();
    }

    @Override
    public int getTabIndex() {
        return uploadButton.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        uploadButton.setTabIndex(tabIndex);
        clearButton.setTabIndex(tabIndex);
    }
}