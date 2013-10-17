/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.filter.AbstractRuntimePropConditionEditDlg;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropConditionEditDlg extends AbstractRuntimePropConditionEditDlg<Window> {
    protected Editor impl;

    public RuntimePropConditionEditDlg(final RuntimePropCondition condition) {
        super(condition);
    }

    @Override
    protected void closeDlg() {
        impl.closeDlg();
    }

    protected void initShortcuts() {
        ShortcutAction closeAction = new ShortcutAction("close", ShortcutAction.KeyCode.ESCAPE, new int[0]);
        ShortcutAction commitAction = new ShortcutAction("commit", ShortcutAction.KeyCode.ENTER,
                new int[]{ShortcutAction.ModifierKey.CTRL});

        Map<Action, Runnable> actions = new HashMap<Action, Runnable>();
        actions.put(closeAction, new Runnable() {
            @Override
            public void run() {
                closeDlg();
            }
        });
        actions.put(commitAction, new Runnable() {
            @Override
            public void run() {
                if (commit())
                    closeDlg();
            }
        });
        WebComponentsHelper.setActions(impl, actions);
    }


    @Override
    public Window getImpl() {
        if (impl == null) {
            impl = new Editor();
            initShortcuts();
        }
        return impl;
    }

    @Override
    protected ParamFactory getParamFactory() {
        return new ParamFactoryImpl();
    }

    @Override
    protected void showNotification(String msg, IFrame.NotificationType type) {
        App.getInstance().getWindowManager().showNotification(msg, type);
    }

    protected class Editor extends Window {
        public Editor() {
            super(condition.getLocCaption());
            setWidth("380px");
            setModal(true);

            VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            GridLayout grid = new GridLayout();
            grid.setColumns(2);
            grid.setSpacing(true);
            grid.setMargin(true, false, true, false);
            grid.setRows(4);

            grid.addComponent(WebComponentsHelper.unwrap(categoryLabel), 0, 1);
            grid.setComponentAlignment(WebComponentsHelper.unwrap(categoryLabel), Alignment.MIDDLE_RIGHT);

            Select categories = (Select) WebComponentsHelper.unwrap(categorySelect);
            categories.setNullSelectionAllowed(false);
            grid.addComponent(categories, 1, 1);

            Label attributeLabel = new Label(MessageProvider.getMessage(MESSAGES_PACK, "RuntimePropConditionEditDlg.attributeLabel"));
            grid.addComponent(attributeLabel, 0, 2);
            grid.setComponentAlignment(attributeLabel, Alignment.MIDDLE_RIGHT);

            Select attributes = (Select) WebComponentsHelper.unwrap(attributeSelect);
            attributes.setNullSelectionAllowed(false);
            grid.addComponent(attributes, 1, 2);

            grid.addComponent(WebComponentsHelper.unwrap(operationLabel), 0, 3);
            grid.setComponentAlignment(WebComponentsHelper.unwrap(operationLabel), Alignment.MIDDLE_RIGHT);

            Select operations = (Select) WebComponentsHelper.unwrap(operationSelect);
            operations.setNullSelectionAllowed(false);
            grid.addComponent(operations, 1, 3);

            layout.addComponent(grid);

            HorizontalLayout btnLayout = new HorizontalLayout();
            btnLayout.setSpacing(true);
            btnLayout.setMargin(true, false, false, false);

            btnLayout.addComponent(WebComponentsHelper.unwrap(btnOk));
            btnLayout.addComponent(WebComponentsHelper.unwrap(btnCancel));
            layout.addComponent(btnLayout);
        }

        public void closeDlg() {
            close();
        }
    }
}
