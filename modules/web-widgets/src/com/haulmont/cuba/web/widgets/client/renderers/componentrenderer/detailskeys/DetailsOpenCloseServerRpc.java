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

import com.vaadin.shared.communication.ServerRpc;

/**
 * Handles the expansion and collapsing of the detailsrow. It is necessary
 * to do that on server-side to keep the details-states of client and server-side
 * synced.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public interface DetailsOpenCloseServerRpc extends ServerRpc {

    /**
     * Sets the details of a row visible or hidden.
     *
     * @param rowIndex the rowIndex of the row, this is the rowIndex of an IndexedContainer,
     *                 this is NOT the rowId (= id in the container) or the
     *                 rowKey (= internal key in clienside grid implementation)
     * @param visible set the details to visible (= true) or hidden (= false)
     */
    void setDetailsVisible(int rowIndex, boolean visible);
}