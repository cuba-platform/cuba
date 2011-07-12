/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropOperationEditor extends OperationEditor {
    public RuntimePropOperationEditor(final AbstractCondition condition) {
        super(condition);

        Button btn = WebComponentsHelper.createButton();
        btn.setStyleName(BaseTheme.BUTTON_LINK);
        btn.setCaption(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Edit"));

        btn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                RuntimePropConditionEditDlg dlg = new RuntimePropConditionEditDlg((RuntimePropCondition) condition);
                final Window dlgWindow = dlg.getImpl();
                dlgWindow.addListener(new Window.CloseListener() {
                    public void windowClose(Window.CloseEvent e) {
                        App.getInstance().getAppWindow().removeWindow(dlgWindow);
                    }
                });
                App.getInstance().getAppWindow().addWindow(dlgWindow);
                dlgWindow.center();
            }
        });

        layout.addComponent(btn);
    }
}
