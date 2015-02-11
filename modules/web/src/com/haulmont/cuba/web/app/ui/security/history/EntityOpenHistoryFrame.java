/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.security.history;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.ScreenHistoryEntity;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class EntityOpenHistoryFrame extends AbstractFrame {

    @Inject
    protected Table historyTable;

    @Inject
    protected ComponentsFactory componentFactory;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        historyTable.addGeneratedColumn(getMessage("entityOpenHistoryFrame.user"), new Table.ColumnGenerator<ScreenHistoryEntity>() {
            @Override
            public Component generateCell(final ScreenHistoryEntity entity) {
                Label user = componentFactory.createComponent(Label.NAME);
                User substituteUser = entity.getSubstitutedUser();
                if (substituteUser == null) {
                    user.setValue(entity.getUser().getCaption());
                } else {
                    user.setValue(String.format(getMessage("userMessage"), entity.getUser().getCaption(),
                            substituteUser.getCaption()));
                }
                return user;
            }
        });
    }
}