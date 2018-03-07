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

package com.haulmont.cuba.web.widgets.client.verticalmenu;

import com.google.gwt.dom.client.Element;
import com.haulmont.cuba.web.widgets.CubaSideMenu;
import com.vaadin.client.TooltipInfo;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import elemental.json.JsonArray;

import java.util.Map;

@Connect(CubaSideMenu.class)
public class CubaSideMenuConnector extends AbstractComponentConnector {

    public CubaSideMenuConnector() {
        registerRpc(CubaSideMenuClientRpc.class, new CubaSideMenuClientRpc() {
            @Override
            public void buildMenu(JsonArray menuItems) {
                getWidget().buildMenu(menuItems);
            }

            @Override
            public void selectItem(String itemId) {
                getWidget().selectItem(itemId);
            }

            @Override
            public void updateBadge(Map<String, String> badgeUpdates) {
                getWidget().updateBadges(badgeUpdates);
            }
        });
    }

    @Override
    public CubaSideMenuState getState() {
        return (CubaSideMenuState) super.getState();
    }

    @Override
    public CubaSideMenuWidget getWidget() {
        return (CubaSideMenuWidget) super.getWidget();
    }

    @Override
    protected void init() {
        super.init();

        getWidget().menuItemClickHandler = itemId ->
                getRpcProxy(CubaSideMenuServerRpc.class).menuItemTriggered(itemId);
        getWidget().menuItemIconSupplier = iconId -> {
            String resourceUrl = getResourceUrl(iconId);
            return getConnection().getIcon(resourceUrl);
        };
        getWidget().headerItemExpandHandler = (id, expanded) ->
                getRpcProxy(CubaSideMenuServerRpc.class).headerItemExpandChanged(id, expanded);
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("selectOnTrigger")) {
            getWidget().selectOnTrigger = getState().selectOnClick;
            getWidget().singleExpandedMenu = getState().singleExpandedMenu;
        }
    }

    @Override
    public boolean hasTooltip() {
        return true;
    }

    @Override
    public TooltipInfo getTooltipInfo(Element element) {
        String tooltip = getWidget().getTooltip(element);
        if (tooltip != null) {
            return new TooltipInfo(tooltip);
        }

        return super.getTooltipInfo(element);
    }
}