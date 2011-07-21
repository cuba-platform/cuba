/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.09.2010 17:07:29
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.HashMap;
import java.util.Map;

public class ShowInfoAction extends AbstractAction {

    public static final String ACTION_ID = "showSystemInfo";
    public static final String ACTION_PERMISSION = "cuba.gui.showInfo";

    private CollectionDatasource ds;

    public ShowInfoAction() {
        super(ACTION_ID);
    }

    public CollectionDatasource getDatasource() {
        return ds;
    }

    public void setDatasource(CollectionDatasource ds) {
        this.ds = ds;
    }

    @Override
    public String getCaption() {
        return MessageProvider.getMessage(AppConfig.getMessagesPack(), "table.showInfoAction");
    }

    @Override
    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
        if (ds == null)
            return;

        if (component instanceof Component.BelongToFrame) {

            Map<String,Object> params = new HashMap<String, Object>();
            params.put("itemDs", ds);

            IFrame frame = ((Component.BelongToFrame) component).getFrame();
            frame.openWindow("sysInfoWindow", WindowManager.OpenType.DIALOG, params);
        }
    }
}
