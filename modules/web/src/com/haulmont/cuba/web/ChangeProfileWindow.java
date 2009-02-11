/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 05.01.2009 15:28:11
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.terminal.Sizeable;
import com.itmill.toolkit.terminal.ExternalResource;
import com.haulmont.cuba.web.resource.Messages;
import com.haulmont.cuba.web.log.LogLevel;
import com.haulmont.cuba.core.app.BasicService;
import com.haulmont.cuba.core.global.BasicInvocationContext;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.Subject;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.global.LoginException;

import java.util.List;

public class ChangeProfileWindow extends Window
{
    public ChangeProfileWindow() {
        super(Messages.getString("changeProfileWindow.caption"));
        setModal(true);
        initUI();
    }

    private void initUI() {
        ExpandLayout layout = new ExpandLayout(ExpandLayout.ORIENTATION_VERTICAL);
        layout.setMargin(true);
        layout.setSpacing(true);
        setHeight(200, Sizeable.UNITS_PIXELS);
        setWidth(400, Sizeable.UNITS_PIXELS);

        Label label = new Label(Messages.getString("changeProfileWindow.label"));
        layout.addComponent(label);

        final ListSelect select = new ListSelect();
        select.setMultiSelect(false);
        select.setNullSelectionAllowed(false);
        select.setSizeFull();
        select.focus();
        fillItems(select);
        layout.addComponent(select);
        layout.expand(select);

        Button button = new Button(Messages.getString("changeProfileWindow.button"),
                new Button.ClickListener()
                {
                    public void buttonClick(Button.ClickEvent event) {
                        changeProfile((String) select.getValue());
                        Component parent = getParent();
                        if (parent != null && parent instanceof Window) {
                            ((Window) parent).removeWindow(ChangeProfileWindow.this);
                            ((Window) parent).open(new ExternalResource(App.getInstance().getURL()));
                        }
                    }
                }
        );
        layout.addComponent(button);

        setLayout(layout);
    }

    private void changeProfile(String profile) {
        Connection connection = App.getInstance().getConnection();
        try {
            connection.changeProfile(profile);
        } catch (LoginException e) {
            App.getInstance().getAppLog().log(LogLevel.ERROR, "Unable to change profile", e);
            showNotification(e.getMessage());
        }
    }

    private void fillItems(ListSelect select) {
        BasicService bs = ServiceLocator.getBasicService();
        UserSession userSession = App.getInstance().getConnection().getSession();
        BasicInvocationContext ctx = new BasicInvocationContext().setEntityClass(Subject.class);
        ctx.setQueryString("select s from sec$Subject s where s.user.id = :userId")
                .addParameter("userId", userSession.getUserId());
        List<Subject> list = bs.loadList(ctx);
        for (Subject subject : list) {
            if (!subject.getId().equals(userSession.getSubjectId())) {
                select.addItem(subject.getProfile().getName());
            }
        }
        if (!select.getItemIds().isEmpty()) {
            select.select(select.getItemIds().iterator().next());
        }
    }
}
