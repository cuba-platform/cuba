/*
 * Copyright (c) 2008-2019 Haulmont.
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

package spec.cuba.web.editorscreenfacet.screens;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;

@SuppressWarnings({"unused", "InvalidInstalledDelegate"})
@LoadDataBeforeShow
@UiController("cuba_EditorScreenFacetTestScreen")
@UiDescriptor("editor-screen-facet-test-screen.xml")
public class EditorScreenFacetTestScreen extends Screen {

    @Inject
    public Metadata metadata;

    @Inject
    public CollectionContainer<User> userDc;

    @Inject
    public PickerField<User> userField;
    @Inject
    public Table<User> usersTable;

    @Inject
    public Action action;
    @Inject
    public Button button;

    @Inject
    public EditorScreenFacet<User, UserEditor> editorScreenFacet;
    @Inject
    public EditorScreenFacet<User, UserEditor> tableScreenFacet;
    @Inject
    public EditorScreenFacet<User, UserEditor> fieldScreenFacet;
    @Inject
    public EditorScreenFacet<User, UserEditor> editorEntityProvider;

    @Install(to = "editorScreenFacet", subject = "entityProvider")
    public User provideEntity() {
        User testUser = metadata.create(User.class);
        testUser.setName("Test user");

        return testUser;
    }

    @Install(to = "editorEntityProvider", subject = "entityProvider")
    public User provideUser() {
        User testUser = metadata.create(User.class);
        testUser.setName("Test user");

        return testUser;
    }

    @Install(to = "editorScreenFacet", subject = "initializer")
    public void initEntity(User user) {
        user.setEmail("test@email.com");
    }
}
