/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.split;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.widgets.client.placeholder.CubaPlaceHolderWidget;
import com.vaadin.client.ui.VSplitPanelVertical;

import java.util.function.Consumer;

public class CubaVerticalSplitPanelWidget extends VSplitPanelVertical {

    /**
     * Styles for widget
     */
    protected static final String SP_DOCK_BUTTON = "c-splitpanel-dock-button-vertical";
    protected static final String SP_DOCK_BUTTON_UP = "c-splitpanel-dock-button-up";
    protected static final String SP_DOCK_BUTTON_DOWN = "c-splitpanel-dock-button-down";
    protected static final String SP_DOCK_UP = "c-splitpanel-dock-up";
    protected static final String SP_DOCK_DOWN = "c-splitpanel-dock-down";
    protected static final String SP_DOCKABLE_UP = "c-splitpanel-dockable-up";
    protected static final String SP_DOCKABLE_DOWN = "c-splitpanel-dockable-down";

    protected static final int BUTTON_HEIGHT_SPACE = 12;
    protected boolean reversed;
    protected boolean docked;

    protected int splitWidth;

    protected enum DockButtonState {
        UP,
        DOWN
    }

    protected DockButtonState dockButtonState = DockButtonState.UP;

    protected SplitPanelDockMode dockMode = SplitPanelDockMode.TOP;

    protected Consumer<String> beforeDockPositionHandler = null;

    protected String defaultPosition = null;
    protected String beforeDockPosition = null;

    private Element dockButtonContainer;
    private CubaPlaceHolderWidget dockButton;

    public boolean isDockable() {
        return dockButtonContainer != null;
    }

    public void setDockable(boolean dockable) {
        if (isDockable() == dockable) {
            return;
        }

        if (dockable) {
            dockButton = createDockButton();
            dockButtonContainer = createDockButtonContainer();

            add(dockButton, dockButtonContainer);
            splitter.getParentElement().appendChild(dockButtonContainer);

            updateDockButtonPosition();
        } else {
            if (dockButtonContainer != null) {
                dockButtonContainer.removeFromParent();

                dockButtonContainer = null;
                dockButton = null;
            }
        }
    }

    protected CubaPlaceHolderWidget createDockButton() {
        CubaPlaceHolderWidget dockBtn = new CubaPlaceHolderWidget();
        dockBtn.setStyleName(SP_DOCK_BUTTON);
        if (dockMode == SplitPanelDockMode.TOP) {
            dockBtn.addStyleName(SP_DOCK_BUTTON_UP);
        } else {
            dockBtn.addStyleName(SP_DOCK_BUTTON_DOWN);
        }
        dockBtn.addDomHandler(
                event -> onDockButtonClick(),
                ClickEvent.getType());
        return dockBtn;
    }

    protected Element createDockButtonContainer() {
        Element dockBtnContainer = DOM.createDiv();
        dockBtnContainer.getStyle().setZIndex(101);
        dockBtnContainer.getStyle().setPosition(Style.Position.ABSOLUTE);

        if (dockMode == SplitPanelDockMode.TOP) {
            dockBtnContainer.addClassName(SP_DOCK_UP);
        } else if (dockMode == SplitPanelDockMode.BOTTOM) {
            dockBtnContainer.addClassName(SP_DOCK_DOWN);
        }
        return dockBtnContainer;
    }

