/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.navigation;

import com.haulmont.cuba.gui.screen.Screen;

import java.util.EventObject;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Event sent when browser URL parameters corresponding to opened screen are changed. It is fired before the screen is
 * shown, which enables to do some preparatory work.
 * <p>
 * In this event listener, you can load some data or change screen controls state depending on new parameters:
 * <pre>
 *     &#64;Subscribe
 *     protected void onUrlParamsChanged(UrlParamsChangedEvent event) {
 *         Map&lt;String, String&gt; params = event.getParams();
 *         // handle new params
 *     }
 * </pre>
 */
public class UrlParamsChangedEvent extends EventObject {

    protected final Map<String, String> params;

    public UrlParamsChangedEvent(Screen source, Map<String, String> params) {
        super(source);
        this.params = params;
    }

    @Override
    public Screen getSource() {
        return (Screen) super.getSource();
    }

    /**
     * @return current URL parameters
     */
    public Map<String, String> getParams() {
        return params;
    }
}
