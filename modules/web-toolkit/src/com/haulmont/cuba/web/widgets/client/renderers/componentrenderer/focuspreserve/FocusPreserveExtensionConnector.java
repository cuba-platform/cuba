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

import com.haulmont.cuba.web.widgets.renderers.componentrenderer.FocusPreserveExtension;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Provides rpc-methods to save and restore the current focus of the grid. This
 * is needed to preserve the current focus when issuing a full rerendering of
 * the grid.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
@Connect(FocusPreserveExtension.class)
public class FocusPreserveExtensionConnector extends AbstractExtensionConnector {

    @Override
    protected void extend(ServerConnector target) {
        registerRpc(FocusPreservingRefreshClientRpc.class, new FocusPreservingRefreshClientRpcImpl());
    }
}
