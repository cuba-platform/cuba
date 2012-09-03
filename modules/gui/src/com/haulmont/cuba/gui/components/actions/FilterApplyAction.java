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

/**
 * List action to apply current filter by refreshing the underlying datasource.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor or setting properties.
 * </p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class FilterApplyAction extends AbstractAction {

    public static final String ACTION_ID = "apply";

    protected final ListComponent owner;

    /**
     * The simplest constructor. The action has default name.
     * @param owner    component containing this action
     */
    public FilterApplyAction(ListComponent owner) {
        this(owner, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name.
     * @param owner    component containing this action
     * @param id        action name
     */
    public FilterApplyAction(ListComponent owner, String id) {
        super(id);
        this.owner = owner;
        this.caption = MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Apply");
    }

    @Override
    public void actionPerform(Component component) {
        owner.getDatasource().refresh();
    }
}
