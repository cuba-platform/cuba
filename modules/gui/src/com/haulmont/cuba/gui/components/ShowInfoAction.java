/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
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
        return messages.getMessage(AppConfig.getMessagesPack(), "table.showInfoAction");
    }

    @Override
    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
        if (ds == null)
            return;

        if (component instanceof Component.BelongToFrame) {
            showInfo(ds.getItem(), ds.getMetaClass(), (Component.BelongToFrame) component);
        }
    }

    public void showInfo(Entity entity, MetaClass metaClass, Component.BelongToFrame component) {
        Map<String, Object> params = new HashMap<>();
        params.put("metaClass", metaClass);
        params.put("item", entity);
        IFrame frame = (component).getFrame();
        frame.openWindow("sysInfoWindow", WindowManager.OpenType.DIALOG, params);
    }
}
