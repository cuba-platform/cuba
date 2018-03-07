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
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.UploadComponent;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.*;
import org.apache.commons.lang.StringUtils;

import static com.vaadin.v7.ui.themes.BaseTheme.BUTTON_LINK;

public class CubaFileUploadWrapper extends CustomField {
    protected static final String FILE_UPLOAD_WRAPPER_STYLENAME = "c-fileupload-wrapper";
    protected static final String EMPTY_VALUE_STYLENAME = "c-fileupload-empty";

    protected HorizontalLayout container;
    protected Button fileNameButton;
    protected Button clearButton;
    protected UploadComponent uploadButton;

    protected boolean showFileName = false;
    protected boolean showClearButton = false;

    protected String fileName;

    public CubaFileUploadWrapper(UploadComponent uploadButton) {
        setPrimaryStyleName(FILE_UPLOAD_WRAPPER_STYLENAME);
        initLayout(uploadButton);

        setValidationVisible(false);
    }

    protected void initLayout(UploadComponent uploadComponent) {
        this.uploadButton = uploadComponent;

        container = new HorizontalLayout();
        container.setSpacing(true);
        container.addStyleName("c-fileupload-container");

        fileNameButton = new Button();
        fileNameButton.setWidth("100%");
        fileNameButton.addStyleName(BUTTON_LINK);
        fileNameButton.addStyleName("c-fileupload-filename");
        setFileNameButtonCaption(null);
        container.addComponent(fileNameButton);
        container.setComponentAlignment(fileNameButton, Alignment.MIDDLE_LEFT);

        container.addComponent(uploadComponent);

        Messages messages = AppBeans.get(Messages.NAME);
        clearButton = new Button(messages.getMainMessage("FileUploadField.clearButtonCaption"));
        clearButton.setStyleName("c-fileupload-clear");
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

        onSetInternalValue(newValue);
    }

    protected void onSetInternalValue(Object newValue) {
    }

    private void updateComponentWidth() {
        if (container == null)
            return;

        if (getWidth() >= 0) {
            container.setWidth(100, Unit.PERCENTAGE);
            if (isShowFileName()) {
                container.setExpandRatio(fileNameButton, 1);
                fileNameButton.setWidth(100, Unit.PERCENTAGE);
                uploadButton.setWidthUndefined();
                clearButton.setWidthUndefined();
            } else {
                container.setExpandRatio(fileNameButton, 0);
                fileNameButton.setWidthUndefined();
                if (isShowClearButton() && !isRequired()) {
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
    public void setRequired(boolean required) {
        super.setRequired(required);

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

        if (!isReadOnly() && !isRequired() && showClearButton) {
            clearButton.setVisible(true);
        } else {
            clearButton.setVisible(false);
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
        this.fileName = title;

        if (StringUtils.isNotEmpty(title)) {
            fileNameButton.setCaption(title);
            fileNameButton.removeStyleName(EMPTY_VALUE_STYLENAME);
        } else {
            Messages messages = AppBeans.get(Messages.NAME);
            fileNameButton.setCaption(messages.getMainMessage("FileUploadField.fileNotSelected"));
            fileNameButton.addStyleName(EMPTY_VALUE_STYLENAME);
        }
    }

    public void addFileNameClickListener(Button.ClickListener clickListener) {
        fileNameButton.addClickListener(clickListener);
    }

    public void removeFileNameClickListener(Button.ClickListener clickListener) {
        fileNameButton.removeClickListener(clickListener);
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

    public void setUploadButtonIcon(String icon) {
        uploadButton.setIcon(AppBeans.get(IconResolver.class).getIconResource(icon));
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

    public void setClearButtonIcon(String icon) {
        clearButton.setIcon(AppBeans.get(IconResolver.class).getIconResource(icon));
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
    public ErrorMessage getErrorMessage() {
        ErrorMessage superError = super.getErrorMessage();
        if (!isReadOnly() && isRequired() && isEmpty()) {
            ErrorMessage error = AbstractErrorMessage.getErrorMessageForException(
                    new com.vaadin.v7.data.Validator.EmptyValueException(getRequiredError()));
            if (error != null) {
                return new CompositeErrorMessage(superError, error);
            }
        }
        return superError;
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