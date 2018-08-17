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

package com.haulmont.cuba.web.gui.components;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.TabWindow;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.vaadin.ui.TabSheet;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class WebTabWindow extends WebWindow implements TabWindow {

    protected BeanLocator beanLocator;

    public WebTabWindow() {
        setSizeFull();
    }

    @Inject
    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Override
    public void setIcon(String icon) {
        super.setIcon(icon);

        if (component.isAttached()) {
            TabSheet.Tab tabWindow = findTab();
            if (tabWindow != null) {
                IconResolver iconResolver = beanLocator.get(IconResolver.NAME);
                tabWindow.setIcon(iconResolver.getIconResource(icon));
            }
        }
    }

    @Nullable
    protected TabSheet.Tab findTab() {
        if (component.isAttached()) {
            com.vaadin.ui.Component parent = component;
            while (parent != null) {
                if (parent.getParent() instanceof TabSheet) {
                    return ((TabSheet) parent.getParent()).getTab(parent);
                }

                parent = parent.getParent();
            }
        }
        return null;
    }

    @Override
    public String formatTabCaption() {
        String s = formatTabDescription();

        WebConfig webConfig = beanLocator.get(Configuration.class)
                .getConfig(WebConfig.class);

        int maxLength = webConfig.getMainTabCaptionLength();
        if (s.length() > maxLength) {
            return s.substring(0, maxLength) + "...";
        } else {
            return s;
        }
    }

    @Override
    public String formatTabDescription() {
        if (!StringUtils.isEmpty(getDescription())) {
            return String.format("%s: %s", getCaption(), getDescription());
        } else {
            return Strings.nullToEmpty(getCaption());
        }
    }
}