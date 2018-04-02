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

package com.haulmont.cuba.web.toolkit.ui.client.tabsheetcsslayout;

import com.haulmont.cuba.web.widgets.CubaTabSheetCssLayout;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.csslayout.CssLayoutConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaTabSheetCssLayout.class)
public class CubaTabSheetCssLayoutConnector extends CssLayoutConnector {

    // This method is copied from the superclass but its parts are swapped
    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        for (ComponentConnector child : event.getOldChildren()) {
            if (child.getParent() == this) {
                // Skip current children
                continue;
            }
            getWidget().remove(child.getWidget());
            VCaption vCaption = childIdToCaption.get(child.getConnectorId());
            if (vCaption != null) {
                childIdToCaption.remove(child.getConnectorId());
                getWidget().remove(vCaption);
            }
        }

        int index = 0;
        for (ComponentConnector child : getChildComponents()) {
            VCaption childCaption = childIdToCaption
                    .get(child.getConnectorId());
            if (childCaption != null) {
                getWidget().addOrMove(childCaption, index++);
            }
            getWidget().addOrMove(child.getWidget(), index++);
        }
    }
}
