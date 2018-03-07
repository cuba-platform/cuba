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

import com.haulmont.cuba.web.widgets.client.renderers.componentrenderer.detailskeys.DetailsOpenCloseServerRpc;
import com.vaadin.v7.ui.Grid;

/**
 * Handles the expansion and collapsing of the detailsrow with STRG+DOWN (expand) and STRG+UP (collapse).
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class DetailsKeysExtension extends Grid.AbstractGridExtension {

    private DetailsKeysExtension(final Grid grid) {
        super.extend(grid);
        registerRpc(new DetailsOpenCloseServerRpcImpl(grid));
    }

    public static DetailsKeysExtension extend(Grid grid) {
        return new DetailsKeysExtension(grid);
    }

    public static class DetailsOpenCloseServerRpcImpl implements DetailsOpenCloseServerRpc {
        private final Grid grid;

        public DetailsOpenCloseServerRpcImpl(Grid grid) {
            this.grid = grid;
        }

        @Override
        public void setDetailsVisible(int rowIndex, boolean visible) {
            Object itemId = grid.getContainerDataSource().getItemIds(rowIndex, 1).get(0);
            grid.setDetailsVisible(itemId, visible);
        }
    }
}
