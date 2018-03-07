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

package com.haulmont.cuba.web.widgets.client.cssactionslayout;

import com.haulmont.cuba.web.widgets.CubaCssActionsLayout;
import com.haulmont.cuba.web.widgets.client.caption.CubaCaptionWidget;
import com.vaadin.client.*;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.csslayout.CssLayoutConnector;
import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.AbstractFieldState;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.MarginInfo;

@Connect(CubaCssActionsLayout.class)
public class CubaCssActionsLayoutConnector extends CssLayoutConnector implements Paintable {

    @Override
    public CubaCssActionsLayoutWidget getWidget() {
        return (CubaCssActionsLayoutWidget) super.getWidget();
    }

    @Override
    public CubaCssActionsLayoutState getState() {
        return (CubaCssActionsLayoutState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setMargin(new MarginInfo(getState().marginsBitmask));
        getWidget().setSpacing(getState().spacing);
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        final int cnt = uidl.getChildCount();
        for (int i = 0; i < cnt; i++) {
            UIDL childUidl = uidl.getChildUIDL(i);
            if (childUidl.getTag().equals("actions")) {
                if (getWidget().getShortcutHandler() == null) {
                    getWidget().setShortcutHandler(new ShortcutActionHandler(uidl.getId(), client));
                }
                getWidget().getShortcutHandler().updateActionMap(childUidl);
            }
        }
    }

    @Override
    protected VCaption createCaption(ComponentConnector child) {
        return new CubaCaptionWidget(child, getConnection());
    }

    @Override
    protected boolean isCaptionNeeded(ComponentConnector child) {
        AbstractComponentState state = child.getState();
        return super.isCaptionNeeded(child) || (state instanceof AbstractFieldState
                && ((AbstractFieldState) state).contextHelpText != null
                && !((AbstractFieldState) state).contextHelpText.isEmpty());
    }
}