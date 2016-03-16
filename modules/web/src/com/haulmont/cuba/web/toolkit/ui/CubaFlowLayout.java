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

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.flowlayout.CubaFlowLayoutState;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Layout;

/**
 */
public class CubaFlowLayout extends CssLayout implements Layout.MarginHandler, Layout.SpacingHandler {

    @Override
    protected CubaFlowLayoutState getState() {
        return (CubaFlowLayoutState) super.getState();
    }

    @Override
    protected CubaFlowLayoutState getState(boolean markAsDirty) {
        return (CubaFlowLayoutState) super.getState(markAsDirty);
    }

    @Override
    public void setMargin(boolean enabled) {
        setMargin(new MarginInfo(enabled));
    }

    @Override
    public MarginInfo getMargin() {
        return new MarginInfo(getState().marginsBitmask);
    }

    @Override
    public void setMargin(MarginInfo marginInfo) {
        getState().marginsBitmask = marginInfo.getBitMask();
    }

    @Override
    public void setSpacing(boolean spacing) {
        getState().spacing = spacing;
    }

    @Override
    public boolean isSpacing() {
        return getState().spacing;
    }
}