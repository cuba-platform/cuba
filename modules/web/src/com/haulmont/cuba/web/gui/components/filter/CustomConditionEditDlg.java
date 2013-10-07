/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.filter.AbstractCustomConditionEditDlg;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CustomConditionEditDlg extends AbstractCustomConditionEditDlg<Window> {

    protected Editor impl;

    public CustomConditionEditDlg(CustomCondition condition) {
        super(condition);
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
        App.getInstance().getWindowManager().showNotification(msg,type);
    }

    @Override
    protected void closeDlg() {
       impl.closeWindow();
    }

    protected class Editor extends Window {
        public Editor() {
            super(condition.getLocCaption());
            setWidth(-1, Unit.PIXELS);
            setModal(true);
            setResizable(false);

            VerticalLayout layout = new VerticalLayout();
            layout.setWidth(-1, Unit.PIXELS);
            layout.setSpacing(true);

            setContent(layout);

            entityAlias = condition.getEntityAlias();
            Label eaLab = new Label(messages.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.hintLabel"));
            eaLab.setContentMode(ContentMode.HTML);
            layout.addComponent(eaLab);

            GridLayout grid = new GridLayout();
            grid.setColumns(2);
            grid.setSpacing(true);
            grid.setMargin(new MarginInfo(true, false, true, false));

            int i = 0;
            // allow to change caption if it isn't set in descriptor
            if (StringUtils.isBlank(condition.getCaption())) {
                grid.setRows(7);

                grid.addComponent(WebComponentsHelper.unwrap(nameLab), 0, i);
                grid.setComponentAlignment(WebComponentsHelper.unwrap(nameLab), Alignment.MIDDLE_RIGHT);

                grid.addComponent(WebComponentsHelper.unwrap(nameText), 1, i++);

            } else {
                grid.setRows(6);
                joinText.requestFocus();
            }

            grid.addComponent(WebComponentsHelper.unwrap(joinLab), 0, i);
            grid.setComponentAlignment(WebComponentsHelper.unwrap(joinLab), Alignment.MIDDLE_RIGHT);

            grid.addComponent(WebComponentsHelper.unwrap(joinText), 1, i++);

            grid.addComponent(WebComponentsHelper.unwrap(whereLab), 0, i);
            grid.setComponentAlignment(WebComponentsHelper.unwrap(whereLab), Alignment.MIDDLE_RIGHT);

            grid.addComponent(WebComponentsHelper.unwrap(whereText),1,i++);

            grid.addComponent(WebComponentsHelper.unwrap(typeLab), 0, i);
            grid.setComponentAlignment(WebComponentsHelper.unwrap(typeLab), Alignment.MIDDLE_RIGHT);

            HorizontalLayout typeLayout = new HorizontalLayout();

            ComboBox types = (ComboBox) WebComponentsHelper.unwrap(typeSelect);
            types.setImmediate(true);
            types.setNullSelectionAllowed(false);
            typeLayout.addComponent(types);

            typeLayout.addComponent(WebComponentsHelper.unwrap(typeCheckBox));
            grid.addComponent(typeLayout, 1, i++);

            grid.addComponent(WebComponentsHelper.unwrap(entityLab), 0, i);
            grid.setComponentAlignment(WebComponentsHelper.unwrap(entityLab), Alignment.MIDDLE_RIGHT);

            ComboBox entities = (ComboBox) WebComponentsHelper.unwrap(entitySelect);
            entities.setImmediate(true);
            entities.setFilteringMode(FilteringMode.CONTAINS);
            grid.addComponent(entities, 1, i++);
            grid.setComponentAlignment(entities, Alignment.MIDDLE_RIGHT);

            grid.addComponent(WebComponentsHelper.unwrap(entityParamWhereLab), 0, i);
            grid.setComponentAlignment(WebComponentsHelper.unwrap(entityParamWhereLab), Alignment.MIDDLE_RIGHT);

            TextArea paramWhereText = (TextArea) WebComponentsHelper.unwrap(entityParamWhereText);
            paramWhereText.setNullRepresentation("");
            grid.addComponent(paramWhereText, 1, i++);

            grid.addComponent(WebComponentsHelper.unwrap(entityParamViewLab), 0, i);
            grid.setComponentAlignment(WebComponentsHelper.unwrap(entityParamViewLab), Alignment.MIDDLE_RIGHT);

            TextField paramViewText = (TextField) WebComponentsHelper.unwrap(entityParamViewText);
            paramViewText.setNullRepresentation("");
            grid.addComponent(paramViewText, 1, i);

            layout.addComponent(grid);

            HorizontalLayout btnLayout = new HorizontalLayout();
            btnLayout.setSpacing(true);

            btnLayout.addComponent(WebComponentsHelper.unwrap(btnOk));
            btnLayout.addComponent(WebComponentsHelper.unwrap(btnCancel));
            layout.addComponent(btnLayout);
        }

        public void closeWindow() {
            close();
        }
    }
}