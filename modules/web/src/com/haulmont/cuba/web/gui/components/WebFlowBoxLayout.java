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

import com.haulmont.cuba.gui.components.FlowBoxLayout;
import com.haulmont.cuba.web.widgets.CubaFlowLayout;
import com.vaadin.shared.ui.MarginInfo;
import org.apache.commons.lang.StringUtils;

public class WebFlowBoxLayout extends WebAbstractOrderedLayout<CubaFlowLayout> implements FlowBoxLayout {

    protected static final String FLOWLAYOUT_STYLENAME = "c-flowlayout";

    public WebFlowBoxLayout() {
        component = new CubaFlowLayout();
    }

    @Override
    public void setStyleName(String styleName) {
        super.setStyleName(styleName);

        component.addStyleName(FLOWLAYOUT_STYLENAME);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(FLOWLAYOUT_STYLENAME, ""));
    }

    @Override
    public void setMargin(com.haulmont.cuba.gui.components.MarginInfo marginInfo) {
        MarginInfo vMargin = new MarginInfo(marginInfo.hasTop(), marginInfo.hasRight(), marginInfo.hasBottom(),
                marginInfo.hasLeft());
        component.setMargin(vMargin);
    }

    @Override
    public com.haulmont.cuba.gui.components.MarginInfo getMargin() {
        MarginInfo vMargin = component.getMargin();
        return new com.haulmont.cuba.gui.components.MarginInfo(vMargin.hasTop(), vMargin.hasRight(), vMargin.hasBottom(),
                vMargin.hasLeft());
    }

    @Override
    public void setSpacing(boolean enabled) {
        component.setSpacing(enabled);
    }

    @Override
    public boolean getSpacing() {
        return component.isSpacing();
    }
}