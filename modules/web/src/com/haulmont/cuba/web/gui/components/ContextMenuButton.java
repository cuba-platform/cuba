/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.Action;
import org.apache.commons.lang.StringUtils;

/**
 * @author artamonov
 * @version $Id$
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