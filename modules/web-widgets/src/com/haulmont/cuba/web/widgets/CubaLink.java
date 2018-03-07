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

import com.haulmont.cuba.web.widgets.client.link.CubaLinkState;
import com.vaadin.ui.Link;


public class CubaLink extends Link {

    public void setRel(String rel) {
        getState(false).rel = rel;
    }

    public String getRel() {
        return getState(false).rel;
    }

    @Override
    protected CubaLinkState getState(boolean markAsDirty) {
        return (CubaLinkState) super.getState(markAsDirty);
    }

    @Override
    protected CubaLinkState getState() {
        return (CubaLinkState) super.getState();
    }
}