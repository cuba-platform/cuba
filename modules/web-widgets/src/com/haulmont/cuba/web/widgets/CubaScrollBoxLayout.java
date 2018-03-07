/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.cubascrollboxlayout.CubaScrollBoxLayoutServerRpc;
import com.haulmont.cuba.web.widgets.client.cubascrollboxlayout.CubaScrollBoxLayoutState;

public class CubaScrollBoxLayout extends CubaCssActionsLayout {

    protected CubaScrollBoxLayoutServerRpc serverRpc;

    public CubaScrollBoxLayout() {
        serverRpc = new CubaScrollBoxLayoutServerRpc() {
            @Override
            public void setDeferredScroll(int scrollTop, int scrollLeft) {
                getState().scrollTop = scrollTop;
                getState().scrollLeft = scrollLeft;
            }

            @Override
            public void setDelayedScroll(int scrollTop, int scrollLeft) {
                getState().scrollTop = scrollTop;
                getState().scrollLeft = scrollLeft;
            }
        };
        registerRpc(serverRpc);
    }

    @Override
    protected CubaScrollBoxLayoutState getState() {
        return (CubaScrollBoxLayoutState) super.getState();
    }

    @Override
    protected CubaScrollBoxLayoutState getState(boolean markAsDirty) {
        return (CubaScrollBoxLayoutState) super.getState(markAsDirty);
    }

    public void setDelayed(boolean delayed) {
        getState().scrollChangeMode = delayed ?
                CubaScrollBoxLayoutState.DELAYED_MODE : CubaScrollBoxLayoutState.DEFERRED_MODE;
    }

    public void setScrollTop(int scrollTop) {
        if (getState(false).scrollTop != scrollTop) {
            getState().scrollTop = scrollTop;
        }
    }

    public void setScrollLeft(int scrollLeft) {
        if (getState(false).scrollLeft != scrollLeft) {
            getState().scrollLeft = scrollLeft;
        }
    }
}