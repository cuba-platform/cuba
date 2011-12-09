/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.03.11 9:18
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentVisitor;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ListComponent;

/**
 * List action to clear all fields in the specific container.
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
public class FilterClearAction extends AbstractAction {

    private static final long serialVersionUID = 391662021995150227L;

    public static final String ACTION_ID = "clear";

    protected final ListComponent holder;
    protected final String containerName;

    /**
     * The simplest constructor. The action has default name.
     * @param holder        component containing this action
     * @param containerName component containing fields to clear
     */
    public FilterClearAction(ListComponent holder, String containerName) {
        this(holder, containerName, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name.
     * @param holder    component containing this action
     * @param containerName component containing fields to clear
     * @param id        action name
     */
    public FilterClearAction(ListComponent holder, String containerName, String id) {
        super(id);
        this.holder = holder;
        this.containerName = containerName;
    }

    /**
     * Returns the action's caption. Override to provide a specific caption.
     * @return  localized caption
     */
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Clear");
    }

    @Override
    public void actionPerform(Component component) {
        Component.Container container = holder.getFrame().getComponent(containerName);
        ComponentsHelper.walkComponents(container,
                new ComponentVisitor() {
                    public void visit(Component component, String name) {
                        if (component instanceof Field) {
                            ((Field) component).setValue(null);
                        }
                    }
                }
        );
    }
}
