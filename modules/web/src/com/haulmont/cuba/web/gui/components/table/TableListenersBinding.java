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

package com.haulmont.cuba.web.gui.components.table;

import com.haulmont.bali.events.Subscription;

public class TableListenersBinding {
    private Subscription stateChangeEventSubscription;
    private Subscription valueChangeEventSubscription;
    private Subscription itemSetChangeEventSubscription;

    public Subscription getStateChangeEventSubscription() {
        return stateChangeEventSubscription;
    }

    public void setStateChangeEventSubscription(Subscription stateChangeEventSubscription) {
        this.stateChangeEventSubscription = stateChangeEventSubscription;
    }

    public Subscription getValueChangeEventSubscription() {
        return valueChangeEventSubscription;
    }

    public void setValueChangeEventSubscription(Subscription valueChangeEventSubscription) {
        this.valueChangeEventSubscription = valueChangeEventSubscription;
    }

    public Subscription getItemSetChangeEventSubscription() {
        return itemSetChangeEventSubscription;
    }

    public void setItemSetChangeEventSubscription(Subscription itemSetChangeEventSubscription) {
        this.itemSetChangeEventSubscription = itemSetChangeEventSubscription;
    }

    public void unbind() {
        if (stateChangeEventSubscription != null) {
            stateChangeEventSubscription.remove();
            stateChangeEventSubscription = null;
        }
        if (valueChangeEventSubscription != null) {
            valueChangeEventSubscription.remove();
            valueChangeEventSubscription = null;
        }
        if (itemSetChangeEventSubscription != null) {
            itemSetChangeEventSubscription.remove();
            itemSetChangeEventSubscription = null;
        }
    }
}