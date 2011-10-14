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

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.filter.HasAction;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

public class CustomOperationEditor extends OperationEditor implements HasAction {

    public CustomOperationEditor(final AbstractCondition condition) {
        super(condition);

        Button btn = WebComponentsHelper.createButton();
        btn.setStyleName(BaseTheme.BUTTON_LINK);
        btn.setCaption(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Edit"));

        btn.setEnabled(UserSessionProvider.getUserSession().isSpecificPermitted("cuba.gui.filter.customConditions"));

        btn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                doAction();
            }
        });

        layout.addComponent(btn);
    }


    @Override
    public void doAction() {
        final CustomConditionEditDlg dlg = new CustomConditionEditDlg((CustomCondition) condition);
        dlg.getImpl().addListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                App.getInstance().getAppWindow().removeWindow(dlg.getImpl());
            }
        });
        App.getInstance().getAppWindow().addWindow(dlg.getImpl());
        dlg.getImpl().center();
    }
}
