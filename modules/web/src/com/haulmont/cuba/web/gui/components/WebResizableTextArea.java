/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.ResizeListener;
import com.haulmont.cuba.web.toolkit.ui.CubaResizableTextAreaWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaTextArea;
import com.vaadin.data.Property;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.ui.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author subbotin
 * @version $Id$
 */
public class WebResizableTextArea
        extends
            WebAbstractTextArea<CubaTextArea>
        implements
            ResizableTextArea {

    protected List<ResizeListener> resizeListeners = new ArrayList<>();

    protected CubaResizableTextAreaWrapper wrapper;

    public WebResizableTextArea() {
        wrapper = new CubaResizableTextAreaWrapper(component);
        wrapper.addResizeListener(new CubaResizableTextAreaWrapper.ResizeListener() {
            @Override
            public void onResize(String oldWidth, String oldHeight, String width, String height) {
                for (ResizeListener listener : resizeListeners) {
                    listener.onResize(WebResizableTextArea.this, oldWidth, oldHeight, width, height);
                }
            }
        });

        component.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                wrapper.markAsDirty();
            }
        });
    }

    @Override
    protected CubaTextArea createTextFieldImpl() {
        return new CubaTextArea() {
            @Override
            public void setComponentError(ErrorMessage componentError) {
                if (componentError instanceof UserError) {
                    super.setComponentError(componentError);
                } else {
                    wrapper.setComponentError(componentError);
                }
            }
        };
    }

    @Override
    public Component getComposition() {
        return wrapper;
    }

    @Override
    public boolean isResizable() {
        return wrapper.isResizable();
    }

    @Override
    public void setResizable(boolean resizable) {
        wrapper.setResizable(resizable);
    }

    @Override
    public void setHeight(String height) {
        wrapper.setHeight(height);
    }

    @Override
    public void setWidth(String width) {
        wrapper.setWidth(width);
    }

    @Override
    public void setCaption(String caption) {
        wrapper.setCaption(caption);
    }

    @Override
    public String getCaption() {
        return wrapper.getCaption();
    }

    @Override
    public String getDescription() {
        return wrapper.getDescription();
    }

    @Override
    public void setDescription(String description) {
        wrapper.setDescription(description);
    }

    @Override
    public boolean isRequired() {
        return wrapper.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        wrapper.setRequired(required);
    }

    @Override
    public void setRequiredMessage(String msg) {
        wrapper.setRequiredError(msg);
    }

    @Override
    public String getRequiredMessage() {
        return wrapper.getRequiredError();
    }

    @Override
    public void addResizeListener(ResizeListener resizeListener) {
        if (!resizeListeners.contains(resizeListener)) {
            resizeListeners.add(resizeListener);
        }
    }

    @Override
    public void removeResizeListener(ResizeListener resizeListener) {
        resizeListeners.remove(resizeListener);
    }

    @Override
    public void setCursorPosition(int position) {
        component.setCursorPosition(position);
    }
}