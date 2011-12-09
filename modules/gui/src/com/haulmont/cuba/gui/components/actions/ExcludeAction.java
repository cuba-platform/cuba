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

/**
 * The <code>RemoveAction</code> variant that excludes instances from the list, but doesn't delete them from DB.
 * Makes sense for removing items from non-aggregating OneToMany collections.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ExcludeAction extends RemoveAction {

    private static final long serialVersionUID = 62824552031621792L;

    public static final String ACTION_ID = "exclude";

    protected final boolean confirm;

    /**
     * Constructor that allows to specify autocommit and confirm value. The action has default name.
     * @param holder        component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param confirm       whether to show the confirmation dialog to user
     */
    public ExcludeAction(ListComponent holder, boolean autocommit, boolean confirm) {
        this(holder, autocommit, confirm, ACTION_ID);
    }

    /**
     * Constructor that allows to specify all parameters.
     * @param holder        component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param confirm       whether to show the confirmation dialog to user
     * @param id            action's name
     */
    public ExcludeAction(ListComponent holder, boolean autocommit, boolean confirm, String id) {
        super(holder, autocommit, id);
        this.confirm = confirm;
    }

    @Override
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Exclude");
    }

    @Override
    public void actionPerform(Component component) {
        if(!isEnabled()) return;
        final Set selected = holder.getSelected();
        if (!selected.isEmpty()) {
            if (confirm) {
                confirmAndRemove(selected);
            } else {
                doRemove(selected, autocommit);
                afterRemove(selected);
            }
        }
    }

    @Override
    protected void doRemove(Set selected, boolean autocommit) {
        @SuppressWarnings({"unchecked"})
        final CollectionDatasource ds = holder.getDatasource();
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