    private void onDockButtonClick() {
        String newPosition = position;
        boolean isDocked = false;

        if (dockMode == SplitPanelDockMode.TOP) {
            if (dockButtonState == DockButtonState.UP) {
                defaultPosition = position;
                newPosition = "0px";
                isDocked = true;
            } else if (defaultPosition != null) {
                newPosition = defaultPosition;
            } else if (beforeDockPosition != null) {
                // apply last saved position if defaultPosition is null
                newPosition = beforeDockPosition;
            } else if (isSplitterInBottomChangeArea()) {
                // splitter is placed in the absolute UP position and if we click on the dock button
                // it won't be replaced, because of defaultPosition and beforeDockPosition are null.
                // So we replace it to the absolute DOWN position.
                defaultPosition = position;
                newPosition = reversed ? "0px" : getAbsoluteBottom() + "px";
                isDocked = true;
            }
        } else if (dockMode == SplitPanelDockMode.BOTTOM) {
            if (dockButtonState == DockButtonState.DOWN) {
                defaultPosition = position;
                newPosition = reversed ? "0px" : getAbsoluteBottom() + "px";
                isDocked = true;
            } else if (defaultPosition != null) {
                newPosition = defaultPosition;
            } else if (beforeDockPosition != null) {
                // apply last saved position if defaultPosition is null
                newPosition = beforeDockPosition;
            } else if (isSplitterInTopChangeArea()) {
                // splitter is placed in the absolute DOWN position and if we click on the dock button
                // it won't be replaced, because of defaultPosition and beforeDockPosition are null.
                // So we replace it to the absolute UP position.
                defaultPosition = position;
                newPosition = "0px";
                isDocked = true;
            }
        }

        // save last position before dock changes position
        beforeDockPositionHandler.accept(defaultPosition);
        setDocked(isDocked);
        setSplitPosition(newPosition);
        fireEvent(new SplitterMoveHandler.SplitterMoveEvent(this));
    }

    public void setDockMode(SplitPanelDockMode dockMode) {
        this.dockMode = dockMode;

        updateDockButtonPosition();
    }

    protected void updateDockButtonPosition() {
        if (isDockable()) {

            Style dockButtonStyle = dockButtonContainer.getStyle();
            if (dockMode == SplitPanelDockMode.TOP) {
                int top = splitter.getOffsetTop();
                if (top > BUTTON_HEIGHT_SPACE) {
                    dockButtonStyle.setTop(top - (dockButton.getOffsetHeight() - getSplitterSize()), Style.Unit.PX);
                    dockButtonStyle.setLeft(getDockBtnContainerHorizontalPosition(), Style.Unit.PX);

                    if (dockButtonState == DockButtonState.DOWN) {
                        updateDockButtonStyle(SP_DOCK_BUTTON_UP, SP_DOCK_BUTTON_DOWN);
                        dockButtonState = DockButtonState.UP;
                    }

                    updateSplitPanelStyle(SP_DOCKABLE_UP, SP_DOCKABLE_DOWN);
                } else {
                    dockButtonStyle.setTop(top, Style.Unit.PX);
                    dockButtonStyle.setLeft(getDockBtnContainerHorizontalPosition(), Style.Unit.PX);

                    if (dockButtonState == DockButtonState.UP) {
                        updateDockButtonStyle(SP_DOCK_BUTTON_DOWN, SP_DOCK_BUTTON_UP);
                        dockButtonState = DockButtonState.DOWN;
                    }

                    updateSplitPanelStyle(SP_DOCKABLE_DOWN, SP_DOCKABLE_UP);
                }
            } else if (dockMode == SplitPanelDockMode.BOTTOM) {
                int down = splitter.getOffsetTop() + splitter.getOffsetHeight();
                int splitBottomPosition = getAbsoluteBottom();

                if (down < splitBottomPosition - BUTTON_HEIGHT_SPACE) {
                    dockButtonStyle.setTop(down - getSplitterSize(), Style.Unit.PX);
                    dockButtonStyle.setLeft(getDockBtnContainerHorizontalPosition(), Style.Unit.PX);

                    if (dockButtonState == DockButtonState.UP) {
                        updateDockButtonStyle(SP_DOCK_BUTTON_DOWN, SP_DOCK_BUTTON_UP);
                        dockButtonState = DockButtonState.DOWN;
                    }

                    updateSplitPanelStyle(SP_DOCKABLE_DOWN, SP_DOCKABLE_UP);
                } else {
                    dockButtonStyle.setTop(down - (dockButton.getOffsetHeight()), Style.Unit.PX);
                    dockButtonStyle.setLeft(getDockBtnContainerHorizontalPosition(), Style.Unit.PX);

                    if (dockButtonState == DockButtonState.DOWN) {
                        updateDockButtonStyle(SP_DOCK_BUTTON_UP, SP_DOCK_BUTTON_DOWN);
                        dockButtonState = DockButtonState.UP;
                    }

                    updateSplitPanelStyle(SP_DOCKABLE_UP, SP_DOCKABLE_DOWN);
                }
            }
        }
    }

