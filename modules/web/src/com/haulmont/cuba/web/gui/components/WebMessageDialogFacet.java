/*
 * Copyright (c) 2008-2019 Haulmont.
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

import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.web.gui.WebAbstractFacet;

import javax.annotation.Nullable;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class WebMessageDialogFacet extends WebAbstractFacet
        implements MessageDialogFacet {

    protected String caption;
    protected String message;

    protected SizeWithUnit width;
    protected SizeWithUnit height;

    protected boolean maximized;
    protected boolean modal;

    protected String styleName;

    protected Dialogs.MessageType type = Dialogs.MessageType.CONFIRMATION;
    protected ContentMode contentMode = ContentMode.TEXT;

    protected String actionId;
    protected String buttonId;

    protected boolean closeOnClickOutside;

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setType(Dialogs.MessageType type) {
        this.type = type;
    }

    @Override
    public Dialogs.MessageType getType() {
        return type;
    }

    @Override
    public void setContentMode(ContentMode contentMode) {
        this.contentMode = contentMode;
    }

    @Override
    public ContentMode getContentMode() {
        return contentMode;
    }

    @Override
    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    @Override
    public boolean isMaximized() {
        return maximized;
    }

    @Override
    public void setModal(boolean modal) {
        this.modal = modal;
    }

    @Override
    public boolean isModal() {
        return modal;
    }

    @Override
    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    @Override
    public String getStyleName() {
        return styleName;
    }

    @Override
    public void setWidth(String width) {
        this.width = SizeWithUnit.parseStringSize(width);
    }

    @Override
    public float getWidth() {
        return width != null
                ? width.getSize()
                : -1;
    }

    @Override
    public SizeUnit getWidthSizeUnit() {
        return width != null
                ? width.getUnit()
                : SizeUnit.PIXELS;
    }

    @Override
    public void setHeight(String height) {
        this.height = SizeWithUnit.parseStringSize(height);
    }

    @Override
    public float getHeight() {
        return height != null
                ? height.getSize()
                : -1;
    }

    @Override
    public SizeUnit getHeightSizeUnit() {
        return height != null
                ? height.getUnit()
                : SizeUnit.PIXELS;
    }

    @Override
    public String getActionTarget() {
        return actionId;
    }

    @Override
    public void setActionTarget(String actionId) {
        this.actionId = actionId;
    }

    @Override
    public String getButtonTarget() {
        return buttonId;
    }

    @Override
    public void setButtonTarget(String buttonId) {
        this.buttonId = buttonId;
    }

    @Override
    public void setCloseOnClickOutside(boolean closeOnClickOutside) {
        this.closeOnClickOutside = closeOnClickOutside;
    }

    @Override
    public boolean isCloseOnClickOutside() {
        return closeOnClickOutside;
    }

    @Override
    public void setOwner(@Nullable Frame owner) {
        super.setOwner(owner);

        subscribe();
    }

    @Override
    public void show() {
        Frame owner = getOwner();
        if (owner == null) {
            throw new IllegalStateException("MessageDialog is not attached to Frame");
        }

        Dialogs.MessageDialogBuilder builder = UiControllerUtils
                .getScreenContext(owner.getFrameOwner())
                .getDialogs()
                .createMessageDialog(type);

        if (width != null) {
            builder.withWidth(width.stringValue());
        }
        if (height != null) {
            builder.withHeight(height.stringValue());
        }

        builder.withCaption(caption)
                .withMessage(message)
                .withContentMode(contentMode)
                .withMaximized(maximized)
                .withModal(modal)
                .withStyleName(styleName)
                .withCloseOnClickOutside(closeOnClickOutside)
                .show();
    }

    protected void subscribe() {
        Frame owner = getOwner();
        if (owner == null) {
            throw new IllegalStateException("Notification is not attached to Frame");
        }

        if (isNotEmpty(actionId)
                && isNotEmpty(buttonId)) {
            throw new GuiDevelopmentException(
                    "Notification facet should have either action or button target",
                    owner.getId());
        }

        if (isNotEmpty(actionId)) {
            subscribeOnAction(owner);
        } else if (isNotEmpty(buttonId)) {
            subscribeOnButton(owner);
        }
    }

    protected void subscribeOnAction(Frame owner) {
        Action action = WebComponentsHelper.findAction(owner, actionId);

        if (!(action instanceof BaseAction)) {
            throw new GuiDevelopmentException(
                    String.format("Unable to find Dialog target button with id '%s'", actionId),
                    owner.getId());
        }

        ((BaseAction) action).addActionPerformedListener(e ->
                show());
    }

    protected void subscribeOnButton(Frame owner) {
        Component component = owner.getComponent(buttonId);

        if (!(component instanceof Button)) {
            throw new GuiDevelopmentException(
                    String.format("Unable to find Dialog target button with id '%s'", buttonId),
                    owner.getId());
        }

        ((Button) component).addClickListener(e ->
                show());
    }
}
