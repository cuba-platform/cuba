/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.actions.ChangeSubstUserAction;
import com.haulmont.cuba.web.actions.DoNotChangeSubstUserAction;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.toolkit.ui.CubaComboBox;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.apache.commons.lang.StringUtils;

import java.util.List;

import static com.vaadin.server.Sizeable.Unit;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebUserIndicator extends WebAbstractComponent<HorizontalLayout> implements UserIndicator {

    protected UserSessionSource uss = AppBeans.get(UserSessionSource.NAME);

    protected Label userNameLabel;
    protected CubaComboBox userComboBox;

    public WebUserIndicator() {
        component = new HorizontalLayout();
    }

    @Override
    public void refreshUserSubstitutions() {
        component.removeAllComponents();

        List<UserSubstitution> substitutions = getUserSubstitutions();

        User user = uss.getUserSession().getUser();
        AppUI ui = AppUI.getCurrent();
        if (substitutions.isEmpty()) {
            userComboBox = null;

            userNameLabel = new Label(getSubstitutedUserCaption(user));
            userNameLabel.setStyleName("cuba-user-select-label");
            userNameLabel.setSizeUndefined();

            if (ui.isTestMode()) {
                userNameLabel.setCubaId("currentUserLabel");
            }

            component.addComponent(userNameLabel);
        } else {
            userNameLabel = null;

            userComboBox = new CubaComboBox();
            userComboBox.setFilteringMode(FilteringMode.CONTAINS);

            userComboBox.setNullSelectionAllowed(false);
            userComboBox.setImmediate(true);
            if (ui.isTestMode()) {
                userComboBox.setCubaId("substitutedUserSelect");
                userComboBox.setId(ui.getTestIdManager().reserveId("substitutedUserSelect"));
            }

            userComboBox.setStyleName("cuba-user-select-combobox");
            userComboBox.addItem(user);
            userComboBox.setItemCaption(user, getSubstitutedUserCaption(user));

            for (UserSubstitution substitution : substitutions) {
                User substitutedUser = substitution.getSubstitutedUser();
                userComboBox.addItem(substitutedUser);
                userComboBox.setItemCaption(substitutedUser, getSubstitutedUserCaption(substitutedUser));
            }

            UserSession session = uss.getUserSession();
            userComboBox.select(session.getSubstitutedUser() == null ? session.getUser() : session.getSubstitutedUser());
            userComboBox.addValueChangeListener(new SubstitutedUserChangeListener(userComboBox));

            component.addComponent(userComboBox);
        }

        adjustWidth();
        adjustHeight();
    }

    protected String getSubstitutedUserCaption(User user) {
        return InstanceUtils.getInstanceName(user);
    }

    protected List<UserSubstitution> getUserSubstitutions() {
        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        DataService dataService = AppBeans.get(DataService.NAME);

        LoadContext<UserSubstitution> ctx = new LoadContext<>(UserSubstitution.class);
        LoadContext.Query query = ctx.setQueryString("select us from sec$UserSubstitution us " +
                "where us.user.id = :userId and (us.endDate is null or us.endDate >= :currentDate) " +
                "and (us.startDate is null or us.startDate <= :currentDate) " +
                "and (us.substitutedUser.active = true or us.substitutedUser.active is null) order by us.substitutedUser.name");
        query.setParameter("userId", uss.getUserSession().getUser().getId());
        query.setParameter("currentDate", timeSource.currentTimestamp());
        ctx.setView("app");
        return dataService.loadList(ctx);
    }

    protected void revertToCurrentUser() {
        UserSession us = uss.getUserSession();

        userComboBox.select(us.getCurrentOrSubstitutedUser());
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);

        adjustWidth();
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);

        adjustHeight();
    }

    protected void adjustWidth() {
        if (getWidth() < 0) {
            if (userNameLabel != null) {
                userNameLabel.setWidth(-1, Unit.PIXELS);
            } else if (userComboBox != null) {
                ThemeConstants theme = App.getInstance().getThemeConstants();
                userComboBox.setWidth(theme.get("cuba.web.AppWindow.substUserSelect.width"));
            }
        } else {
            if (userNameLabel != null) {
                userNameLabel.setWidth(100, Unit.PERCENTAGE);
            } else if (userComboBox != null) {
                userComboBox.setWidth(100, Unit.PERCENTAGE);
            }
        }
    }

    protected void adjustHeight() {
        if (getHeight() < 0) {
            if (userNameLabel != null) {
                userNameLabel.setHeight(-1, Unit.PIXELS);
            } else if (userComboBox != null) {
                userComboBox.setHeight(-1, Unit.PIXELS);
            }
        } else {
            if (userNameLabel != null) {
                userNameLabel.setHeight(100, Unit.PERCENTAGE);
            } else if (userComboBox != null) {
                userComboBox.setHeight(100, Unit.PERCENTAGE);
            }
        }
    }

    protected class SubstitutedUserChangeListener implements Property.ValueChangeListener {

        protected final Field userComboBox;

        public SubstitutedUserChangeListener(Field userComboBox) {
            this.userComboBox = userComboBox;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            User newUser = (User) event.getProperty().getValue();
            UserSession userSession = uss.getUserSession();
            if (userSession == null) {
                throw new RuntimeException("No user session found");
            }

            User oldUser = userSession.getSubstitutedUser() == null ? userSession.getUser() : userSession.getSubstitutedUser();

            if (!oldUser.equals(newUser)) {
                String newUserName = StringUtils.isBlank(newUser.getName()) ? newUser.getLogin() : newUser.getName();

                Messages messages = AppBeans.get(Messages.NAME);

                getFrame().showOptionDialog(
                        messages.getMainMessage("substUserSelectDialog.title"),
                        messages.formatMainMessage("substUserSelectDialog.msg", newUserName),
                        Frame.MessageType.WARNING,
                        new Action[]{new ChangeSubstUserAction((User) userComboBox.getValue()) {
                            @Override
                            public void doRevert() {
                                super.doRevert();

                                revertToCurrentUser();
                            }
                        }, new DoNotChangeSubstUserAction() {
                            @Override
                            public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                super.actionPerform(component);

                                revertToCurrentUser();
                            }
                        }}
                );
            }
        }
    }
}