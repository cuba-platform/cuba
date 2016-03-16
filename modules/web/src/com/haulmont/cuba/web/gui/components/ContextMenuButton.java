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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.Action;
import org.apache.commons.lang.StringUtils;

/**
 */
public abstract class ContextMenuButton extends WebButton {

    @Override
    public void setIcon(String icon) {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        if (clientConfig.getShowIconsForPopupMenuActions()) {
            super.setIcon(icon);
        }
    }

    @Override
    public void setAction(Action action) {
        super.setAction(action);

        if (action != null) {
            String caption = action.getCaption();
            if (!StringUtils.isEmpty(caption)) {
                if (action.getShortcut() != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(caption);
                    if (action.getShortcut() != null) {
                        sb.append(" (").append(action.getShortcut().format()).append(")");
                    }
                    caption = sb.toString();
                    component.setCaption(caption);
                }
            }
        }
    }

    @Override
    public void setCaption(String caption) {
        if (action.getShortcut() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(caption);
            if (action.getShortcut() != null) {
                sb.append(" (").append(action.getShortcut().format()).append(")");
            }
            caption = sb.toString();
        }

        super.setCaption(caption);
    }
}