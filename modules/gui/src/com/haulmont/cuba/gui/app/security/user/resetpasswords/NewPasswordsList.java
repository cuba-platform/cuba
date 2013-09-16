/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.user.resetpasswords;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class NewPasswordsList extends AbstractWindow {

    @Inject
    protected Table passwordsTable;

    @Inject
    protected CollectionDatasource<User, UUID> usersDs;

    protected Map<User, String> passwords;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setResizable(true);
        getDialogParams().setHeight(350);

        passwords = (Map<User, String>) params.get("passwords");
        for (User user : passwords.keySet()) {
            usersDs.includeItem(user);
        }
        usersDs.refresh();

        passwordsTable.getColumn("id").setFormatter(new Formatter<UUID>() {
            @Override
            public String format(UUID id) {
                if (id == null)
                    return "";

                User user = usersDs.getItem(id);
                if (user != null)
                    return passwords.get(user);
                else
                    return "";
            }
        });
    }

    public void close() {
        close(Window.CLOSE_ACTION_ID);
    }
}