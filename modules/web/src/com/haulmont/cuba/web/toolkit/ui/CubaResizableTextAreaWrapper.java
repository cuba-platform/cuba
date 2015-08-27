/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea.CubaResizableTextAreaWrapperServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea.CubaResizableTextAreaWrapperState;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gorelov
 * @version $Id$
 */
public class CubaResizableTextAreaWrapper extends CustomField {

    protected List<ResizeListener> listeners = new ArrayList<>();

    public interface ResizeListener {
        void onResize(String oldWidth, String oldHeight, String width, String height);
    }

    protected final CubaTextArea textArea;

    public CubaResizableTextAreaWrapper(CubaTextArea textArea) {
        this.textArea = textArea;

        setWidthUndefined();
        setPrimaryStyleName(getState().primaryStyleName);

        setValidationVisible(false);
        setShowBufferedSourceException(false);

        CubaResizableTextAreaWrapperServerRpc rpc = new CubaResizableTextAreaWrapperServerRpc() {
            @Override
            public void sizeChanged(String width, String height) {
                setWidth(width);
                setHeight(height);

                for (ResizeListener listener : listeners) {
                    listener.onResize(null, null, width, height);
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
            LogFactory.getLog(getClass()).warn(
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
