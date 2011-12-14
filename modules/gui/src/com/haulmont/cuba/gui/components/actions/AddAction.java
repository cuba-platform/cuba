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
 * Standard list action adding an entity instance to list from a lookup screen.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor or setting properties.
 * </p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class AddAction extends AbstractAction {

    private static final long serialVersionUID = -4102961617048369835L;

    public static final String ACTION_ID = ListActionType.ADD.getId();

    protected final ListComponent owner;
    protected Window.Lookup.Handler handler;
    protected WindowManager.OpenType openType;

    protected String windowId;
    protected Map<String, Object> windowParams;

    /**
     * The simplest constructor. The action has default name and opens the lookup screen in THIS tab.
     * Lookup handler must be set by subsequent call to {@link #setHandler(com.haulmont.cuba.gui.components.Window.Lookup.Handler)}
     * @param owner    component containing this action
     */
    public AddAction(ListComponent owner) {
        this(owner, null, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    /**
     * The simplest constructor. The action has default name and opens the lookup screen in THIS tab.
     * @param owner    component containing this action
     * @param handler   lookup handler
     */
    public AddAction(ListComponent owner, Window.Lookup.Handler handler) {
        this(owner, handler, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    /**
     * Constructor that allows to specify how the lookup screen opens. The action has default name.
     * @param owner    component containing this action
     * @param handler   lookup handler
     * @param openType  how to open the editor screen
     */
    public AddAction(ListComponent owner, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        this(owner, handler, openType, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the action name and how the lookup screen opens.
     * @param owner    component containing this action
     * @param handler   lookup handler
     * @param openType  how to open the editor screen
     * @param id        action's name
     */
    public AddAction(ListComponent owner, Window.Lookup.Handler handler, WindowManager.OpenType openType, String id) {
        super(id);
        this.owner = owner;
        this.handler = handler;
        this.openType = openType;
        this.caption = MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Add");
        this.icon = "icons/add.png";
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

        owner.getFrame().openLookup(getWindowId(), handler, openType, params);
    }

    /**
     * @return  handler to pass to lookup screen
     */
    public Window.Lookup.Handler getHandler() {
        return handler;
    }

    /**
     * @param handler   handler to pass to lookup screen
     */
    public void setHandler(Window.Lookup.Handler handler) {
        this.handler = handler;
    }

    /**
     * @return  lookup screen open type
     */
    public WindowManager.OpenType getOpenType() {
        return openType;
    }

    /**
     * @param openType  lookup screen open type
     */
    public void setOpenType(WindowManager.OpenType openType) {
        this.openType = openType;
    }

    /**
     * @return  lookup screen id
     */
    public String getWindowId() {
        if (windowId != null)
            return windowId;
        else
            return owner.getDatasource().getMetaClass().getName() + ".browse";
    }

    /**
     * @param windowId  lookup screen id
     */
    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }

    /**
     * @return  lookup screen parameters
     */
    public Map<String, Object> getWindowParams() {
        return windowParams;
    }

    /**
     * @param windowParams  lookup screen parameters
     */
    public void setWindowParams(Map<String, Object> windowParams) {
        this.windowParams = windowParams;
    }
}
