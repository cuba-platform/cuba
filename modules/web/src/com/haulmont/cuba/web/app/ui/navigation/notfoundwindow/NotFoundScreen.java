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

package com.haulmont.cuba.web.app.ui.navigation.notfoundwindow;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.web.theme.HaloTheme;

import javax.inject.Inject;

@Route("not-found")
@UiController(NotFoundScreen.ID)
public class NotFoundScreen extends Screen {

    public static final String ID = "404 Not Found";

    @WindowParam(name = "requestedRoute", required = true)
    protected String requestedRoute;

    @Inject
    protected UiComponents uiComponents;

    @Inject
    protected Messages messages;

    @Subscribe
    protected void onInit(InitEvent event) {
        Window window = getWindow();

        Label<String> msgLabel = uiComponents.create(Label.TYPE_STRING);
        msgLabel.setAlignment(Component.Alignment.TOP_CENTER);
        msgLabel.addStyleName(HaloTheme.LABEL_H1);
        msgLabel.setValue(messages.formatMessage(NotFoundScreen.class, "notAssociatedRoute", requestedRoute));

        window.add(msgLabel);

        window.setCaption(messages.formatMessage(NotFoundScreen.class, "tabCaption", requestedRoute));
    }
}
