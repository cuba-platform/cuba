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

package spec.cuba.web.sanitizer.screens;

import com.haulmont.cuba.gui.components.MessageDialogFacet;
import com.haulmont.cuba.gui.components.NotificationFacet;
import com.haulmont.cuba.gui.components.OptionDialogFacet;
import com.haulmont.cuba.gui.components.RichTextArea;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;

@UiController
@UiDescriptor("sanitizer-screen.xml")
public class SanitizerScreen extends Screen {

    @Inject
    public TextField<String> textField;

    @Inject
    public RichTextArea richTextArea;

    @Inject
    public MessageDialogFacet messageDialogFacet;

    @Inject
    public OptionDialogFacet optionDialogFacet;

    @Inject
    public NotificationFacet notificationFacet;
}
