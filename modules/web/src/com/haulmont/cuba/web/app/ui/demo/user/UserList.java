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

package com.haulmont.cuba.web.app.ui.demo.user;

import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.WebBrowserTools;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.events.InitEvent;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;

@UiController("user-list")
public class UserList extends Screen {
    @Inject
    protected ComponentsFactory componentsFactory;
    @Inject
    protected Screens screens;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected Notifications notifications;

    @Inject
    protected WebBrowserTools webBrowserTools;

    @Subscribe
    protected void init(InitEvent event) {
        Label<String> label = componentsFactory.createComponent(Label.NAME);
        label.setValue("Demo " + this);

        getWindow().setCaption("Users");

        Button button = componentsFactory.createComponent(Button.NAME);
        button.setAction(new BaseAction("onClick")
                .withCaption("Demo")
                .withHandler(e -> {

                    dialogs.createOptionDialog()
                            .setCaption("Choose your destiny!")
                            .setMessage("Demo")
                            .setActions(
                                    new DialogAction(DialogAction.Type.OK).withHandler(actionPerformedEvent -> {
                                        notifications.create()
                                                .setCaption("Yep!")
                                                .setDescription("Ooopsy !")
                                                .setType(NotificationType.TRAY)
                                                .setPosition(Notifications.Position.BOTTOM_LEFT)
                                                .show();

                                    }),
                                    new DialogAction(DialogAction.Type.CANCEL)
                            )
                            .show();
                })
        );

        Label<String> spacer = componentsFactory.createComponent(Label.NAME);

        getWindow().add(
                label,
                button,
                spacer
        );
        getWindow().expand(spacer);
    }

    /*@Subscribe
    protected void onResize(Page.BrowserWindowResizeEvent event) {

    }*/
}