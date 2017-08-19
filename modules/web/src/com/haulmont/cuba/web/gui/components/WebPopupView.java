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

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.PopupView;
import com.vaadin.ui.Label;
import org.apache.commons.lang.StringUtils;

public class WebPopupView extends WebAbstractComponent<com.vaadin.ui.PopupView> implements PopupView {
    protected Component popupContent;
    protected String minimizedValue;

    public WebPopupView() {
        component = new com.vaadin.ui.PopupView(new EmptyContent());

        component.addPopupVisibilityListener(e ->
                getEventRouter().fireEvent(PopupVisibilityListener.class,
                        PopupVisibilityListener::popupVisibilityChange,
                        new PopupVisibilityEvent(this))
        );
    }

    @Override
    public void setPopupContent(Component popupContent) {
        if (this.popupContent != null) {
            if (this.popupContent instanceof BelongToFrame) {
                ((BelongToFrame) this.popupContent).setFrame(null);
            } else {
                detachFromFrame(this.popupContent);
            }
            this.popupContent.setParent(null);
        }

        this.popupContent = popupContent;

        if (popupContent != null) {
            component.setContent(new PopupContent());

            if (frame != null) {
                if (popupContent instanceof BelongToFrame
                        && ((BelongToFrame) popupContent).getFrame() == null) {
                    ((BelongToFrame) popupContent).setFrame(frame);
                } else {
                    attachToFrame(popupContent);
                }
            }
            popupContent.setParent(this);
        } else {
            component.setContent(new EmptyContent());
        }
    }

    protected void attachToFrame(Component childComponent) {
        frame.registerComponent(childComponent);
    }

    protected void detachFromFrame(Component childComponent) {
        frame.unregisterComponent(childComponent);
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);

        if (popupContent != null && frame != null) {
            if (popupContent instanceof BelongToFrame
                    && ((BelongToFrame) popupContent).getFrame() == null) {
                ((BelongToFrame) popupContent).setFrame(frame);
            } else {
                attachToFrame(popupContent);
            }
        }
    }

    @Override
    public Component getPopupContent() {
        return popupContent;
    }

    @Override
    public void setPopupVisible(boolean value) {
        component.setPopupVisible(value);
        component.markAsDirty();
    }

    @Override
    public void setHideOnMouseOut(boolean value) {
        component.setHideOnMouseOut(value);
        component.markAsDirty();
    }

    @Override
    public boolean isHideOnMouseOut() {
        return component.isHideOnMouseOut();
    }

    @Override
    public boolean isPopupVisible() {
        return component.isPopupVisible();
    }

    @Override
    public void setMinimizedValue(String minimizedValue) {
        this.minimizedValue = minimizedValue;
        component.markAsDirty();
    }

    @Override
    public String getMinimizedValue() {
        return minimizedValue;
    }

    @Override
    public void setCaptionAsHtml(boolean value) {
        component.setCaptionAsHtml(value);
    }

    @Override
    public boolean isCaptionAsHtml() {
        return component.isCaptionAsHtml();
    }

    @Override
    public void addPopupVisibilityListener(PopupVisibilityListener listener) {
        getEventRouter().addListener(PopupVisibilityListener.class, listener);
    }

    @Override
    public void removePopupVisibilityListener(PopupVisibilityListener listener) {
        getEventRouter().removeListener(PopupVisibilityListener.class, listener);
    }

    protected class EmptyContent implements com.vaadin.ui.PopupView.Content {
        private Label label = new Label("");

        @Override
        public String getMinimizedValueAsHTML() {
            return minimizedValue;
        }

        @Override
        public com.vaadin.ui.Component getPopupComponent() {
            return label;
        }
    }

    protected class PopupContent implements com.vaadin.ui.PopupView.Content {
        @Override
        public final com.vaadin.ui.Component getPopupComponent() {
            return WebComponentsHelper.getComposition(popupContent);
        }

        @Override
        public final String getMinimizedValueAsHTML() {
            return minimizedValue;
        }
    }

    @Override
    public void setCaption(String caption) {
        super.setCaption(caption);
        setIconStyle();
    }

    @Override
    public void setIcon(String icon) {
        super.setIcon(icon);
        setIconStyle();
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);
        setIconStyle();
    }

    protected void setIconStyle() {
        if (StringUtils.isNotEmpty(getIcon()) && StringUtils.isEmpty(getCaption())) {
            getComposition().addStyleName("popupview-icon-on-left");
        } else {
            getComposition().removeStyleName("popupview-icon-on-left");
        }
    }
}