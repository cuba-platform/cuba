/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.screen;

import com.google.common.collect.Iterables;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;

@Component(ScreenValidation.NAME)
public class ScreenValidation {

    public static final String NAME = "cuba_ScreenValidation";

    @Inject
    protected Configuration configuration;
    @Inject
    protected Messages messages;
    @Inject
    protected Icons icons;
    @Inject
    protected BeanValidation beanValidation;

    /**
     * Validates UI components by invoking their {@link Validatable#validate()}.
     *
     * @param components components collection
     * @return validation errors
     */
    public ValidationErrors validateUiComponents(Collection<com.haulmont.cuba.gui.components.Component> components) {
        ValidationErrors errors = new ValidationErrors();

        for (com.haulmont.cuba.gui.components.Component component : components) {
            if (component instanceof Validatable) {
                Validatable validatable = (Validatable) component;
                if (validatable.isValidateOnCommit()) {
                    validate(validatable, errors);
                }
            }
        }
        return errors;
    }

    /**
     * Validates UI components by invoking their {@link Validatable#validate()}.
     *
     * @param container components container
     * @return validation errors
     */
    public ValidationErrors validateUiComponents(ComponentContainer container) {
        ValidationErrors errors = new ValidationErrors();

        ComponentsHelper.traverseValidatable(container,
                v -> validate(v, errors)
        );
        return errors;
    }

    protected void validate(Validatable validatable, ValidationErrors errors) {
        try {
            validatable.validate();
        } catch (ValidationException e) {
            Logger log = LoggerFactory.getLogger(Screen.class);

            if (log.isTraceEnabled()) {
                log.trace("Validation failed", e);
            } else if (log.isDebugEnabled()) {
                log.debug("Validation failed: " + e);
            }

            ComponentsHelper.fillErrorMessages(validatable, e, errors);
        }
    }

    /**
     * Show validation alert with passed errors and first problem UI component.
     *
     * @param origin screen controller
     * @param errors validation error
     */
    public void showValidationErrors(FrameOwner origin, ValidationErrors errors) {
        checkNotNullArgument(origin);
        checkNotNullArgument(errors);

        if (errors.isEmpty()) {
            return;
        }

        StringBuilder buffer = new StringBuilder();
        for (ValidationErrors.Item error : errors.getAll()) {
            buffer.append(error.description).append("\n");
        }

        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

        String validationNotificationType = clientConfig.getValidationNotificationType();
        if (validationNotificationType.endsWith("_HTML")) {
            // HTML validation notification types are not supported
            validationNotificationType = validationNotificationType.replace("_HTML", "");
        }

        Notifications notifications = getScreenContext(origin).getNotifications();

        notifications.create(NotificationType.valueOf(validationNotificationType))
                .withCaption(messages.getMainMessage("validationFail.caption"))
                .withDescription(buffer.toString())
                .show();

        focusProblemComponent(errors);
    }

    protected void focusProblemComponent(ValidationErrors errors) {
        com.haulmont.cuba.gui.components.Component component = null;
        if (!errors.getAll().isEmpty()) {
            component = errors.getFirstComponent();
        }
        if (component != null) {
            ComponentsHelper.focusComponent(component);
        }
    }

    /**
     * Validate cross-field BeanValidation rules.
     *
     * @param origin screen controller
     * @param item   item to validate
     * @return validation errors
     */
    public ValidationErrors validateCrossFieldRules(@SuppressWarnings("unused") FrameOwner origin, Entity item) {
        ValidationErrors errors = new ValidationErrors();

        Validator validator = beanValidation.getValidator();
        Set<ConstraintViolation<Entity>> violations = validator.validate(item, UiCrossFieldChecks.class);

        violations.stream()
                .filter(violation -> {
                    Path propertyPath = violation.getPropertyPath();

                    Path.Node lastNode = Iterables.getLast(propertyPath);
                    return lastNode.getKind() == ElementKind.BEAN;
                })
                .forEach(violation -> errors.add(violation.getMessage()));

        return errors;
    }

