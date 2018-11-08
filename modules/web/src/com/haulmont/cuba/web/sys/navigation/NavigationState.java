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

package com.haulmont.cuba.web.sys.navigation;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class NavigationState {

    protected final String root;
    protected final String stateMark;
    protected final String nestedRoute;
    protected final Map<String, String> params;

    public NavigationState(String root, String stateMark, String nestedRoute, Map<String, String> params) {
        this.root = root;
        this.stateMark = stateMark;
        this.nestedRoute = nestedRoute;
        this.params = params;
    }

    public static NavigationState empty() {
        return new NavigationState("", "", "", Collections.emptyMap());
    }

    public String getRoot() {
        return root;
    }

    public String getStateMark() {
        return stateMark;
    }

    public String getNestedRoute() {
        return nestedRoute;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getParamsString() {
        if (MapUtils.isEmpty(params)) {
            return "";
        }

        return params.entrySet()
                .stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    public String asRoute() {
        StringBuilder route = new StringBuilder(root);

        if (StringUtils.isNotEmpty(stateMark)) {
            route.append('/').append(stateMark);
        }

        if (StringUtils.isNotEmpty(nestedRoute)) {
            route.append('/').append(nestedRoute);
        }

        if (MapUtils.isNotEmpty(params)) {
            route.append("?").append(getParamsString());
        }

        return route.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }

        NavigationState thatState = (NavigationState) that;
        return Objects.equals(this.asRoute(), thatState.asRoute());
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, stateMark, nestedRoute, params);
    }
}
