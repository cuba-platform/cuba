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

package com.haulmont.cuba.gui.app.security.user.resetpasswords;

import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

public class NewPasswordsList extends AbstractWindow {

    public interface Companion {
        void allowTextSelection(Table passwordsTable);
    }

    @Inject
    protected Table passwordsTable;

    @Inject
    protected CollectionDatasource<User, UUID> usersDs;

    @WindowParam(required = true)
    protected Map<User, String> passwords;

    @Inject
    protected ThemeConstants themeConstants;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogOptions()
                .setResizable(true)
                .setHeight(themeConstants.getInt("cuba.gui.NewPasswordsList.height"));

        passwordsTable.getColumn("id").setFormatter(new Formatter<UUID>() {
            @Override
            public String format(UUID id) {
                if (id == null) {
                    return "";
                }

                User user = usersDs.getItem(id);
                if (user != null) {
                    return passwords.get(user);
                } else {
                    return "";
                }
            }
        });

        for (User user : passwords.keySet()) {
            usersDs.includeItem(user);
        }
        usersDs.refresh();

        Companion companion = getCompanion();
        if (companion != null) {
            companion.allowTextSelection(passwordsTable);
        }
    }

    public void close() {
        close(Window.CLOSE_ACTION_ID);
    }
}