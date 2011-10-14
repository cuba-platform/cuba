/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.filter.HasAction;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.StringUtils;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropOperationEditor extends OperationEditor implements HasAction {

    private final Button btn = WebComponentsHelper.createButton();

    public RuntimePropOperationEditor(final AbstractCondition condition) {
        super(condition);
        btn.setStyleName(BaseTheme.BUTTON_LINK);
        btn.setIcon(new ThemeResource("icons/edit.png"));
        setCaption(btn);

        btn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                doAction();
            }
        });

        layout.addComponent(btn);
    }

    private void setCaption(Button btn) {
        String caption = condition.getOperationCaption();
        if (StringUtils.isEmpty(caption)) {
            caption = MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Edit");
        }
        btn.setCaption(caption);
    }

    @Override
    public void doAction() {
        RuntimePropConditionEditDlg dlg = new RuntimePropConditionEditDlg((RuntimePropCondition) condition);
        final Window dlgWindow = dlg.getImpl();
        dlgWindow.addListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                setCaption(btn);
                App.getInstance().getAppWindow().removeWindow(dlgWindow);
            }
        });
        App.getInstance().getAppWindow().addWindow(dlgWindow);
        dlgWindow.center();
    }
}
