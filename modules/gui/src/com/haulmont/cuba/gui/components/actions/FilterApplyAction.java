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
 *      Action's behaviour can be customized by providing arguments to constructor, as well as overriding the following
 *      methods:
 *      <ul>
 *          <li>{@link #getCaption()}</li>
 *          <li>{@link #isEnabled()}</li>
 *      </ul>
 * </p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class FilterApplyAction extends AbstractAction {

    private static final long serialVersionUID = 634685916452545835L;

    public static final String ACTION_ID = "apply";

    protected final ListComponent holder;

    /**
     * The simplest constructor. The action has default name.
     * @param holder    component containing this action
     */
    public FilterApplyAction(ListComponent holder) {
        this(holder, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name.
     * @param holder    component containing this action
     * @param id        action name
     */
    public FilterApplyAction(ListComponent holder, String id) {
        super(id);
        this.holder = holder;
    }

    /**
     * Returns the action's caption. Override to provide a specific caption.
     * @return  localized caption
     */
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Apply");
    }

    @Override
    public void actionPerform(Component component) {
        holder.getDatasource().refresh();
    }
}
