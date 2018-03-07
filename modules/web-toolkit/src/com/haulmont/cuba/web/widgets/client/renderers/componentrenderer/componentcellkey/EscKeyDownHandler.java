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

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.WidgetUtil;

/**
 * Handles pressing the ESC key by setting the focus to the
 * cell whiches component holds the current focus.
 *
 * This enables the user to go on navigating through the grid
 * using the arrow keys which would not work if the current
 * focus was located e.g. in a ComboBox.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class EscKeyDownHandler implements KeyDownHandler {
    @Override
    public void onKeyDown(KeyDownEvent keyDownEvent) {

        if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {

            SimplePanel panel = WidgetUtil.findWidget(WidgetUtil.getFocusedElement(), SimplePanel.class);

            if (panel != null) {
                WidgetUtil.focus(panel.getParent().getElement());

                // prevent further bubbling of the event as it has only
                // navigational purpose if thrown at this depth. A subsequent
                // press of ESC is not stopped, so ESC abort actions of the
                // application should work as soon as the focus is in
                // "navigational mode" on a grid-cell
                keyDownEvent.preventDefault();
                keyDownEvent.stopPropagation();
            }
        }
    }
}
