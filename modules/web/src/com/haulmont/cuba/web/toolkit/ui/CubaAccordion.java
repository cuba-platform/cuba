/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.event.Action;
import com.vaadin.server.KeyMapper;
import com.vaadin.ui.Component;

import java.util.HashSet;
import java.util.Stack;

/**
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
