/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;

/**
 * List action to apply current filter by refreshing the underlying datasource.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor or setting properties.
 *
 * @author krivopustov
 * @version $Id$
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
        this.caption = messages.getMainMessage("actions.Apply");
    }

    @Override
    public void actionPerform(Component component) {
        owner.getDatasource().refresh();
    }
}
