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

    @Override
    public void init(Map<String, Object> params) {
    }
}