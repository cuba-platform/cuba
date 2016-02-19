/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.compatibility.ResizeListenerWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaResizableTextAreaWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaTextArea;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.ui.Component;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author subbotin
 */
public class WebResizableTextArea extends WebAbstractTextArea<CubaTextArea> implements ResizableTextArea {

    protected List<ResizeListener> resizeListeners;

    protected CubaResizableTextAreaWrapper wrapper;
    protected boolean settingsEnabled = true;

    public WebResizableTextArea() {
        wrapper = new CubaResizableTextAreaWrapper(component);
        wrapper.addResizeListener((oldWidth, oldHeight, width, height) -> {
            if (resizeListeners != null && !resizeListeners.isEmpty()) {
                ResizeEvent e = new ResizeEvent(this, oldWidth, width, oldHeight, height);

                for (ResizeListener resizeListener : resizeListeners) {
                    resizeListener.sizeChanged(e);
                }
            }
        });

        component.addValueChangeListener(event -> wrapper.markAsDirty());
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
    public void addResizeListener(com.haulmont.cuba.gui.components.ResizeListener resizeListener) {
        addResizeListener(new ResizeListenerWrapper(resizeListener));
    }

    @Override
    public void removeResizeListener(com.haulmont.cuba.gui.components.ResizeListener resizeListener) {
        removeResizeListener(new ResizeListenerWrapper(resizeListener));
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
    public boolean isEditable() {
        return wrapper.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        wrapper.setEditable(editable);
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
        if (resizeListeners == null) {
            resizeListeners = new ArrayList<>();
        }
        if (!resizeListeners.contains(resizeListener)) {
            resizeListeners.add(resizeListener);
        }
    }

    @Override
    public void removeResizeListener(ResizeListener resizeListener) {
        if (resizeListeners != null) {
            resizeListeners.remove(resizeListener);
        }
    }

    @Override
    public void setCursorPosition(int position) {
        component.setCursorPosition(position);
    }

    @Override
    public void applySettings(Element element) {
        if (isSettingsEnabled() && isResizable()) {
            String width = element.attributeValue("width");
            String height = element.attributeValue("height");
            if (StringUtils.isNotEmpty(width) && StringUtils.isNotEmpty(height)) {
                setWidth(width);
                setHeight(height);
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        if (!isSettingsEnabled() || !isResizable()) {
            return false;
        }

        String width = String.valueOf(getWidth()) + wrapper.getWidthUnits().toString();
        String height = String.valueOf(getHeight()) + wrapper.getHeightUnits().toString();
        element.addAttribute("width", width);
        element.addAttribute("height", height);

        return true;
    }

    @Override
    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
    }

    @Override
    public String getInputPrompt() {
        return component.getInputPrompt();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        component.setInputPrompt(inputPrompt);
    }
}