    protected boolean isSplitterInBottomChangeArea() {
        int left = splitter.getOffsetLeft();
        return left < getAbsoluteBottom() + BUTTON_HEIGHT_SPACE;
    }

    protected boolean isSplitterInTopChangeArea() {
        int bottom = splitter.getOffsetTop() + splitter.getOffsetHeight();
        int splitBottomPosition = getAbsoluteTop() + getAbsoluteBottom();
        return bottom > splitBottomPosition - BUTTON_HEIGHT_SPACE;
    }

    private void updateDockButtonStyle(String newStyle, String oldStyle) {
        dockButton.removeStyleName(oldStyle);
        dockButton.addStyleName(newStyle);
    }

    private void updateSplitPanelStyle(String newStyle, String oldStyle) {
        this.removeStyleName(oldStyle);
        this.addStyleName(newStyle);
    }

    private int getDockBtnContainerHorizontalPosition() {
        return splitWidth / 2 - dockButton.getOffsetWidth() / 2;
    }

    public int getAbsoluteBottom() {
        return getOffsetHeight();
    }

    @Override
    public void updateSizes() {
        super.updateSizes();

        splitWidth = splitter.getOffsetWidth();

        if (isAttached()) {
            updateDockButtonPosition();
        }
    }

    @Override
    public void onMouseDown(Event event) {
        if (isDockable()
                && isDocked()
                && (isMinPositionSet() || isMaxPositionSet())) {
            return;
        }
        super.onMouseDown(event);
    }

    @Override
    public void setSplitPosition(String pos) {
        if (isDockable()
                && isDocked()
                && !isExtremePosition(pos)) {
            setDocked(false);
        }
        super.setSplitPosition(pos);
    }

    protected boolean isMinPositionSet() {
        return dockMode == SplitPanelDockMode.TOP
                && !minimumPosition.equals("0%")
                && !minimumPosition.equals("0px");
    }

    protected boolean isMaxPositionSet() {
        return dockMode == SplitPanelDockMode.BOTTOM
                && !maximumPosition.equals("100%")
                && !maximumPosition.equals(getAbsoluteBottom() + "px");
    }

    protected boolean isExtremePosition(String pos) {
        boolean dockedToLeft = dockMode == SplitPanelDockMode.TOP
                && (pos.equals("0%") || pos.equals("0px"));
        boolean dockedToRight = dockMode == SplitPanelDockMode.BOTTOM
                && (pos.equals("100%") || pos.equals(getAbsoluteBottom() + "px"));
        return dockedToLeft || dockedToRight;
    }

    @Override
    public void onMouseUp(Event event) {
        super.onMouseUp(event);

        splitWidth = splitter.getOffsetWidth();
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        if (dockButtonContainer != null) {
            dockButtonContainer.removeFromParent();
        }
    }

    @Override
    protected String checkSplitPositionLimits(String pos) {
        return isDocked() ? pos : super.checkSplitPositionLimits(pos);
    }

    @Override
    public void setPositionReversed(boolean reversed) {
        super.setPositionReversed(reversed);

        this.reversed = reversed;
    }

    protected void setDocked(boolean docked) {
        if (this.docked != docked) {
            this.docked = docked;
        }
    }

    protected boolean isDocked() {
        return docked;
    }
}
