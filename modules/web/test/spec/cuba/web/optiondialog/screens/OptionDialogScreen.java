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

package spec.cuba.web.optiondialog.screens;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.OptionDialogFacet;
import com.haulmont.cuba.gui.screen.Install;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;

@SuppressWarnings({"unused", "InvalidInstalledDelegate"})
@UiController
@UiDescriptor("option-dialog-screen.xml")
public class OptionDialogScreen extends Screen {

    @Inject
    public Action dialogAction;
    @Inject
    public Button dialogButton;
    @Inject
    public OptionDialogFacet optionDialog;

    @Install(subject = "actionHandler", to = "optionDialog.ok")
    protected void onOkActionPerform() {
    }
}
