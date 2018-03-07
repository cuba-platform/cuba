/*
 * Licensed under the Apache License,Version2.0(the"License");you may not
 * use this file except in compliance with the License.You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.haulmont.cuba.web.widgets.client.renderers.componentrenderer.detailskeys;

import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.v7.client.widget.grid.events.BodyKeyDownHandler;
import com.vaadin.v7.client.widget.grid.events.GridKeyDownEvent;

/**
 * Handles the expansion and collapsing of the detailsrow with STRG+DOWN (expand) and STRG+UP (collapse).
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class DetailsKeyDownHandler implements BodyKeyDownHandler {

    private final DetailsOpenCloseServerRpc detailsRpc;

    public DetailsKeyDownHandler(DetailsOpenCloseServerRpc detailsRpc) {
        this.detailsRpc = detailsRpc;
    }

    @Override
    public void onKeyDown(GridKeyDownEvent keyEvent) {

        if (keyEvent.isControlKeyDown()) {

            if (keyEvent.getNativeKeyCode() == KeyCodes.KEY_DOWN) {

                int rowIndex = keyEvent.getFocusedCell().getRowIndex();
                detailsRpc.setDetailsVisible(rowIndex, true);

            } else if (keyEvent.getNativeKeyCode() == KeyCodes.KEY_UP) {

                int rowIndex = keyEvent.getFocusedCell().getRowIndex();
                detailsRpc.setDetailsVisible(rowIndex, false);

                // cell-focus is moved by grid even if event propagation + default are stopped.
                // To make use more intuitive when closing detailsrows also the details above the
                // focus are closed. Now you can flick open/close with keeping CTRL pressed and flicking
                // key-down and key-up.
                int rowAbove = rowIndex - 1;

                if (rowAbove >= 0) {
                    detailsRpc.setDetailsVisible(rowAbove, false);
                }

            }
        }

    }
}