/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.03.11 19:17
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.HashMap;
import java.util.Map;

/**
 * List action to add an entity instance to list from a lookup screen.
 * <p>
 *      Action's behaviour can be customized by providing arguments to constructor, as well as overriding the following
 *      methods:
 *      <ul>
 *          <li>{@link #getCaption()}</li>
 *          <li>{@link #isEnabled()}</li>
 *          <li>{@link #getWindowId()}</li>
 *          <li>{@link #getWindowParams()}</li>
 *      </ul>
 * </p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class AddAction extends AbstractAction {

    private static final long serialVersionUID = -4102961617048369835L;

    public static final String ACTION_ID = "add";

    protected ListComponent holder;
    protected final Window.Lookup.Handler handler;
    protected final WindowManager.OpenType openType;
    protected CollectionDatasource datasource;

    /**
     * The simplest constructor. The action has default name and opens the lookup screen in THIS tab.
     * @param holder    component containing this action
     * @param handler   lookup handler
     */
    public AddAction(ListComponent holder, Window.Lookup.Handler handler) {
        this(holder, handler, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    /**
     * Constructor that allows to specify how the lookup screen opens. The action has default name.
     * @param holder    component containing this action
     * @param handler   lookup handler
     * @param openType  how to open the editor screen
     */
    public AddAction(ListComponent holder, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        this(holder, handler, openType, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name and how the lookup screen opens.
     * @param holder    component containing this action
     * @param handler   lookup handler
     * @param openType  how to open the editor screen
     * @param id        action's name
     */
    public AddAction(ListComponent holder, Window.Lookup.Handler handler, WindowManager.OpenType openType, String id) {
        super(id);
        this.holder = holder;
        this.handler = handler;
        this.openType = openType;
        this.datasource = holder.getDatasource();
    }

    /**
     * Returns the action's caption. Override to provide a specific caption.
     * @return  localized caption
     */
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Add");
    }

    /**
     * This method is invoked by action owner component. Don't override it, there are special methods to
     * customize behaviour below.
     * @param component component invoking action
     */
    public void actionPerform(Component component) {
        Map<String, Object> params = getWindowParams();
        if (params == null)
            params = new HashMap<String, Object>();

        holder.getFrame().openLookup(getWindowId(), handler, openType, params);
    }

    /**
     * Provides the lookup screen identifier. Override to provide a specific value.
     * @return  lookup screen id
     */
    protected String getWindowId() {
        return datasource.getMetaClass().getName() + ".browse";
    }

    /**
     * Provides the lookup screen parameters. Override to provide a specific value.
     * @return  lookup screen parameters
     */
    protected Map<String, Object> getWindowParams() {
        return null;
    }
}
