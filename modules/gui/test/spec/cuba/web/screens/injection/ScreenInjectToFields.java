/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.cuba.web.screens.injection;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.MessageBundle;
import com.haulmont.cuba.gui.screen.Screen;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;

public class ScreenInjectToFields extends Screen {
    @Inject
    private Logger logger;

    @Inject
    private Button button;
    @Inject
    private Messages messages;

    @Named("commit")
    private Action commitAction;

    @Named("fieldGroup.name")
    private TextField textField;

    @Named("usersTable.create")
    private Action createAction;

    @Inject
    private Notifications notifications;
    @Inject
    private Screens screens;
    @Inject
    private Dialogs dialogs;

    @Inject
    private MessageBundle messageBundle;
}