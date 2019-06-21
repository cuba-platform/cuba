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

import com.haulmont.cuba.gui.navigation.NavigationState;
import com.vaadin.server.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class UrlTools {

    private static final Logger log = LoggerFactory.getLogger(UrlTools.class);

    /**
     * Root route regexp. Intended to match first part of a fragment:
     *
     * <pre>{@code
     *    /#<root_route>
     * }</pre>
     */
    protected static final String ROOT_ROUTE = "^([\\w-]+)$";
    protected static final Pattern ROOT_ROUTE_PATTERN = Pattern.compile(ROOT_ROUTE);

    /**
     * Nested screens route regexp. Intended to match a fragment that contains root and nested screen routes
     *
     * <pre>{@code
     *    /#<root_route>/[<url_state_mark>/]<nested_screen_route>[/<nested_screen_route>]
     * }</pre>
     */
    protected static final String NESTED_ROUTE = "^([\\w-]+)(?:/(\\d+))?(?:/([\\w-]+(?:|/[\\w-]+)*))?$";
    protected static final Pattern NESTED_ROUTE_PATTERN = Pattern.compile(NESTED_ROUTE);

    /**
     * Params route regexp. Intended to match a fragment that contains root and nested screen routes and URL params
     * part:
     *
     * <pre>{@code
     *    /#<root_route>/[<url_state_mark>/]<nested_screen_route>?<params_part>
     * }</pre>
     */
    protected static final String PARAMS_ROUTE = "^([\\w-]+)(?:(?:/(\\d+))?/([\\w-]+(?:|/[\\w-]+)*))?\\?(.+)$";
    protected static final Pattern PARAMS_ROUTE_PATTERN = Pattern.compile(PARAMS_ROUTE);

    /**
     * URL params regexp. Intended to match param pairs:
     *
     * <pre>{@code
     *    p1=v2[&p2=v2]...
     * }</pre>
     */
    protected static final String PARAMS_REGEX =
            "^(?:(?:\\w+=[-a-zA-Z0-9_/+%]+)?|\\w+=[-a-zA-Z0-9_/+%]+(?:&\\w+=[-a-zA-Z0-9_/+%]+)+)$";
    protected static final Pattern PARAMS_PATTERN = Pattern.compile(PARAMS_REGEX);

    public static void pushState(String navigationState) {
        checkNotNullArgument(navigationState, "Navigation state cannot be null");

        if (headless()) {
            log.debug("Unable to push navigation state in headless mode");
            return;
        }

        String state = !navigationState.isEmpty()
                ? "#" + navigationState
                : "";

        if (!state.isEmpty()) {
            Page.getCurrent().pushState(state);
        } else {
            Page.getCurrent().pushState(getEmptyFragmentUri());
        }
    }

    public static void replaceState(String navigationState) {
        checkNotNullArgument(navigationState, "Navigation state cannot be null");

        if (headless()) {
            log.debug("Unable to replace navigation state in headless mode");
            return;
        }

        String state = !navigationState.isEmpty()
                ? "#" + navigationState
                : "";

        if (!state.isEmpty()) {
            Page.getCurrent().replaceState(state);
        } else {
            Page.getCurrent().replaceState(getEmptyFragmentUri());
        }
    }

    public static NavigationState parseState(String uriFragment) {
        if (uriFragment == null || uriFragment.isEmpty()) {
            return NavigationState.EMPTY;
        }

        if (uriFragment.endsWith("/")) {
            uriFragment = uriFragment.substring(0, uriFragment.lastIndexOf('/'));
        }

        NavigationState navigationState = parseRootRoute(uriFragment);

        if (navigationState == null) {
            navigationState = parseNestedRoute(uriFragment);
        }

        if (navigationState == null) {
            navigationState = parseParamsRoute(uriFragment);
        }

        if (navigationState == null) {
            log.debug("Unable to determine navigation state for the given fragment: \"{}\"", uriFragment);
            return NavigationState.EMPTY;
        }

        return navigationState;
    }

    protected static URI getEmptyFragmentUri() {
        URI location = Page.getCurrent().getLocation();

        try {
            return new URI(location.getScheme(), location.getSchemeSpecificPart(), null);
        } catch (URISyntaxException e) {
            log.info("Failed to form new location to reset fragment");
        }

        return location;
    }

    protected static NavigationState parseRootRoute(String uriFragment) {
        Matcher matcher = ROOT_ROUTE_PATTERN.matcher(uriFragment);
        if (!matcher.matches()) {
            return null;
        }

        String root = matcher.group(1);
        return new NavigationState(root, "", "", Collections.emptyMap());
    }

    protected static NavigationState parseNestedRoute(String uriFragment) {
        Matcher matcher = NESTED_ROUTE_PATTERN.matcher(uriFragment);
        if (!matcher.matches()) {
            return null;
        }

        String root = matcher.group(1);
        String stateMark;
        String nestedRoute;

        if (matcher.groupCount() == 2) {
            stateMark = "";
            nestedRoute = matcher.group(2);
        } else {
            stateMark = matcher.group(2);
            nestedRoute = matcher.group(3);
        }

        return new NavigationState(root, stateMark, nestedRoute, Collections.emptyMap());
    }

    protected static NavigationState parseParamsRoute(String uriFragment) {
        Matcher matcher = PARAMS_ROUTE_PATTERN.matcher(uriFragment);
        if (!matcher.matches()) {
            return null;
        }

        String root = matcher.group(1);
        String stateMark;
        String nestedRoute;
        String params = matcher.group(matcher.groupCount());

        if (matcher.groupCount() == 3) {
            stateMark = "";
            nestedRoute = matcher.group(2);
        } else {
            stateMark = matcher.group(2);
            nestedRoute = matcher.group(3);
        }

        return new NavigationState(root, stateMark, nestedRoute, extractParams(params));
    }

    protected static Map<String, String> extractParams(String paramsString) {
        if (!PARAMS_PATTERN.matcher(paramsString).matches()) {
            log.info("Unable to extract params from the given params string: \"{}\"", paramsString);
            return Collections.emptyMap();
        }

        String[] paramPairs = paramsString.split("&");
        Map<String, String> paramsMap = new LinkedHashMap<>(paramPairs.length);

        for (String paramPair : paramPairs) {
            String[] param = paramPair.split("=");
            paramsMap.put(param[0], param[1]);
        }

        return paramsMap;
    }

    public static boolean headless() {
        Page current = Page.getCurrent();
        if (current == null) {
            return true;
        }

        return current.getUI().getSession() == null;
    }
}
