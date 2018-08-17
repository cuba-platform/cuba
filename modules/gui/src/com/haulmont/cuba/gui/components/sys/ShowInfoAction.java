/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */
package com.haulmont.cuba.gui.components.sys;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;

public class ShowInfoAction extends BaseAction {

    public static final String ACTION_ID = "showSystemInfo";
    public static final String ACTION_PERMISSION = "cuba.gui.showInfo";

    public ShowInfoAction() {
        super(ACTION_ID);

        Messages messages = AppBeans.get(Messages.NAME);
        setCaption(messages.getMainMessage("table.showInfoAction"));
    }

    @Override
    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
        if (component instanceof Component.BelongToFrame
                && component instanceof ListComponent) {

            Entity selectedItem = ((ListComponent) component).getSingleSelected();
            if (selectedItem != null) {
                showInfo(selectedItem, selectedItem.getMetaClass(), (Component.BelongToFrame) component);
            }
        }
    }

    public void showInfo(Entity entity, MetaClass metaClass, Component.BelongToFrame component) {
        LegacyFrame.of(component).openWindow("sysInfoWindow", OpenType.DIALOG,
                ParamsMap.of(
                        "metaClass", metaClass,
                        "item", entity));
    }
}