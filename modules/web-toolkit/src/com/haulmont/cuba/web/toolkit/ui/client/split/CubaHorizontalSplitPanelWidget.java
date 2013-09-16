/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.split;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.haulmont.cuba.web.toolkit.ui.client.placeholder.CubaPlaceHolderWidget;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.ui.VSplitPanelHorizontal;

/**
 * @author artamonov
 * @version $Id$
 */
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
            DOM.setStyleAttribute(dockButtonContainer.getElement(), "zIndex", "" + 9999);

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
        if (dockMode == SplitPanelDockMode.LEFT) {
            if (dockButtonState == DockButtonState.LEFT) {
                defaultPosition = position;
                position = "0px";
            } else if (defaultPosition != null) {
                position = defaultPosition;
            }
        } // todo artamonov SplitPanelDockMode.RIGHT

        setSplitPosition(position);
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