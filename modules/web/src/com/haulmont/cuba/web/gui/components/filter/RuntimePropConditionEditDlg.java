/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.filter.AbstractRuntimePropConditionEditDlg;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.gui.theme.Theme;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author devyatkin
 * @version $Id$
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
        ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
        KeyCombination close = KeyCombination.create(clientConfig.getCloseShortcut());
        KeyCombination commit = KeyCombination.create(clientConfig.getCommitShortcut());

        ShortcutAction closeAction = new ShortcutAction("Close", close.getKey().getCode(),
                KeyCombination.Modifier.codes(close.getModifiers()));
        ShortcutAction commitAction = new ShortcutAction("Commit", commit.getKey().getCode(),
                KeyCombination.Modifier.codes(commit.getModifiers()));


        Map<Action, Runnable> actions = new HashMap<>();
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

            Theme theme = App.getInstance().getUiTheme();
            setWidth(theme.get("cuba.web.RuntimePropConditionEditDlg.editor.width"));

            setModal(true);

            VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            GridLayout grid = new GridLayout();
            grid.setColumns(2);
            grid.setSpacing(true);
            grid.setMargin(new MarginInfo(true, false, true, false));
            grid.setRows(4);

            grid.addComponent(WebComponentsHelper.unwrap(categoryLabel), 0, 1);
            grid.setComponentAlignment(WebComponentsHelper.unwrap(categoryLabel), Alignment.MIDDLE_RIGHT);

            ComboBox categories = (ComboBox) WebComponentsHelper.unwrap(categorySelect);
            categories.setNullSelectionAllowed(false);
            grid.addComponent(categories, 1, 1);

            Label attributeLabel = new Label(AppBeans.get(Messages.class)
                    .getMessage(MESSAGES_PACK, "RuntimePropConditionEditDlg.attributeLabel"));
            grid.addComponent(attributeLabel, 0, 2);
            grid.setComponentAlignment(attributeLabel, Alignment.MIDDLE_RIGHT);

            ComboBox attributes = (ComboBox) WebComponentsHelper.unwrap(attributeSelect);
            attributes.setNullSelectionAllowed(false);
            grid.addComponent(attributes, 1, 2);

            grid.addComponent(WebComponentsHelper.unwrap(operationLabel), 0, 3);
            grid.setComponentAlignment(WebComponentsHelper.unwrap(operationLabel), Alignment.MIDDLE_RIGHT);

            ComboBox operations = (ComboBox) WebComponentsHelper.unwrap(operationSelect);
            operations.setNullSelectionAllowed(false);
            grid.addComponent(operations, 1, 3);

            layout.addComponent(grid);

            HorizontalLayout btnLayout = new HorizontalLayout();
            btnLayout.setSpacing(true);
            btnLayout.setMargin(new MarginInfo(true, false, false, false));

            btnLayout.addComponent(WebComponentsHelper.unwrap(btnOk));
            btnLayout.addComponent(WebComponentsHelper.unwrap(btnCancel));
            layout.addComponent(btnLayout);
        }

        public void closeDlg() {
            close();
        }
    }
}