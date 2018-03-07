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

package com.haulmont.cuba.web.widgets.renderers.componentrenderer;

import com.haulmont.cuba.web.widgets.client.renderers.componentrenderer.focuspreserve.FocusPreservingRefreshClientRpc;
import com.vaadin.v7.ui.Grid;

/**
 * Provides rpc-methods to save and restore the current focus of the grid. This
 * is needed to preserve the current focus when issuing a full rerendering of
 * the grid.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class FocusPreserveExtension extends Grid.AbstractGridExtension {
    private final FocusPreservingRefreshClientRpc focusRpc = getRpcProxy(FocusPreservingRefreshClientRpc.class);

    private FocusPreserveExtension(final Grid grid) {
        super.extend(grid);
    }

    public static FocusPreserveExtension extend(Grid grid) {
        return new FocusPreserveExtension(grid);
    }

    /**
     * Saves the grid's current focus in this extension's internal state.
     */
    public void saveFocus() {
        focusRpc.saveFocus();
    }

    /**
     * Restores the grid's focus from  extension's internal state.
     */
    public void restoreFocus() {
        focusRpc.restoreFocus();
    }
}
