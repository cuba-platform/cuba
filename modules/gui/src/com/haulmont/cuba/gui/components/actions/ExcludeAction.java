/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.03.11 19:05
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Set;

public class ExcludeAction extends RemoveAction {

    private static final long serialVersionUID = 62824552031621792L;

    public static final String ACTION_ID = "exclude";

    protected final boolean confirm;

    public ExcludeAction(ListComponent owner, boolean autocommit, boolean confirm) {
        this(owner, autocommit, confirm, ACTION_ID);
    }

    public ExcludeAction(ListComponent owner, boolean autocommit, boolean confirm, String id) {
        super(owner, autocommit, id);
        this.confirm = confirm;
    }

    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Exclude");
    }

    public void actionPerform(Component component) {
        if(!isEnabled()) return;
        final Set selected = owner.getSelected();
        if (!selected.isEmpty()) {
            if (confirm) {
                confirmAndRemove(selected);
            } else {
                doRemove(selected, autocommit);
                afterRemove(selected);
            }
        }
    }

    protected void doRemove(Set selected, boolean autocommit) {
        @SuppressWarnings({"unchecked"})
        final CollectionDatasource ds = owner.getDatasource();
        for (Object item : selected) {
            ds.excludeItem((Entity) item);
        }

        if (this.autocommit) {
            try {
                ds.commit();
            } catch (RuntimeException e) {
                ds.refresh();
                throw e;
            }
        }
    }
}
