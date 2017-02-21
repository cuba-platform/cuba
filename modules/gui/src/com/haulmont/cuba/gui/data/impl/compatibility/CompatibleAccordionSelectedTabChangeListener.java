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

package com.haulmont.cuba.gui.data.impl.compatibility;

import com.haulmont.cuba.gui.components.Accordion;

public class CompatibleAccordionSelectedTabChangeListener implements Accordion.SelectedTabChangeListener {

    private final Accordion.TabChangeListener listener;

    public CompatibleAccordionSelectedTabChangeListener(Accordion.TabChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void selectedTabChanged(Accordion.SelectedTabChangeEvent event) {
        listener.tabChanged(event.getSelectedTab());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CompatibleAccordionSelectedTabChangeListener that = (CompatibleAccordionSelectedTabChangeListener) obj;

        return this.listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }
}