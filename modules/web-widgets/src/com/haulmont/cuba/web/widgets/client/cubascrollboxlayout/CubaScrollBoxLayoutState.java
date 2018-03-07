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

package com.haulmont.cuba.web.widgets.client.cubascrollboxlayout;

import com.haulmont.cuba.web.widgets.client.cssactionslayout.CubaCssActionsLayoutState;

public class CubaScrollBoxLayoutState extends CubaCssActionsLayoutState {

    public static final String DELAYED_MODE = "DELAYED"; // send after 200ms delay
    public static final String DEFERRED_MODE = "DEFERRED"; // send only with changes from other components

    public int scrollTop = 0;
    public int scrollLeft = 0;

    public String scrollChangeMode = DEFERRED_MODE;
}