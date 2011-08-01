/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.03.11 9:17
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;

public class FilterApplyAction extends AbstractAction {

    private static final long serialVersionUID = 634685916452545835L;

    public static final String ACTION_ID = "apply";

    protected final ListComponent owner;

    public FilterApplyAction(ListComponent owner) {
        this(owner, ACTION_ID);
    }

    public FilterApplyAction(ListComponent owner, String id) {
        super(ACTION_ID);
        this.owner = owner;
    }

    public String getCaption() {
        final String messagesPackage = AppConfig.getInstance().getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Apply");
    }

    public void actionPerform(Component component) {
        owner.getDatasource().refresh();
    }
}
