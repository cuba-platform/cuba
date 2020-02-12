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

package spec.cuba.web.screens.injection;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.WebBrowserTools;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.MessageBundle;
import com.haulmont.cuba.gui.screen.Screen;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;

public class ScreenInjectToFields extends Screen {
    @Inject
    private Logger logger;

    @Inject
    private Button button;
    @Autowired
    private Messages messages;

    @Named("commit")
    private Action commitAction;

    @Named("fieldGroup.name")
    private TextField textField;

    @Named("usersTable.create")
    private Action createAction;

    @Resource
    private Notifications notifications;
    @Inject
    private Screens screens;
    @Inject
    private Dialogs dialogs;
    @Inject
    private WebBrowserTools webBrowserTools;

    @Inject
    private MessageBundle messageBundle;
}