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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.inputdialog.InputDialogAction;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.web.gui.WebAbstractFacet;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class WebInputDialogFacet extends WebAbstractFacet implements InputDialogFacet {

    protected String caption;

    protected SizeWithUnit width;
    protected SizeWithUnit height;

    protected InputParameter[] parameters;

    protected String actionId;
    protected String buttonId;

    protected DialogActions dialogActions;
    protected Consumer<InputDialog.InputDialogResult> dialogResultHandler;

    protected Collection<DialogAction<InputDialogFacet>> actions;

    protected Function<InputDialog.ValidationContext, ValidationErrors> validator;

    protected InputDialog inputDialog;

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setWidth(String width) {
        this.width = SizeWithUnit.parseStringSize(width);
    }

    @Override
    public float getWidth() {
        return width.getSize();
    }

    @Override
    public SizeUnit getWidthSizeUnit() {
        return width.getUnit();
    }

    @Override
    public void setHeight(String height) {
        this.height = SizeWithUnit.parseStringSize(height);
    }

    @Override
    public float getHeight() {
        return height.getSize();
    }

    @Override
    public SizeUnit getHeightSizeUnit() {
        return height.getUnit();
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
    public void setDialogActions(DialogActions dialogActions) {
        this.dialogActions = dialogActions;
    }

    @Override
    public DialogActions getDialogActions() {
        return dialogActions;
    }

    @Override
    public void setParameters(InputParameter... parameters) {
        this.parameters = parameters;
    }

    @Override
    public void setActions(Collection<DialogAction<InputDialogFacet>> actions) {
        this.actions = actions;
    }

    @Override
    public Collection<DialogAction<InputDialogFacet>> getActions() {
        return actions;
    }

    @Override
    public Subscription addCloseListener(Consumer<CloseEvent> closeListener) {
        return getEventHub().subscribe(CloseEvent.class, closeListener);
    }

    @Override
    public void setDialogResultHandler(Consumer<InputDialog.InputDialogResult> dialogResultHandler) {
        this.dialogResultHandler = dialogResultHandler;
    }

    @Override
    public void setValidator(Function<InputDialog.ValidationContext, ValidationErrors> validator) {
        this.validator = validator;
    }

    @Nullable
    @Override
    public Object getSubPart(String name) {
        if (actions == null) {
            return null;
        }
        return actions.stream()
                .filter(action -> Objects.equals(action.getId(), name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public InputDialog create() {
        Frame owner = getOwner();
        if (owner == null) {
            throw new IllegalStateException("MessageDialog is not attached to Frame");
        }

        Dialogs.InputDialogBuilder builder = UiControllerUtils.getScreenContext(owner.getFrameOwner())
                .getDialogs()
                .createInputDialog(owner.getFrameOwner());

        if (width != null) {
            builder.withWidth(width.stringValue());
        }
        if (height != null) {
            builder.withHeight(height.stringValue());
        }

        builder.withCaption(caption)
                .withParameters(parameters)
                .withValidator(validator)
                .withCloseListener(this::fireCloseActionEvent);

        if (dialogActions == null
                && CollectionUtils.isEmpty(actions)) {
            builder.withActions(DialogActions.OK_CANCEL, dialogResultHandler);
        } else {
            if (dialogActions != null) {
                builder.withActions(dialogActions, dialogResultHandler);
            } else if (CollectionUtils.isNotEmpty(actions)) {
                builder.withActions(createActions(actions));
            }
        }

        inputDialog = builder.build();

        return inputDialog;
    }

    @Override
    public InputDialog show() {
        return (InputDialog) create().show();
    }

    @Override
    public void setOwner(@Nullable Frame owner) {
        super.setOwner(owner);

        subscribe();
    }

    protected void fireCloseActionEvent(InputDialog.InputDialogCloseEvent event) {
        CloseEvent closeEvent = new CloseEvent(this,
                event.getCloseAction(), event.getValues());

        publish(CloseEvent.class, closeEvent);
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

    protected InputDialogAction[] createActions(Collection<DialogAction<InputDialogFacet>> actions) {
        if (actions == null) {
            return new InputDialogAction[]{};
        }
        return actions.stream()
                .map(this::createAction)
                .collect(Collectors.toList())
                .toArray(new InputDialogAction[]{});
    }

    protected InputDialogAction createAction(DialogAction<InputDialogFacet> action) {
        return new InputDialogAction(action.getId())
                .withCaption(action.getCaption())
                .withDescription(action.getDescription())
                .withIcon(action.getIcon())
                .withPrimary(action.isPrimary())
                .withHandler(inputDialogActionPerformed -> {
                    if (action.getActionHandler() != null) {
                        action.getActionHandler().accept(
                                new DialogActionPerformedEvent<>(this, action));
                    }
                });
    }
}
