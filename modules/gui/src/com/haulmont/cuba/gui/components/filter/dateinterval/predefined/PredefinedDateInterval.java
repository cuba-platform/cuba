/*
 * Copyright (c) 2008-2016 Haulmont.
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

package com.haulmont.cuba.gui.components.filter.dateinterval.predefined;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

/**
 * Parent class for predefined date intervals that are used by the "In interval" date condition of the generic filter
 * component
 */
public abstract class PredefinedDateInterval {

    public PredefinedDateInterval(String name) {
        this.name = name;
    }

    protected String name;

    public String getName() {
        return name;
    }

    public String getLocalizedCaption() {
        return AppBeans.get(Messages.class).getMessage(getClass(), name);
    }

    public abstract String getJPQL(String propertyName);
}
