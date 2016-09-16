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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.web.toolkit.ui.UploadComponent;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;

import static com.vaadin.ui.themes.BaseTheme.BUTTON_LINK;

public class CubaFileUploadWrapper extends CustomField {

    protected static final String FILE_UPLOAD_WRAPPER = "cuba-fileupload-wrapper";
    protected static final String EMPTY_VALUE_STYLE = "cuba-fileupload-empty";
    protected static final String ERROR_STYLE = "error";

    protected Messages messages = AppBeans.get(Messages.NAME);

    protected HorizontalLayout container;
    protected Button fileNameButton;
    protected Button clearButton;
    protected UploadComponent uploadButton;

    protected boolean showFileName = false;
    protected boolean showClearButton = false;

    protected String fileName;

    public CubaFileUploadWrapper(UploadComponent uploadButton) {
        setPrimaryStyleName(FILE_UPLOAD_WRAPPER);
        initLayout(uploadButton);
    }

    private void initLayout(UploadComponent uploadComponent) {
        this.uploadButton = uploadComponent;

        container = new HorizontalLayout();
        container.setSpacing(true);
        container.addStyleName("fileupload-wrapper-container");

        fileNameButton = new Button();
        fileNameButton.setWidth("100%");
        fileNameButton.addStyleName(BUTTON_LINK);
        setFileNameButtonCaption(null);
        container.addComponent(fileNameButton);
        container.setComponentAlignment(fileNameButton, Alignment.MIDDLE_LEFT);

        container.addComponent(uploadComponent);

        clearButton = new Button(messages.getMainMessage("FileUploadField.clearButtonCaption"));
        container.addComponent(clearButton);
        setShowClearButton(showClearButton);

        setShowFileName(false);
        setWidthUndefined();
    }

    @Override
    protected Component initContent() {
        return container;
    }

    @Override
    public Class getType() {
        return FileDescriptor.class;
    }

    @Override
    protected void setInternalValue(Object newValue) {
        //noinspection unchecked
        super.setInternalValue(newValue);

        if (newValue != null) {
            FileDescriptor fileDescriptor = (FileDescriptor) newValue;
            setFileNameButtonCaption(fileDescriptor.getName());
        } else {
            setFileNameButtonCaption(null);
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

    public void setFileNameButtonCaption(String title) {
        fileName = title;

        if (StringUtils.isNotEmpty(title)) {
            fileNameButton.setCaption(title);
            fileNameButton.removeStyleName(EMPTY_VALUE_STYLE);

            if (isRequired()) {
                fileNameButton.removeStyleName(ERROR_STYLE);
            }
        } else {
            fileNameButton.setCaption(messages.getMainMessage("FileUploadField.fileNotSelected"));
            fileNameButton.addStyleName(EMPTY_VALUE_STYLE);

            if (isRequired()) {
                fileNameButton.addStyleName(ERROR_STYLE);
            } else {
                fileNameButton.removeStyleName(ERROR_STYLE);
            }
        }
    }

    public void addFileNameClickListener(Button.ClickListener clickListener) {
        fileNameButton.addClickListener(clickListener);
    }

    public void removeFileNameClickListener(Button.ClickListener clickListener) {
        fileNameButton.removeClickListener(clickListener);
    }

    private void updateComponentWidth() {
        if (container == null)
            return;

        if (getWidth() >= 0) {
            container.setWidth(100, Unit.PERCENTAGE);
            if (isShowFileName()) {
                fileNameButton.setWidth(100, Unit.PERCENTAGE);

                uploadButton.setWidthUndefined();
            } else {
                uploadButton.setWidth(100, Unit.PERCENTAGE);
            }
        } else {
            container.setWidthUndefined();
            fileNameButton.setWidthUndefined();
            uploadButton.setWidthUndefined();
        }
    }

    private void updateComponentHeight() {
        if (container == null)
            return;

        if (getHeight() >= 0) {
            container.setHeight(100, Unit.PERCENTAGE);
            fileNameButton.setHeight(100, Unit.PERCENTAGE);
            uploadButton.setHeight(100, Unit.PERCENTAGE);
        } else {
            container.setHeightUndefined();
            fileNameButton.setHeightUndefined();
            uploadButton.setHeightUndefined();
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
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);

        setFileNameButtonCaption(fileName);
        updateButtonsVisibility();
    }

    @Override
    public void focus() {
        super.focus();
        if (uploadButton instanceof Focusable) {
            ((Focusable) uploadButton).focus();
        }
    }

    protected void updateButtonsVisibility() {
        uploadButton.setVisible(!isReadOnly());

        if (!isReadOnly() && !isRequired() && showClearButton) {
            clearButton.setVisible(true);
        } else {
            clearButton.setVisible(false);
        }
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
    }

    public void setClearButtonCaption(String caption) {
        clearButton.setCaption(caption);
    }

    public String getClearButtonCaption() {
        return clearButton.getCaption();
    }

    public void setClearButtonIcon(String icon) {
        clearButton.setIcon(WebComponentsHelper.getIcon(icon));
    }

    public String getClearButtonIcon() {
        return clearButton.getIcon().toString();
    }

    public void setClearButtonAction(Button.ClickListener listener) {
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
        uploadButton.setIcon(WebComponentsHelper.getIcon(icon));
    }

    public String getUploadButtonIcon() {
        return uploadButton.getIcon().toString();
    }
}
