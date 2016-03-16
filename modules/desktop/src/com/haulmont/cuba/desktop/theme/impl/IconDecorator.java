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

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.theme.ComponentDecorator;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;

import javax.swing.*;
import java.util.Set;

/**
 * Assigns icon to swing or cuba label.
 * Can be used also for table cells.
 *
 */
public class IconDecorator implements ComponentDecorator {
    private String iconName;

    public IconDecorator(String iconName) {
        this.iconName = iconName;
    }

    @Override
    public void decorate(Object component, Set<String> state) {
        JLabel label;
        if (component instanceof JLabel) {
            label = (JLabel) component;
        } else if (component instanceof Label) {
            label = (JLabel) DesktopComponentsHelper.unwrap((Component) component);
        } else {
            throw new RuntimeException("Component is not suitable: " + component);
        }

        Icon icon = App.getInstance().getResources().getIcon(iconName);
        label.setIcon(icon);
    }
}