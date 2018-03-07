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

package com.haulmont.cuba.web.widgets.client.renderers.componentrenderer.focuspreserve;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.WidgetUtil;

/**
 * Provides rpc-methods to save and restore the current focus of the grid. This
 * is needed to preserve the current focus when issuing a full rerendering of
 * the grid.
 *
 * <ul>
 *     <li>save the current focus using {@link #saveFocus()}</li>
 *     <li>rerender the grid</li>
 *     <li>restore the current focus using {@link #restoreFocus()}</li>
 * </ul>
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class FocusPreservingRefreshClientRpcImpl implements FocusPreservingRefreshClientRpc {

    private Element focus = null;

    @Override
    public void saveFocus() {
        SimplePanel panel = WidgetUtil.findWidget(WidgetUtil.getFocusedElement(), SimplePanel.class);

        if (panel != null) {
            focus = panel.getParent().getElement();
        }
    }

    @Override
    public void restoreFocus() {
        WidgetUtil.focus(focus);

    }
}
