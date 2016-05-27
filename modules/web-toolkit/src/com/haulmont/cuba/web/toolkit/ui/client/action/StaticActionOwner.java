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
package com.haulmont.cuba.web.toolkit.ui.client.action;

import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ui.Action;
import com.vaadin.client.ui.ActionOwner;

public class StaticActionOwner implements ActionOwner {

    private Action[] actions;

    private ApplicationConnection connection;

    private String paintableId;

    public StaticActionOwner(ApplicationConnection connection, String paintableId) {
        this.connection = connection;
        this.paintableId = paintableId;
    }

    @Override
    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    @Override
    public ApplicationConnection getClient() {
        return connection;
    }

    @Override
    public String getPaintableId() {
        return paintableId;
    }
}