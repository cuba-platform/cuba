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
 *
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea.CubaResizableTextAreaWrapperServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea.CubaResizableTextAreaWrapperState;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CubaResizableTextAreaWrapper extends CustomField {

    protected List<ResizeListener> listeners = new ArrayList<>();

    public interface ResizeListener {
        void onResize(String oldWidth, String oldHeight, String width, String height);
    }

    protected final CubaTextArea textArea;

    public CubaResizableTextAreaWrapper(CubaTextArea txtArea) {
        this.textArea = txtArea;

        setWidthUndefined();
        setPrimaryStyleName(getState().primaryStyleName);

        setValidationVisible(false);
        setShowBufferedSourceException(false);
        setShowErrorForDisabledState(false);

        CubaResizableTextAreaWrapperServerRpc rpc = new CubaResizableTextAreaWrapperServerRpc() {
            String oldWidth;
            String oldHeight;

            @Override
            public void sizeChanged(String width, String height) {
                if (StringUtils.isEmpty(oldWidth)) {
                    oldWidth = (int) getWidth() + getWidthUnits().getSymbol();
                }
                if (StringUtils.isEmpty(oldHeight)) {
                    oldHeight = ((int) getHeight()) + getHeightUnits().getSymbol();
                }

                setWidth(width);
                setHeight(height);

                for (ResizeListener listener : listeners) {
                    listener.onResize(oldWidth, oldHeight, width, height);
                }

                oldWidth = width;
                oldHeight = height;
            }

            @Override
            public void textChanged(String text) {
                if (!textArea.isReadOnly()) {
                    textArea.setValue(text);
                }
            }
        };
        registerRpc(rpc);
    }

    @Override
    public ErrorMessage getErrorMessage() {
        ErrorMessage superError = super.getErrorMessage();
        if (!textArea.isReadOnly() && isRequired() && textArea.isEmpty()) {
            ErrorMessage error = AbstractErrorMessage.getErrorMessageForException(
                    new com.vaadin.data.Validator.EmptyValueException(getRequiredError()));
            if (error != null) {
                return new CompositeErrorMessage(superError, error);
            }
        }

        return superError;
    }

    @Override
    protected Component initContent() {
        return textArea;
    }

    @Override
    public Class getType() {
        return Object.class;
    }

    public boolean isResizable() {
        return getState(false).resizable;
    }

    public void setResizable(boolean resizable) {
        getState().resizable = resizable;
    }

    public boolean isEditable() {
        return !super.isReadOnly();
    }

    public void setEditable(boolean editable) {
        super.setReadOnly(!editable);
        textArea.setReadOnly(!editable);
    }

    @Override
    protected CubaResizableTextAreaWrapperState getState() {
        return (CubaResizableTextAreaWrapperState) super.getState();
    }

    @Override
    protected CubaResizableTextAreaWrapperState getState(boolean markAsDirty) {
        return (CubaResizableTextAreaWrapperState) super.getState(markAsDirty);
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        textArea.setRequired(required);
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        if (textArea != null) {
            if (width < 0) {
                textArea.setWidth(com.haulmont.cuba.gui.components.Component.AUTO_SIZE);
            } else {
                textArea.setWidth("100%");
            }
        }
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);

        if (textArea != null) {
            if (height < 0) {
                textArea.setHeight(com.haulmont.cuba.gui.components.Component.AUTO_SIZE);
            } else {
                textArea.setHeight("100%");
            }
        }
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        if (getState(false).resizable
                && (textArea.getRows() > 0 && textArea.getColumns() > 0
                || isPercentageSize())) {
            LoggerFactory.getLogger(CubaResizableTextAreaWrapper.class).warn(
                    "TextArea with fixed rows and cols or percentage size can not be resizable");
            getState().resizable = false;
        }
    }

    protected boolean isPercentageSize() {
        return Unit.PERCENTAGE.equals(getHeightUnits()) || Unit.PERCENTAGE.equals(getWidthUnits());
    }

    public void addResizeListener(ResizeListener resizeListener) {
        if (!listeners.contains(resizeListener)) {
            listeners.add(resizeListener);
        }
    }

    public void removeResizeListener(ResizeListener resizeListener) {
        listeners.remove(resizeListener);
    }
}