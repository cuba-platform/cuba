/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.filter.HasAction;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.gui.AppConfig;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CustomOperationEditor extends OperationEditor implements HasAction<Component> {

    private final Button btn = WebComponentsHelper.createButton();

    public CustomOperationEditor(final AbstractCondition condition) {
        super(condition);

        btn.setStyleName(BaseTheme.BUTTON_LINK);

        Messages messages = AppBeans.get(Messages.NAME);
        btn.setCaption(messages.getMessage(AppConfig.getMessagesPack(), "actions.Edit"));

        Security security = AppBeans.get(Security.NAME);
        btn.setEnabled(security.isSpecificPermitted("cuba.gui.filter.customConditions"));

        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                doAction(btn);
            }
        });

        impl.addComponent(btn);
    }

    @Override
    public void doAction(Component component) {
        final CustomConditionEditDlg dlg = new CustomConditionEditDlg((CustomCondition) condition);
        dlg.getImpl().addCloseListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                App.getInstance().getAppUI().removeWindow(dlg.getImpl());
            }
        });
        App.getInstance().getAppUI().addWindow(dlg.getImpl());
        dlg.getImpl().center();
    }
}