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
 *
 */
package com.haulmont.cuba.core.global;

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Defines a {@link View} property. Each view property corresponds to a
 * {@link com.haulmont.chile.core.model.MetaProperty} with the same name.
 *
 */
public class ViewProperty implements Serializable {

    private static final long serialVersionUID = 4098678639930287203L;

    private String name;

    private View view;

    private FetchMode fetchMode = FetchMode.AUTO;

    public ViewProperty(String name, @Nullable View view) {
        this(name, view, FetchMode.AUTO);
    }

    @Deprecated
    public ViewProperty(String name, @Nullable View view, boolean lazy) {
        this.name = name;
        this.view = view;
    }

    public ViewProperty(String name, @Nullable View view, FetchMode fetchMode) {
        this.name = name;
        this.view = view;
        this.fetchMode = fetchMode;
    }

    /**
     * @return property name that is a metaclass attribute name
     */
    public String getName() {
        return name;
    }

    /**
     * @return view of the property if the corresponding metaclass attribute is a reference
     */
    @Nullable
    public View getView() {
        return view;
    }

    /**
     * @return fetch mode if the property is a reference
     */
    public FetchMode getFetchMode() {
        return fetchMode;
    }

    /**
     * DEPRECATED since v.6
     */
    @Deprecated
    public boolean isLazy() {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}