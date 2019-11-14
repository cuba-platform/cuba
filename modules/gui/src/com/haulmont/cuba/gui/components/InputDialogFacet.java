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

package com.haulmont.cuba.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.meta.*;
import com.haulmont.cuba.gui.screen.CloseAction;

import java.util.EventObject;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Prepares and shows input dialogs.
 */
@StudioFacet(
        caption = "Input Dialog",
        description = "Prepares and shows input dialogs",
        defaultProperty = "message",
        defaultEvent = "CloseEvent"
)
@StudioProperties(
        properties = {
                @StudioProperty(name = "id", required = true)
        }
)
public interface InputDialogFacet extends Facet, ActionsAwareDialogFacet<InputDialogFacet>, HasSubParts {

    /**
     * Sets dialog caption.
     * @param caption caption
     */
    @StudioProperty(type = PropertyType.LOCALIZED_STRING)
    void setCaption(String caption);

    /**
     * @return dialog caption
     */
    String getCaption();

    /**
     * Sets dialog width.
     * @param width width
     */
    @StudioProperty
    void setWidth(String width);

    /**
     * @return dialog width
     */
    float getWidth();

    /**
     * @return dialog width size unit
     */
    SizeUnit getWidthSizeUnit();

    /**
     * Sets dialog height.
     * @param height height
     */
    @StudioProperty
    void setHeight(String height);

    /**
     * @return dialog height
     */
    float getHeight();

    /**
     * @return dialog height size unit
     */
    SizeUnit getHeightSizeUnit();

    /**
     * Sets that dialog should be shown when action with id {@code actionId}
     * is performed.
     *
     * @param actionId action id
     */
    @StudioProperty(type = PropertyType.COMPONENT_ID)
    void setActionTarget(String actionId);

    /**
     * @return id of action that triggers dialog
     */
    String getActionTarget();

    /**
     * Sets that dialog should be shown when button with id {@code actionId}
     * is clicked.
     *
     * @param buttonId button id
     */
    @StudioProperty(type = PropertyType.COMPONENT_ID)
    void setButtonTarget(String buttonId);

    /**
     * @return id of button that triggers dialog
     */
    String getButtonTarget();

    /**
     * Defines a set of predefined actions to use in dialog.
     *
     * @param dialogActions one of {@link DialogActions} values
     */
    @StudioProperty(type = PropertyType.ENUMERATION)
    void setDialogActions(DialogActions dialogActions);

    /**
     * @return set of predefined actions used in dialog
     */
    DialogActions getDialogActions();

    /**
     * Adds the given {@code Consumer} as dialog {@link InputDialog.InputDialogCloseEvent} listener.
     *
     * @param closeListener close listener
     *
     * @return close event subscription
     */
    @StudioEvent
    Subscription addCloseListener(Consumer<CloseEvent> closeListener);

    /**
     * Sets input dialog result handler.
     *
     * @param dialogResultHandler result handler
     */
    @StudioDelegate
    void setDialogResultHandler(Consumer<InputDialog.InputDialogResult> dialogResultHandler);

    /**
     * Sets additional handler for field validation. It receives input dialog context and must return {@link ValidationErrors}
     * instance. Returned validation errors will be shown with another errors from fields.
     *
     * @param validator validator
     */
    @StudioDelegate
    void setValidator(Function<InputDialog.ValidationContext, ValidationErrors> validator);

    /**
     * Sets input dialog parameters.
     *
     * @param parameters set of {@link InputParameter}
     */
    void setParameters(InputParameter... parameters);

    /**
     * Creates InputDialog.
     *
     * @return input dialog instance
     */
    InputDialog create();

    /**
     * Shows InputDialog.
     */
    InputDialog show();

    /**
     * Event that is fired when InputDialog is closed.
     */
    class CloseEvent extends EventObject {

        protected CloseAction closeAction;
        protected Map<String, Object> values;

        public CloseEvent(InputDialogFacet source, CloseAction closeAction, Map<String, Object> values) {
            super(source);
            this.closeAction = closeAction;
            this.values = values;
        }

        @Override
        public InputDialogFacet getSource() {
            return (InputDialogFacet) super.getSource();
        }

        public CloseAction getCloseAction() {
            return closeAction;
        }

        public Map<String, Object> getValues() {
            return values;
        }
    }
}
