/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FocusWidget;
import com.vaadin.client.ui.Icon;

/**
 * todo artamonov support keyboard Enter/Space press
 *
 * todo artamonov compress js files
 *
 * @author artamonov
 * @version $Id$
 */
public class CubaFileUploadWidget extends FocusWidget {

    public static final String DEFAULT_CLASSNAME = "cuba-fileupload";

    protected Element rootElement;
    protected Element inputElement;
    protected Element captionElement;
    protected Element buttonWrap;
    protected Icon icon;

    protected int tabIndex = 0;
    protected boolean enabled = true;

    protected JQueryFileUploadOverlay fileUpload;

    protected String unableToUploadFileMessage;
    protected String progressWindowCaption;
    protected String cancelButtonCaption;
    private CubaFileUploadProgressWindow progressWindow;

    public CubaFileUploadWidget() {
        Document doc = Document.get();

        rootElement = doc.createDivElement();

        buttonWrap = doc.createSpanElement();

        captionElement = doc.createSpanElement();
        buttonWrap.appendChild(captionElement);

        rootElement.appendChild(buttonWrap);

        inputElement = doc.createFileInputElement();
        inputElement.setAttribute("name", "files[]");
        Style inputStyle = inputElement.getStyle();
        inputStyle.setPosition(Style.Position.ABSOLUTE);
        inputStyle.setTop(0, Unit.PX);
        inputStyle.setRight(0, Unit.PX);
        inputStyle.setFontSize(200, Unit.PX);
        inputStyle.setMargin(0, Unit.PX);
        inputStyle.setOpacity(0);

        rootElement.appendChild(inputElement);

        setElement(rootElement);

        setStyleName(DEFAULT_CLASSNAME);

        fileUpload = new JQueryFileUploadOverlay(inputElement) {
            @Override
            protected void queueUploadStart() {
                progressWindow = new CubaFileUploadProgressWindow();
                progressWindow.setOwner(CubaFileUploadWidget.this);
                progressWindow.addStyleName(getStylePrimaryName() + "-progresswindow");

                progressWindow.setVaadinModality(true);
                progressWindow.setDraggable(true);
                progressWindow.setResizable(false);
                progressWindow.setClosable(true);

                progressWindow.setCaption(progressWindowCaption);
                progressWindow.setCancelButtonCaption(cancelButtonCaption);

                progressWindow.closeListener = new CubaFileUploadProgressWindow.CloseListener() {
                    @Override
                    public void onClose() {
                        // todo artamonov cancel uploading
                        progressWindow = null;
                    }
                };

                progressWindow.setVisible(false);
                progressWindow.show();
                progressWindow.center();
                progressWindow.setVisible(true);
            }

            @Override
            protected void fileUploadStart(String fileName) {
                if (progressWindow != null) {
                    progressWindow.setCurrentFileName(fileName);
                }
            }

            @Override
            protected void uploadProgress(double loaded, double total) {
                if (progressWindow != null) {
                    float ratio = (float) (loaded / total);
                    progressWindow.setProgress(ratio);
                }
            }

            @Override
            protected void queueUploadStop() {
                if (progressWindow != null) {
                    progressWindow.hide();
                    progressWindow = null;
                }
            }
        };
    }

    public void setMultiSelect(boolean multiple) {
        if (multiple) {
            inputElement.setAttribute("multiple", "");
        } else {
            inputElement.removeAttribute("multiple");
        }
    }

    public void setUploadUrl(String uploadUrl) {
        fileUpload.setUploadUrl(uploadUrl);
    }

    @Override
    public final void setEnabled(boolean enabled) {
        if (isEnabled() != enabled) {
            this.enabled = enabled;
            if (!enabled) {
                Roles.getButtonRole().setAriaDisabledState(getElement(), true);
                super.setTabIndex(-1);
            } else {
                Roles.getButtonRole().removeAriaDisabledState(getElement());
                super.setTabIndex(tabIndex);
            }
        }
    }

    @Override
    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void setTabIndex(int index) {
        if (isEnabled()) {
            super.setTabIndex(index);
        }
        tabIndex = index;
    }

    @Override
    public int getTabIndex() {
        return tabIndex;
    }

    @Override
    protected void onAttach() {
        int tabIndex = this.tabIndex;

        super.onAttach();

        // Small hack to restore tabIndex after attach
        setTabIndex(tabIndex);
    }

    @Override
    public void setStyleName(String style) {
        super.setStyleName(style);

        buttonWrap.setClassName(getStylePrimaryName() + "-wrap");
        captionElement.setClassName(getStylePrimaryName() + "-caption");
    }

    @Override
    public void setStylePrimaryName(String style) {
        super.setStylePrimaryName(style);

        buttonWrap.setClassName(getStylePrimaryName() + "-wrap");
        captionElement.setClassName(getStylePrimaryName() + "-caption");
    }
}