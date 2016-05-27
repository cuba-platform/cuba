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

package com.haulmont.cuba.web.toolkit.ui.client.flowlayout;

import com.vaadin.client.ui.VCssLayout;
import com.vaadin.shared.ui.MarginInfo;

public class CubaFlowLayoutWidget extends VCssLayout {

    public void setMargin(MarginInfo marginInfo) {
        if (marginInfo != null) {
            // Styles inherited from v-csslayout from base theme
            enableStyleDependentName("margin-top", marginInfo.hasTop());
            enableStyleDependentName("margin-right", marginInfo.hasRight());
            enableStyleDependentName("margin-bottom", marginInfo.hasBottom());
            enableStyleDependentName("margin-left", marginInfo.hasLeft());
        }
    }

    public void setSpacing(boolean spacing) {
        enableStyleDependentName("spacing", spacing);
    }

    public void enableStyleDependentName(String suffix, boolean enable) {
        if (enable)
            addStyleDependentName(suffix);
        else
            removeStyleDependentName(suffix);
    }
}