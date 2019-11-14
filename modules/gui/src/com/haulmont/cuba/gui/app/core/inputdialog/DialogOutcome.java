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

package com.haulmont.cuba.gui.app.core.inputdialog;

import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog.InputDialogCloseEvent;
import com.haulmont.cuba.gui.screen.*;

/**
 * A possible outcome of the input dialog with the standard close actions.
 * <p>
 * Constants of this enum can be used instead of {@link CloseAction} instances in {@link InputDialogCloseEvent#closedWith(DialogOutcome)}
 * method to determine how the dialog was closed.
 *
 * @see #OK
 * @see #CANCEL
 * @see #YES
 * @see #NO
 */
public enum DialogOutcome {

    /**
     * User clicked OK.
     */
    OK(InputDialog.INPUT_DIALOG_OK_ACTION),

    /**
     * User clicked CANCEL.
     */
    CANCEL(InputDialog.INPUT_DIALOG_CANCEL_ACTION),

    /**
     * User clicked YES.
     */
    YES(InputDialog.INPUT_DIALOG_YES_ACTION),

    /**
     * User clicked NO.
     */
    NO(InputDialog.INPUT_DIALOG_NO_ACTION);

    private CloseAction closeAction;

    DialogOutcome(CloseAction closeAction) {
        this.closeAction = closeAction;
    }

    public CloseAction getCloseAction() {
        return closeAction;
    }
}