    /**
     * Shows standard unsaved changes dialog with Discard and Cancel actions.
     *
     * @param origin screen controller
     * @param closeAction close action
     * @return result
     */
    public UnsavedChangesDialogResult showUnsavedChangesDialog(FrameOwner origin,
                                                               @SuppressWarnings("unused") CloseAction closeAction) {
        UnsavedChangesDialogResult result = new UnsavedChangesDialogResult();

        Dialogs dialogs = getScreenContext(origin).getDialogs();
        dialogs.createOptionDialog()
                .withCaption(messages.getMainMessage("closeUnsaved.caption"))
                .withMessage(messages.getMainMessage("closeUnsaved"))
                .withType(Dialogs.MessageType.WARNING)
                .withActions(
                        new DialogAction(DialogAction.Type.YES)
                                .withHandler(e -> {

                                    result.discard();
                                }),
                        new DialogAction(DialogAction.Type.NO, Action.Status.PRIMARY)
                                .withHandler(e -> {
                                    Frame frame = UiControllerUtils.getFrame(origin);
                                    ComponentsHelper.focusChildComponent(frame);

                                    result.cancel();
                                })
                )
                .show();

        return result;
    }

    /**
     * Shows standard save confirmation dialog with Save, Discard and Cancel actions.
     *
     * @param origin screen controller
     * @param closeAction close action
     * @return result
     */
    public SaveChangesDialogResult showSaveConfirmationDialog(FrameOwner origin,
                                                              @SuppressWarnings("unused") CloseAction closeAction) {
        SaveChangesDialogResult result = new SaveChangesDialogResult();

        Dialogs dialogs = getScreenContext(origin).getDialogs();
        dialogs.createOptionDialog()
                .withCaption(messages.getMainMessage("closeUnsaved.caption"))
                .withMessage(messages.getMainMessage("saveUnsaved"))
                .withActions(
                        new DialogAction(DialogAction.Type.OK, Action.Status.PRIMARY)
                                .withCaption(messages.getMainMessage("closeUnsaved.save"))
                                .withHandler(e -> {

                                    result.commit();
                                }),
                        new BaseAction("discard")
                                .withIcon(icons.get(CubaIcon.DIALOG_CANCEL))
                                .withCaption(messages.getMainMessage("closeUnsaved.discard"))
                                .withHandler(e -> {

                                    result.discard();
                                }),
                        new DialogAction(DialogAction.Type.CANCEL)
                                .withIcon(null)
                                .withHandler(e -> {
                                    Frame frame = UiControllerUtils.getFrame(origin);
                                    ComponentsHelper.focusChildComponent(frame);

                                    result.cancel();
                                })
                )
                .show();

        return result;
    }

    /**
     * Callbacks holder for unsaved changes dialog.
     */
    public static class UnsavedChangesDialogResult {
        protected Runnable discardHandler;
        protected Runnable cancelHandler;

        public UnsavedChangesDialogResult() {
        }

        public UnsavedChangesDialogResult onDiscard(Runnable discardHandler) {
            this.discardHandler = discardHandler;
            return this;
        }

        public UnsavedChangesDialogResult onCancel(Runnable cancelHandler) {
            this.cancelHandler = cancelHandler;
            return this;
        }

        public void discard() {
            if (discardHandler != null) {
                discardHandler.run();
            }
        }

        public void cancel() {
            if (cancelHandler != null) {
                cancelHandler.run();
            }
        }
    }

    /**
     * Callbacks holder for save changes dialog.
     */
    public static class SaveChangesDialogResult {
        protected Runnable commitHandler;
        protected Runnable discardHandler;
        protected Runnable cancelHandler;

        public SaveChangesDialogResult() {
        }

        public SaveChangesDialogResult onCommit(Runnable commitHandler) {
            this.commitHandler = commitHandler;
            return this;
        }

        public SaveChangesDialogResult onDiscard(Runnable discardHandler) {
            this.discardHandler = discardHandler;
            return this;
        }

        public SaveChangesDialogResult onCancel(Runnable cancelHandler) {
            this.cancelHandler = cancelHandler;
            return this;
        }

        public void commit() {
            if (commitHandler != null) {
                commitHandler.run();
            }
        }

        public void discard() {
            if (discardHandler != null) {
                discardHandler.run();
            }
        }

        public void cancel() {
            if (cancelHandler != null) {
                cancelHandler.run();
            }
        }
    }
}