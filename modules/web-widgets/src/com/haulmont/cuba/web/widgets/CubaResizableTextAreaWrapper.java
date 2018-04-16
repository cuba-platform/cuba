/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.resizabletextarea.CubaResizableTextAreaWrapperServerRpc;
import com.haulmont.cuba.web.widgets.client.resizabletextarea.CubaResizableTextAreaWrapperState;
import com.haulmont.cuba.web.widgets.client.resizabletextarea.ResizeDirection;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
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

        setValidationVisible(false);
        setShowBufferedSourceException(false);
        setShowErrorForDisabledState(false);
        setFocusDelegate(textArea);

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
                    new com.vaadin.v7.data.Validator.EmptyValueException(getRequiredError()));
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

    /**
     * @deprecated Use {@link CubaResizableTextAreaWrapper#getResizableDirection()} instead
     */
    @Deprecated
    public boolean isResizable() {
        return getState(false).resizableDirection != ResizeDirection.NONE;
    }

    /**
     * @deprecated Use {@link CubaResizableTextAreaWrapper#setResizableDirection(ResizeDirection)} instead
     */
    @Deprecated
    public void setResizable(boolean resizable) {
        ResizeDirection value = resizable ? ResizeDirection.BOTH : ResizeDirection.NONE;
        setResizableDirection(value);
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
        textArea.setRequiredIndicatorVisible(required);
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        if (textArea != null) {
            if (width < 0) {
                textArea.setWidthUndefined();
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
                textArea.setHeightUndefined();
            } else {
                textArea.setHeight("100%");
            }
        }
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        if (getState(false).resizableDirection.equals(ResizeDirection.BOTH)
                && isPercentageSize()) {
            LoggerFactory.getLogger(CubaResizableTextAreaWrapper.class).warn(
                    "TextArea with percentage size can not be resizable");
            getState().resizableDirection = ResizeDirection.NONE;
        } else if (getState(false).resizableDirection.equals(ResizeDirection.VERTICAL)
                && Unit.PERCENTAGE.equals(getHeightUnits())) {
            LoggerFactory.getLogger(CubaResizableTextAreaWrapper.class).warn(
                    "TextArea height with percentage size can not be resizable to vertical direction");
            getState().resizableDirection = ResizeDirection.NONE;
        } else if (getState(false).resizableDirection.equals(ResizeDirection.HORIZONTAL)
                && (Unit.PERCENTAGE.equals(getWidthUnits()))) {
            LoggerFactory.getLogger(CubaResizableTextAreaWrapper.class).warn(
                    "TextArea width with percentage size can not be resizable to horizontal direction");
            getState().resizableDirection = ResizeDirection.NONE;
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

    public void setResizableDirection(ResizeDirection direction) {
        getState().resizableDirection = direction;
    }

    public ResizeDirection getResizableDirection() {
        return getState(false).resizableDirection;
    }
}