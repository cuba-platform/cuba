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
 */

package com.haulmont.cuba.web.toolkit.ui.client.tablesort;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.*;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComputedStyle;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.ui.VScrollTable;

public class TableCustomSortDelegate {

    protected final EnhancedCubaTableWidget tableWidget;

    public TableCustomSortDelegate(EnhancedCubaTableWidget tableWidget) {
        this.tableWidget = tableWidget;
    }

    public void showSortMenu(final Element sortIndicator, final String columnId) {
        final VOverlay sortDirectionPopup = new VOverlay();
        sortDirectionPopup.setOwner(tableWidget.getOwner());

        FlowPanel sortDirectionMenu = new FlowPanel();
        Label sortByDescendingButton = new Label(tableWidget.getSortDescendingLabel());
        Label sortByAscendingButton = new Label(tableWidget.getSortAscendingLabel());
        Label sortClearSortButton = new Label(tableWidget.getSortResetLabel());

        sortByDescendingButton.addStyleName("cuba-table-contextmenu-item");
        sortByAscendingButton.addStyleName("cuba-table-contextmenu-item");
        sortClearSortButton.addStyleName("cuba-table-contextmenu-item");

        sortDirectionMenu.add(sortByDescendingButton);
        sortDirectionMenu.add(sortByAscendingButton);
        sortDirectionMenu.add(sortClearSortButton);

        sortByDescendingButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                tableWidget.getClient().updateVariable(tableWidget.getPaintableId(), "sortcolumn", columnId, false);
                tableWidget.getClient().updateVariable(tableWidget.getPaintableId(), "sortascending", false, false);

                tableWidget.getRowRequestHandler().deferRowFetch(); // some validation +
                // defer 250ms
                tableWidget.getRowRequestHandler().cancel(); // instead of waiting
                tableWidget.getRowRequestHandler().run(); // run immediately
                sortDirectionPopup.hide();
            }
        });

        sortByAscendingButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                tableWidget.getClient().updateVariable(tableWidget.getPaintableId(), "sortcolumn", columnId, false);
                tableWidget.getClient().updateVariable(tableWidget.getPaintableId(), "sortascending", true, false);

                tableWidget.getRowRequestHandler().deferRowFetch(); // some validation +
                // defer 250ms
                tableWidget.getRowRequestHandler().cancel(); // instead of waiting
                tableWidget.getRowRequestHandler().run(); // run immediately
                sortDirectionPopup.hide();
            }
        });

        sortClearSortButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                tableWidget.getClient().updateVariable(tableWidget.getPaintableId(), "resetsortorder", columnId, true);
                sortIndicator.addClassName("cuba-sort-indicator-visible");
                sortDirectionPopup.hide();
            }
        });

        sortDirectionMenu.addStyleName("cuba-table-contextmenu");
        sortDirectionPopup.setWidget(sortDirectionMenu);

        sortDirectionPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                sortIndicator.removeClassName("cuba-sort-indicator-visible");
            }
        });

        sortDirectionPopup.setAutoHideEnabled(true);
        ComputedStyle sortIndicatorStyle = new ComputedStyle(sortIndicator);
        sortDirectionPopup.setPopupPosition(sortIndicator.getAbsoluteLeft(), sortIndicator.getAbsoluteTop() +
                ((int) sortIndicatorStyle.getHeight()));
        sortDirectionPopup.show();
        sortIndicator.addClassName("cuba-sort-indicator-visible");
    }
}
