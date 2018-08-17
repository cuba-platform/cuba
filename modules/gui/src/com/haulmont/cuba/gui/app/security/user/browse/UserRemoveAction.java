/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.app.security.user.browse;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.events.UserRemovedEvent;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRemoveAction extends RemoveAction {

    protected UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();

    public UserRemoveAction(ListComponent target,
                            UserManagementService userManagementService) {
        super(target);

        initEnabledRules(userManagementService);
    }

    @Override
    protected void afterRemove(Set removedItems) {
        super.afterRemove(removedItems);

        Events events = AppBeans.get(Events.NAME);
        for (Object removedItem : removedItems) {
            UserRemovedEvent event = new UserRemovedEvent((User) removedItem);
            events.publish(event);
        }
    }

    protected void initEnabledRules(UserManagementService userManagementService) {
        addEnabledRule(() -> {
            Set selected = target.getSelected();
            if (!selected.isEmpty()) {
                return !(selected.contains(userSession.getUser())
                        || userSession.getCurrentOrSubstitutedUser().equals(target.getSingleSelected()));
            }
            return false;
        });
        addEnabledRule(() -> {
            Set<User> selected = target.getSelected();
            if (selected.isEmpty())
                return false;

            List<String> logins = selected.stream()
                    .map(User::getLogin)
                    .collect(Collectors.toList());

            return userManagementService.isUsersRemovingAllowed(logins);
        });
    }
}