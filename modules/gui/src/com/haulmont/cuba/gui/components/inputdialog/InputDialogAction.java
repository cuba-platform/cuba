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

package com.haulmont.cuba.gui.components.inputdialog;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.icons.Icons;

import java.util.function.Consumer;

/**
 * Action can be used in {@link InputDialog}. It can handle specific {@link InputDialogActionPerformed} event for
 * managing opened dialog.
 *
 * @see InputDialog
 * @see Dialogs.InputDialogBuilder
 */
public class InputDialogAction extends AbstractAction {

    protected boolean validationRequired = true;

    public InputDialogAction(String id) {
        super(id);
    }

    @Override
    public void actionPerform(Component component) {
        if (eventHub != null) {

            InputDialog inputDialog = null;
            if (component instanceof Component.BelongToFrame) {
                Window window = ComponentsHelper.getWindow((Component.BelongToFrame) component);
                if (window != null) {
                    inputDialog = (InputDialog) window.getFrameOwner();
                }
            }

            if (!validationRequired || (inputDialog != null && inputDialog.isValid())) {
                InputDialogActionPerformed event = new InputDialogActionPerformed(this, component, inputDialog);
                eventHub.publish(InputDialogActionPerformed.class, event);
            }
        }
    }

    /**
     * Creates new instance of InputDialogAction.
     *
     * @param id action id
     * @return input dialog action
     */
    public static InputDialogAction action(String id) {
        return new InputDialogAction(id);
    }

    /**
     * Set caption using fluent API method.
     *
     * @param caption caption
     * @return current instance of action
     */
    public InputDialogAction withCaption(String caption) {
        this.caption = caption;
        return this;
    }

    /**
     * Set description using fluent API method.
     *
     * @param description description
     * @return current instance of action
     */
    public InputDialogAction withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set icon using fluent API method.
     *
     * @param icon icon
     * @return current instance of action
     */
    public InputDialogAction withIcon(String icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Sets icon from icons set to the action (e.g. CubaIcon.DIALOG_OK).
     *
     * @param iconKey icon
     * @return current instance of action
     */
    public InputDialogAction withIcon(Icons.Icon iconKey) {
        this.icon = iconKey.source();
        return this;
    }

    /**
     * Set shortcut using fluent API method.
     *
     * @param shortcut shortcut
     * @return current instance of action
     */
    public InputDialogAction withShortcut(String shortcut) {
        if (shortcut != null) {
            this.shortcut = KeyCombination.create(shortcut);
        }
        return this;
    }

    /**
     * Set action performed event handler using fluent API method. Can be used instead of subclassing BaseAction class.
     *
     * @param handler action performed handler
     * @return current instance of action
     */
    public InputDialogAction withHandler(Consumer<InputDialogActionPerformed> handler) {
        getEventHub().subscribe(InputDialogActionPerformed.class, handler);

        return this;
    }

    /**
     * Set whether this action is primary using fluent API method. Can be used instead of subclassing BaseAction class.
     *
     * @param primary primary
     * @return current instance of action
     */
    public InputDialogAction withPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }

    /**
     * Set to true if handler should be invoked after successful validation. False - validation won't be preformed and
     * handler will be invoked. Default value is true.
     *
     * @param validationRequired validation required option
     * @return current instance of action
     */
    public InputDialogAction withValidationRequired(boolean validationRequired) {
        this.validationRequired = validationRequired;
        return this;
    }

    /**
     * @return true if handler should be invoked after successful validation
     */
    public boolean isValidationRequired() {
        return validationRequired;
    }

    /**
     * Describes action performed event from {@link InputDialogAction}. It contains opened {@link InputDialog}.
     */
    public static class InputDialogActionPerformed extends Action.ActionPerformedEvent {

        protected InputDialog inputDialog;

        public InputDialogActionPerformed(Action source, Component component, InputDialog inputDialog) {
            super(source, component);

            this.inputDialog = inputDialog;
        }

        /**
         * @return opened input dialog
         */
        public InputDialog getInputDialog() {
            return inputDialog;
        }
    }
}
