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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.gui.components.HBoxLayout;

public class DesktopHBox extends DesktopAbstractBox implements AutoExpanding, HBoxLayout {

    public DesktopHBox() {
        layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.X);
        setHeight("-1px"); // fix layout inside a scrollbox if the height is not set
    }

    @Override
    public boolean expandsWidth() {
        return false;
    }

    @Override
    public boolean expandsHeight() {
        return true;
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        layoutAdapter.setExpandLayout(!widthSize.isOwnSize()); // expand layout if width not -1
    }

    @Override
    public ExpandDirection getExpandDirection() {
        return ExpandDirection.HORIZONTAL;
    }
}