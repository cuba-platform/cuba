/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ActionsHolder;
import com.haulmont.cuba.gui.components.Frame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActionHolderAssignActionPostInitTask extends AbstractAssignActionPostInitTask {
    public ActionHolderAssignActionPostInitTask(ActionsHolder component, String actionId, Frame frame) {
        super(component, actionId, frame);
    }

    @Override
    protected boolean hasOwnAction(String id) {
        ActionsHolder actionsHolder = (ActionsHolder) component;
        return actionsHolder.getAction(id) != null;
    }

    @Override
    protected void addAction(Action action) {
        ActionsHolder actionsHolder = (ActionsHolder) component;
        List<Action> existingActions = new ArrayList<>(actionsHolder.getActions());
        for (Action existingAction : existingActions) {
            // Comparing the id of an existing action with the full Id (including path) of the action to be added
            if (Objects.equals(existingAction.getId(), actionId)) {
                int index = existingActions.indexOf(existingAction);
                actionsHolder.removeAction(existingAction);
                actionsHolder.addAction(action, index);
                break;
            }
        }
    }
}
