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

package com.haulmont.cuba.desktop.sys.layout;

import com.haulmont.cuba.gui.components.MarginInfo;

import java.awt.*;

public abstract class LayoutAdapter
        implements
            com.haulmont.cuba.gui.components.HasMargin,
            com.haulmont.cuba.gui.components.HasSpacing {

    protected boolean[] margins = new boolean[4];
    protected boolean spacing;

    public static boolean isDebug() {
        String property = System.getProperty("cuba.desktop.debugLayouts");
        return Boolean.valueOf(property);
    }

    public abstract LayoutManager getLayout();

    protected abstract void update();

    @Override
    public void setMargin(MarginInfo marginInfo) {
        margins[0] = marginInfo.hasTop();
        margins[1] = marginInfo.hasRight();
        margins[2] = marginInfo.hasBottom();
        margins[3] = marginInfo.hasLeft();
        update();
    }

    @Override
    public MarginInfo getMargin() {
        return new MarginInfo(margins[0], margins[1], margins[2], margins[3]);
    }

    @Override
    public void setSpacing(boolean enabled) {
        spacing = enabled;
        update();
    }

    @Override
    public boolean getSpacing() {
        return spacing;
    }

    /*
     * Get a constraints for java.awt.Container.add(java.awt.Component, Object constraints) method
     */
    public Object getConstraints(com.haulmont.cuba.gui.components.Component component) {
        return null;
    }
}