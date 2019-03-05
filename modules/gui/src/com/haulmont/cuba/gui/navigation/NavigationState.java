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

import com.haulmont.cuba.gui.UrlRouting;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Immutable object of this class represents some navigation state determined by URL fragment.
 * <p>
 * For example, if URL is:
 * <pre>{@code
 *   http://host:port/app/#main/12/orders/view?id=a9fc2d30b51ef30b7e4b5a1c2d
 * }</pre>
 * URL fragment will be:
 * <pre>{@code
 *   main/12/orders/view?id=a9fc2d30b51ef30b7e4b5a1c2d
 * }</pre>
 * Where:
 * <pre>
 *   {@code main} - root screen route (in this case route of {@code AppMainWindow})
 *   {@code 12} - URL state mark
 *   {@code orders/view} - route of nested screen (or screens)
 *   {@code id=a9fc2d30b51ef30b7e4b5a1c2d} - parameter
 * </pre>
 *
 * @see UrlRouting
 */
public class NavigationState {

    public static final NavigationState EMPTY =
            new NavigationState("", "", "", Collections.emptyMap());

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

    /**
     * @return joined by "&amp;" sign URL params, or empty string if no params for this state
     */
    public String getParamsString() {
        if (MapUtils.isEmpty(params)) {
            return "";
        }

        return params.entrySet()
                .stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    /**
     * @return current state combined into URL fragment, e.g. {@code main/0/orders?status=shipped}
     */
    public String asRoute() {
        StringBuilder route = new StringBuilder();

        if (StringUtils.isNotEmpty(root)) {
            route.append(root);
        }

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

    @Override
    public String toString() {
        return "NavigationState{" +
                "root='" + root + '\'' +
                (StringUtils.isEmpty(stateMark) ? ""
                        : ", stateMark='" + stateMark + '\'') +
                (StringUtils.isEmpty(nestedRoute) ? ""
                        : ", nestedRoute='" + nestedRoute + '\'') +
                (MapUtils.isEmpty(params) ? ""
                        : ", params=(" + getParamsString() + ')') +
                "}";
    }
}
