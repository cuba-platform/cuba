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

public class AddAction extends AbstractAction {

    private static final long serialVersionUID = -4102961617048369835L;

    public static final String ACTION_ID = "add";

    protected ListComponent owner;
    protected final Window.Lookup.Handler handler;
    protected final WindowManager.OpenType openType;
    protected CollectionDatasource datasource;

    public AddAction(ListComponent owner, Window.Lookup.Handler handler) {
        this(owner, handler, WindowManager.OpenType.THIS_TAB, ACTION_ID);
    }

    public AddAction(ListComponent owner, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        this(owner, handler, openType, ACTION_ID);
    }

    public AddAction(ListComponent owner, Window.Lookup.Handler handler, WindowManager.OpenType openType, String id) {
        super(id);
        this.owner = owner;
        this.handler = handler;
        this.openType = openType;
        this.datasource = owner.getDatasource();
    }

    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Add");
    }

    public void actionPerform(Component component) {
        Map<String, Object> params = getWindowParams();
        if (params == null)
            params = new HashMap<String, Object>();

        owner.getFrame().openLookup(getWindowId(), handler, openType, params);
    }

    protected String getWindowId() {
        return datasource.getMetaClass().getName() + ".browse";
    }

    protected Map<String, Object> getWindowParams() {
        return null;
    }
}
