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

package com.haulmont.cuba.web.toolkit.ui.client.split;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.haulmont.cuba.web.toolkit.ui.client.placeholder.CubaPlaceHolderWidget;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.ui.VSplitPanelHorizontal;

public class CubaHorizontalSplitPanelWidget extends VSplitPanelHorizontal {

    protected enum DockButtonState {
        LEFT,
        RIGHT
    }

    protected DockButtonState dockButtonState = DockButtonState.LEFT;

    // todo artamonov implement SplitPanelDockMode.RIGHT
    protected SplitPanelDockMode dockMode = SplitPanelDockMode.LEFT;

    protected String defaultPosition = null;

    private VOverlay dockButtonContainer;
    private CubaPlaceHolderWidget dockButton;

    public boolean isDockable() {
        return dockButtonContainer != null;
    }

    public void setDockable(boolean dockable) {
        if (isDockable() == dockable) {
            return;
        }

        if (dockable) {
            dockButton = new CubaPlaceHolderWidget();
            dockButton.setStyleName("cuba-splitpanel-dock-button");
            dockButton.addStyleName("cuba-splitpanel-dock-button-left");
            dockButton.addDomHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    onDockButtonClick();
                }
            }, ClickEvent.getType());

            dockButtonContainer = new VOverlay();
            dockButtonContainer.addStyleName("cuba-splitpanel-dock-overlay");
            dockButtonContainer.getElement().getStyle().setZIndex(9999);

            if (dockMode == SplitPanelDockMode.LEFT) {
                dockButtonContainer.setStyleName("cuba-splitpanel-dock-left");
            } else {
                dockButtonContainer.setStyleName("cuba-splitpanel-dock-right");
            }

            dockButtonContainer.setOwner(this);
            dockButtonContainer.setWidget(dockButton);
            dockButtonContainer.show();

            updateDockButtonPosition();
        } else {
            if (dockButtonContainer != null) {
                dockButtonContainer.hide();
                dockButtonContainer.removeFromParent();

                dockButtonContainer = null;
                dockButton = null;
            }
        }
    }

    private void onDockButtonClick() {
        String newPosition = position;

        if (dockMode == SplitPanelDockMode.LEFT) {
            if (dockButtonState == DockButtonState.LEFT) {
                defaultPosition = position;
                newPosition = "0px";
            } else if (defaultPosition != null) {
                newPosition = defaultPosition;
            }
        } // todo artamonov SplitPanelDockMode.RIGHT

        setSplitPosition(newPosition);
        fireEvent(new SplitterMoveHandler.SplitterMoveEvent(this));
    }

    public void setDockMode(SplitPanelDockMode dockMode) {
        this.dockMode = dockMode;

        updateDockButtonPosition();
    }

    protected void updateDockButtonPosition() {
        if (isDockable()) {

            if (dockMode == SplitPanelDockMode.LEFT) {
                int left = splitter.getAbsoluteLeft();

                if (left > 20) {
                    dockButtonContainer.setPopupPosition(
                            left - (dockButton.getOffsetWidth() - getSplitterSize()),
                            splitter.getAbsoluteTop() + splitter.getOffsetHeight() / 2 - dockButton.getOffsetHeight() / 2);

                    if (dockButtonState == DockButtonState.RIGHT) {
                        dockButton.removeStyleName("cuba-splitpanel-dock-button-right");
                        dockButton.addStyleName("cuba-splitpanel-dock-button-left");
                        dockButtonState = DockButtonState.LEFT;
                    }

                    this.removeStyleName("cuba-splitpanel-dockable-right");
                    this.addStyleName("cuba-splitpanel-dockable-left");
                } else {
                    dockButtonContainer.setPopupPosition(
                            left,
                            splitter.getAbsoluteTop() + splitter.getOffsetHeight() / 2 - dockButton.getOffsetHeight() / 2);

                    if (dockButtonState == DockButtonState.LEFT) {
                        dockButton.removeStyleName("cuba-splitpanel-dock-button-left");
                        dockButton.addStyleName("cuba-splitpanel-dock-button-right");
                        dockButtonState = DockButtonState.RIGHT;
                    }

                    this.removeStyleName("cuba-splitpanel-dockable-left");
                    this.addStyleName("cuba-splitpanel-dockable-right");
                }
            } // todo artamonov SplitPanelDockMode.RIGHT
        }
    }

    @Override
    public void updateSizes() {
        super.updateSizes();

        if (isAttached()) {
            updateDockButtonPosition();
        }
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        if (dockButtonContainer != null) {
            dockButtonContainer.hide();
        }
    }
}