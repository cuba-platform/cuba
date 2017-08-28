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

package com.haulmont.cuba.gui.app.security.group.browse;

import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

import java.util.EventObject;
import java.util.List;

public class UserGroupChangedEvent extends EventObject {

    protected List<User> users;
    protected Group group;

    public UserGroupChangedEvent(Tree<Group> groupTree, List<User> users, Group group) {
        super(groupTree);
        this.users = users;
        this.group = group;
    }

    public List<User> getUsers(){
        return users;
    }

    public Group getGroup() {
        return group;
    }
}
