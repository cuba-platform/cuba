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

package com.haulmont.cuba.web.widgets.client.scrollboxlayout;

import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.haulmont.cuba.web.widgets.client.cssactionslayout.CubaCssActionsLayoutWidget;
import com.haulmont.cuba.web.widgets.client.cubascrollboxlayout.CubaScrollBoxLayoutState;

import java.util.function.BiConsumer;

public class CubaScrollBoxLayoutWidget extends CubaCssActionsLayoutWidget {

    private static final int TIMEOUT = 250;

    protected int scrollTop = 0;
    protected int scrollLeft = 0;

    public BiConsumer<Integer, Integer> onScrollHandler;

    protected String scrollChangeMode;

    protected Timer scrollBoxStateTrigger = new Timer() {

        @Override
        public void run() {
            if (isAttached()) {
                updateScrollState();
            }
        }
    };

    protected void updateScrollState() {
        if (onScrollHandler != null) {
            onScrollHandler.accept(scrollTop, scrollLeft);
        }
    }

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

                this.scrollTop = scrollTop;
                this.scrollLeft = scrollLeft;

                if (CubaScrollBoxLayoutState.DEFERRED_MODE.equals(scrollChangeMode)) {
                    updateScrollState();
                } else {
                    scrollBoxStateTrigger.cancel();
                    scrollBoxStateTrigger.schedule(TIMEOUT);
                }
            }
        }
    }

    public void setScrollChangeMode(String scrollChangeMode) {
        this.scrollChangeMode = scrollChangeMode;
    }

    public void setScrollTop(int scrollTop) {
        this.scrollTop = scrollTop;

        scrollBoxStateTrigger.cancel();
        getElement().setScrollTop(scrollTop);
    }

    public void setScrollLeft(int scrollLeft) {
        this.scrollLeft = scrollLeft;

        scrollBoxStateTrigger.cancel();
        getElement().setScrollLeft(scrollLeft);
    }
}