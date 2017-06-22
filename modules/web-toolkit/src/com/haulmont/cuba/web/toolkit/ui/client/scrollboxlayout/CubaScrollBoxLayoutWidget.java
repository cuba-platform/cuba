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

package com.haulmont.cuba.web.toolkit.ui.client.scrollboxlayout;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.toolkit.ui.client.cssactionslayout.CubaCssActionsLayoutWidget;

import java.util.function.BiConsumer;

public class CubaScrollBoxLayoutWidget extends CubaCssActionsLayoutWidget {

    protected int scrollTop = 0;
    protected int scrollLeft = 0;

    public BiConsumer<Integer, Integer> onScrollHandler;

    protected CubaScrollBoxLayoutWidget() {
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN | Event.ONSCROLL);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        if (DOM.eventGetType(event) == Event.ONSCROLL) {
            Element element = getElement();

            int scrollTop = element.getScrollTop();
            int scrollLeft = element.getScrollLeft();

            if (this.scrollTop != scrollTop || this.scrollLeft != scrollLeft) {
                if (onScrollHandler != null) {
                    onScrollHandler.accept(scrollTop, scrollLeft);
                }

                this.scrollTop = scrollTop;
                this.scrollLeft = scrollLeft;
            }
        }
    }
}