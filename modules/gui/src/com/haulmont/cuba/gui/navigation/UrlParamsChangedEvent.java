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

/**
 * Event sent when browser URL parameters of the opened screen are changed.
 *
 * @see #getParams()
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
