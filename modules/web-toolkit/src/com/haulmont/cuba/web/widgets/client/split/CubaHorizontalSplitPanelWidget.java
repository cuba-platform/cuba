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

package com.haulmont.cuba.web.widgets.client.split;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.widgets.client.placeholder.CubaPlaceHolderWidget;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.ui.VSplitPanelHorizontal;

public class CubaHorizontalSplitPanelWidget extends VSplitPanelHorizontal {
    /**
     * Styles for widget
     */
    protected static final String SP_DOCK_BUTTON = "c-splitpanel-dock-button";
    protected static final String SP_DOCK_BUTTON_LEFT = "c-splitpanel-dock-button-left";
    protected static final String SP_DOCK_BUTTON_RIGHT = "c-splitpanel-dock-button-right";
    protected static final String SP_DOCK_OVERLAY = "c-splitpanel-dock-overlay";
    protected static final String SP_DOCK_LEFT = "c-splitpanel-dock-left";
    protected static final String SP_DOCK_RIGHT = "c-splitpanel-dock-right";
    protected static final String SP_DOCKABLE_LEFT = "c-splitpanel-dockable-left";
    protected static final String SP_DOCKABLE_RIGHT = "c-splitpanel-dockable-right";

    protected static final int BUTTON_WIDTH_SPACE = 20;
    protected boolean reversed;

    protected int splitHeight;
    protected int splitTop;

    protected enum DockButtonState {
        LEFT,
        RIGHT
    }

    protected DockButtonState dockButtonState = DockButtonState.LEFT;

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
            dockButton.setStyleName(SP_DOCK_BUTTON);
            dockButton.addStyleName(SP_DOCK_BUTTON_LEFT);
            dockButton.addDomHandler(
                    event -> onDockButtonClick(),
                    ClickEvent.getType());

            dockButtonContainer = new VOverlay();
            dockButtonContainer.addStyleName(SP_DOCK_OVERLAY);
            dockButtonContainer.getElement().getStyle().setZIndex(9999);

            if (dockMode == SplitPanelDockMode.LEFT) {
                dockButtonContainer.setStyleName(SP_DOCK_LEFT);
            } else if (dockMode == SplitPanelDockMode.RIGHT) {
                dockButtonContainer.setStyleName(SP_DOCK_RIGHT);
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
        } else if (dockMode == SplitPanelDockMode.RIGHT) {
            if (dockButtonState == DockButtonState.RIGHT) {
                defaultPosition = position;
                newPosition = reversed ? "0px" : getAbsoluteRight() + "px";
            } else if (defaultPosition != null) {
                newPosition = defaultPosition;
            }
        }

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

                if (left > BUTTON_WIDTH_SPACE) {
                    dockButtonContainer.setPopupPosition(
                            left - (dockButton.getOffsetWidth() - getSplitterSize()),
                            getDockBtnContainerVerticalPosition());

                    if (dockButtonState == DockButtonState.RIGHT) {
                        updateDockButtonStyle(SP_DOCK_BUTTON_LEFT, SP_DOCK_BUTTON_RIGHT);
                        dockButtonState = DockButtonState.LEFT;
                    }

                    updateSplitPanelStyle(SP_DOCKABLE_LEFT, SP_DOCKABLE_RIGHT);
                } else {
                    dockButtonContainer.setPopupPosition(
                            left,
                            getDockBtnContainerVerticalPosition());

                    if (dockButtonState == DockButtonState.LEFT) {
                        updateDockButtonStyle(SP_DOCK_BUTTON_RIGHT, SP_DOCK_BUTTON_LEFT);
                        dockButtonState = DockButtonState.RIGHT;
                    }

                    updateSplitPanelStyle(SP_DOCKABLE_RIGHT, SP_DOCKABLE_LEFT);
                }
            } else if (dockMode == SplitPanelDockMode.RIGHT) {
                int right = splitter.getAbsoluteRight();

                if (right < getAbsoluteRight() - BUTTON_WIDTH_SPACE) {
                    dockButtonContainer.setPopupPosition(
                            right - getSplitterSize(),
                            getDockBtnContainerVerticalPosition());

                    if (dockButtonState == DockButtonState.LEFT) {
                        updateDockButtonStyle(SP_DOCK_BUTTON_RIGHT, SP_DOCK_BUTTON_LEFT);
                        dockButtonState = DockButtonState.RIGHT;
                    }

                    updateSplitPanelStyle(SP_DOCKABLE_RIGHT, SP_DOCKABLE_LEFT);
                } else {
                    dockButtonContainer.setPopupPosition(
                            right - (dockButton.getOffsetWidth()),
                            getDockBtnContainerVerticalPosition());

                    if (dockButtonState == DockButtonState.RIGHT) {
                        updateDockButtonStyle(SP_DOCK_BUTTON_LEFT, SP_DOCK_BUTTON_RIGHT);
                        dockButtonState = DockButtonState.LEFT;
                    }

                    updateSplitPanelStyle(SP_DOCKABLE_LEFT, SP_DOCKABLE_RIGHT);
                }
            }
        }
    }

    private void updateDockButtonStyle(String newStyle, String oldStyle) {
        dockButton.removeStyleName(oldStyle);
        dockButton.addStyleName(newStyle);
    }

    private void updateSplitPanelStyle(String newStyle, String oldStyle) {
        this.removeStyleName(oldStyle);
        this.addStyleName(newStyle);
    }

    private int getDockBtnContainerVerticalPosition() {
        return splitTop + splitHeight / 2 - dockButton.getOffsetHeight() / 2;
    }

    public int getAbsoluteRight() {
        return getOffsetWidth();
    }

    @Override
    public void updateSizes() {
        super.updateSizes();

        splitHeight = splitter.getOffsetHeight();
        if (splitTop == 0) {
            splitTop = splitter.getAbsoluteTop();
        }

        if (isAttached()) {
            updateDockButtonPosition();
        }
    }

    @Override
    public void onMouseUp(Event event) {
        super.onMouseUp(event);

        splitHeight = splitter.getOffsetHeight();
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        if (dockButtonContainer != null) {
            dockButtonContainer.hide();
        }
    }

    @Override
    public void setPositionReversed(boolean reversed) {
        super.setPositionReversed(reversed);

        this.reversed = reversed;
    }
}