/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

import java.io.Serializable;
import java.util.Objects;

public class SecurityScope implements Serializable {
    private static final long serialVersionUID = 5466663776112824721L;

    protected String name;

    public static final String DEFAULT_SCOPE_NAME = "GENERIC_UI";

    public SecurityScope(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLocName() {
        Messages messages = AppBeans.get(Messages.class);
        return messages.getMainMessage(String.format("securityScope_%s", name));
    }

    public boolean isDefault() {
        return DEFAULT_SCOPE_NAME.equals(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityScope that = (SecurityScope) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
