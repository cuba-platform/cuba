/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class ShowInfoAction extends BaseAction {

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
        Frame frame = (component).getFrame();
        frame.openWindow("sysInfoWindow", WindowManager.OpenType.DIALOG, params);
    }
}