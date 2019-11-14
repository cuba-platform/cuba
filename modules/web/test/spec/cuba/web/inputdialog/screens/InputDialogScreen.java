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

package spec.cuba.web.inputdialog.screens;

import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.InputDialogFacet;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.screen.Install;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;

@SuppressWarnings({"unused", "InvalidInstalledDelegate"})
@UiController
@UiDescriptor("input-dialog-screen.xml")
public class InputDialogScreen extends Screen {

    @Inject
    public Button dialogButton;
    @Inject
    public Action dialogAction;
    @Inject
    public InputDialogFacet inputDialog;
    @Inject
    public InputDialogFacet inputDialogCustomActions;

    @Install(to = "inputDialog", subject = "closeListener")
    public void onInputDialogClose(InputDialog.InputDialogCloseEvent closeEvent) {
    }

    @Install(to = "inputDialog", subject = "dialogResultHandler")
    public void handleDialogResults(InputDialog.InputDialogResult inputDialogResult) {
    }

    @Install(to = "inputDialog", subject = "validator")
    public ValidationErrors validateDialogResults(InputDialog.ValidationContext context) {
        return new ValidationErrors();
    }
}
