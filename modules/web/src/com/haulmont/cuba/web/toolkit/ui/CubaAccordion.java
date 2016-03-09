/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.event.Action;
import com.vaadin.server.KeyMapper;
import com.vaadin.ui.Component;

import java.util.HashSet;
import java.util.Stack;

/**
 * @author petunin
 */
public class CubaAccordion extends com.vaadin.ui.Accordion {

    public void setTestId(Tab tab, String testId) {
        int tabPosition = getTabPosition(tab);
        getState(true).tabs.get(tabPosition).id = testId;
    }

    public void setCubaId(Tab tab, String id) {
        int tabPosition = getTabPosition(tab);
        getState(true).tabs.get(tabPosition).cubaId = id;
    }

    public String getCubaId(Tab tab) {
        int tabPosition = getTabPosition(tab);
        return getState(true).tabs.get(tabPosition).cubaId;
    }
}
