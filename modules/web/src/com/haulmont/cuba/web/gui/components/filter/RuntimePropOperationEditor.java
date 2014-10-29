/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.HasAction;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.StringUtils;

/**
 * @author devyatkin
 * @version $Id$
 */
public class RuntimePropOperationEditor extends OperationEditor implements HasAction<Component> {

    private final Button btn = WebComponentsHelper.createButton();

    public RuntimePropOperationEditor(final AbstractCondition condition) {
        super(condition);
        btn.setStyleName(BaseTheme.BUTTON_LINK);
        btn.setIcon(WebComponentsHelper.getIcon("icons/edit.png"));
        setCaption(btn);

        btn.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                doAction(btn);
            }
        });

        impl.addComponent(btn);
    }

    private void setCaption(Button btn) {
        String caption = condition.getOperationCaption();
        if (StringUtils.isEmpty(caption)) {
            Messages messages = AppBeans.get(Messages.NAME);
            caption = messages.getMainMessage("actions.Edit");
        }
        btn.setCaption(caption);
    }

    @Override
    public void doAction(Component component) {
        RuntimePropConditionEditDlg dlg = new RuntimePropConditionEditDlg((RuntimePropCondition) condition);
        final Window dlgWindow = dlg.getImpl();
        dlgWindow.addCloseListener(new Window.CloseListener() {
            public void windowClose(Window.CloseEvent e) {
                setCaption(btn);
                App.getInstance().getAppUI().removeWindow(dlgWindow);
            }
        });
        App.getInstance().getAppUI().addWindow(dlgWindow);
        dlgWindow.center();
    }
}