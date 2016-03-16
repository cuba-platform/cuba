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
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;

/**
 * List action to apply current filter by refreshing the underlying datasource.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor or setting properties.
 *
 */
public class FilterApplyAction extends AbstractAction {

    public static final String ACTION_ID = "apply";

    protected final ListComponent owner;

    /**
     * The simplest constructor. The action has default name.
     * @param owner    component containing this action
     */
    public FilterApplyAction(ListComponent owner) {
        this(owner, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name.
     * @param owner    component containing this action
     * @param id        action name
     */
    public FilterApplyAction(ListComponent owner, String id) {
        super(id);
        this.owner = owner;
        this.caption = messages.getMainMessage("actions.Apply");
    }

    @Override
    public void actionPerform(Component component) {
        owner.getDatasource().refresh();
    }
}
