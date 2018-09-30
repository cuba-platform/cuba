/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.web.tmp;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.EditorScreens;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.List;

@UiController("dcScreen5")
@UiDescriptor("dc-screen-5.xml")
@PrimaryLookupScreen(User.class)
@LookupComponent("usersTable")
public class DcScreen5 extends StandardLookup<User> {

    @Inject
    protected DataManager dataManager;

    @Inject
    protected Screens screens;
    @Inject
    protected EditorScreens editorScreens;
    @Inject
    protected Dialogs dialogs;

    @Inject
    private CollectionContainer<User> usersCont;
    @Inject
    private CollectionLoader usersLoader;

    @Inject
    private Table<User> usersTable;

    @Subscribe
    private void beforeShow(BeforeShowEvent event) {
        getScreenData().loadAll();
    }

    @Install(target = Target.DATA_LOADER, to = "usersLoader")
    private List<User> loadUsers(LoadContext<User> loadContext) {
        List<User> users = dataManager.loadList(loadContext);
        System.out.println("Loaded users: " + users.size());
        return users;
    }
}