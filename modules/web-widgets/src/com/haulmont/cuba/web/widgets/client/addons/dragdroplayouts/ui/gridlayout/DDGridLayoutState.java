/*
 * Copyright 2015 John Ahlroos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.gridlayout;

import com.vaadin.shared.annotations.DelegateToWidget;
import com.vaadin.shared.ui.gridlayout.GridLayoutState;

import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.DDLayoutState;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.DragAndDropAwareState;

public class DDGridLayoutState extends GridLayoutState
        implements DragAndDropAwareState {

    public static final float DEFAULT_HORIZONTAL_RATIO = 0.2f;
    public static final float DEFAULT_VERTICAL_RATIO = 0.2f;

    @DelegateToWidget
    public float cellLeftRightDropRatio = DEFAULT_HORIZONTAL_RATIO;

    @DelegateToWidget
    public float cellTopBottomDropRatio = DEFAULT_VERTICAL_RATIO;

    public DDLayoutState ddState = new DDLayoutState();

    @Override
    public DDLayoutState getDragAndDropState() {
        return ddState;
    }
}
