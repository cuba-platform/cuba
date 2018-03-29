/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.controllers.ControllerUtils;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.widgets.CubaLabel;
import com.haulmont.cuba.web.widgets.CubaWindow;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.haulmont.cuba.web.gui.components.WebComponentsHelper.setClickShortcut;

/**
 * Handles {@link NoUserSessionException}.
 */
public class NoUserSessionHandler extends AbstractExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(NoUserSessionHandler.class);

    private Locale locale;

    public NoUserSessionHandler() {
        super(NoUserSessionException.class.getName());

        Connection connection = App.getInstance().getConnection();
        //noinspection ConstantConditions
        locale = connection.getSession().getLocale();
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
        try {
            // we may show two or more dialogs if user pressed F5 and we have no valid user session
            // just remove previous dialog and show new
            List<Window> noUserSessionDialogs = app.getAppUI().getWindows().stream()
                    .filter(w -> w instanceof NoUserSessionExceptionDialog)
                    .collect(Collectors.toList());
            for (Window dialog : noUserSessionDialogs) {
                app.getAppUI().removeWindow(dialog);
            }

            showNoUserSessionDialog(app);
        } catch (Throwable th) {
            log.error("Unable to handle NoUserSessionException", throwable);
            log.error("Exception in NoUserSessionHandler", th);
        }
    }

    protected void showNoUserSessionDialog(App app) {
        Messages messages = AppBeans.get(Messages.NAME);

        Window dialog = new NoUserSessionExceptionDialog();
        dialog.setStyleName("c-nousersession-dialog");
        dialog.setCaption(messages.getMainMessage("dialogs.Information", locale));
        dialog.setClosable(false);
        dialog.setResizable(false);
        dialog.setModal(true);

        AppUI ui = app.getAppUI();

        if (ui.isTestMode()) {
            dialog.setCubaId("optionDialog");
            dialog.setId(ui.getTestIdManager().getTestId("optionDialog"));
        }

        CubaLabel messageLab = new CubaLabel();
        messageLab.setWidthUndefined();
        messageLab.setValue(messages.getMainMessage("noUserSession.message", locale));

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(false);
        layout.setWidthUndefined();
        layout.setStyleName("c-nousersession-dialog-layout");
        layout.setSpacing(true);
        dialog.setContent(layout);

        Button reloginBtn = new Button();
        if (ui.isTestMode()) {
            reloginBtn.setCubaId("reloginBtn");
            reloginBtn.setId(ui.getTestIdManager().getTestId("reloginBtn"));
        }
        reloginBtn.addStyleName(WebButton.ICON_STYLE);
        reloginBtn.addStyleName("c-primary-action");
        reloginBtn.addClickListener(event -> relogin());
        reloginBtn.setCaption(messages.getMainMessage(Type.OK.getMsgKey()));

        String iconName = AppBeans.get(Icons.class).get(Type.OK.getIconKey());
        reloginBtn.setIcon(AppBeans.get(IconResolver.class).getIconResource(iconName));

        ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
        setClickShortcut(reloginBtn, clientConfig.getCommitShortcut());

        reloginBtn.focus();

        layout.addComponent(messageLab);
        layout.addComponent(reloginBtn);

        layout.setComponentAlignment(reloginBtn, Alignment.BOTTOM_RIGHT);

        ui.addWindow(dialog);

        dialog.center();
    }

    protected void relogin() {
        String url = ControllerUtils.getLocationWithoutParams() + "?restartApp";
        Page.getCurrent().open(url, "_self");
    }

    public static class NoUserSessionExceptionDialog extends CubaWindow {
    }
}