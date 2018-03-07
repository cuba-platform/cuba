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

package com.haulmont.cuba.web.widgets.client.renderers.componentrenderer.componentcellkey;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.v7.client.widget.grid.CellReference;
import com.vaadin.v7.client.widget.grid.events.BodyKeyDownHandler;
import com.vaadin.v7.client.widget.grid.events.GridKeyDownEvent;

/**
 * Handles pressing the ENTER key by setting the focus to the
 * component inside the cell.
 *
 * If the component contains an input field, the input field is
 * focused, so the value can be changed right away.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class EnterKeyDownHandler implements BodyKeyDownHandler {

    @Override
    public void onKeyDown(GridKeyDownEvent event) {

        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {


            if (isCellContainingComponent(event.getFocusedCell())) {

                // if the ENTER key-event's propagation is not stopped it would
                // be propagated to the newly focused component, which e.g. in the
                // case of a button leads to an immediate button press
                event.preventDefault();
                event.stopPropagation();

                Element componentElement = extractComponentElement(event.getFocusedCell());

                WidgetUtil.focus(componentElement);
                focusInputField(componentElement);

            }

        }


    }

    private void focusInputField(Element componentElement) {
        for (int i = 0; i < componentElement.getChildNodes().getLength(); i++) {
            Node node = componentElement.getChildNodes().getItem(i);
            if (node.getNodeName().equals("INPUT") || node.getNodeName().equals("SELECT")) {
                WidgetUtil.focus((Element) node);
                break;
            }
        }
    }

    private Element extractComponentElement(CellReference cell) {
        return ((AbstractComponentConnector) cell.getValue()).getWidget().getElement();
    }

    private boolean isCellContainingComponent(CellReference cell) {
        return cell.getValue() instanceof AbstractComponentConnector;
    }
}
