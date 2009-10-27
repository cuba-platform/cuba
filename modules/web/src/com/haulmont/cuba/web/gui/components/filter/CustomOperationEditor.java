/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.10.2009 14:44:33
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.UserSessionClient;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

public class CustomOperationEditor extends OperationEditor {

    public CustomOperationEditor(final Condition condition) {
        super(condition);

        Button btn = WebComponentsHelper.createButton();
        btn.setCaption(MessageProvider.getMessage(getClass(), "CustomOperationEditor.button"));

        btn.setEnabled(UserSessionClient.getUserSession().isSpecificPermitted("cuba.gui.filter.customConditions"));

        btn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                final CustomConditionEditDlg dlg = new CustomConditionEditDlg((CustomCondition) condition);
                dlg.addListener(new Window.CloseListener() {
                    public void windowClose(Window.CloseEvent e) {
                        App.getInstance().getMainWindow().removeWindow(dlg);
                    }
                });
                App.getInstance().getMainWindow().addWindow(dlg);
                dlg.center();
            }
        });

        layout.addComponent(btn);
    }
}
