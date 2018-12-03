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

import com.haulmont.bali.util.URLEncodeUtils;
import com.vaadin.server.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.haulmont.bali.util.Preconditions.checkNotEmptyString;
import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class UrlTools {

    private static final Logger log = LoggerFactory.getLogger(UrlTools.class);

    protected static final String ROOT_ROUTE = "^(\\w+)$";
    protected static final Pattern ROOT_ROUTE_PATTERN = Pattern.compile(ROOT_ROUTE);

    protected static final String NESTED_ROUTE = "^(\\w+)(?:/(\\d+))?(?:/([\\w-]+(?:|/[\\w-]+)*))?$";
    protected static final Pattern NESTED_ROUTE_PATTERN = Pattern.compile(NESTED_ROUTE);

    protected static final String PARAMS_ROUTE = "^(\\w+)(?:(?:/(\\d+))?/([\\w-]+(?:|/[\\w-]+)*))?\\?(.+)$";
    protected static final Pattern PARAMS_ROUTE_PATTERN = Pattern.compile(PARAMS_ROUTE);

    protected static final String PARAMS_REGEX =
            "^(?:(?:\\w+=[-a-zA-Z0-9_/+%]+)?|\\w+=[-a-zA-Z0-9_/+%]+(?:&\\w+=[-a-zA-Z0-9_/+%]+)+)$";
    protected static final Pattern PARAMS_PATTERN = Pattern.compile(PARAMS_REGEX);

    @Nullable
    public static String serializeId(Object id) {
        checkNotNullArgument(id, "Unable to serialize null id");

        String serialized = null;
        Class<?> idClass = id.getClass();

        if (String.class == idClass || Integer.class == idClass || Long.class == idClass) {
            serialized = URLEncodeUtils.encodeUtf8(id.toString());

        } else if (UUID.class == idClass) {
            try {
                byte[] bytes = ByteBuffer.allocate(Long.BYTES * 2)
                        .putLong(((UUID) id).getMostSignificantBits())
                        .putLong(((UUID) id).getLeastSignificantBits())
                        .array();

                String encoded = Base64.getEncoder().withoutPadding()
                        .encodeToString(bytes);

                serialized = URLEncodeUtils.encodeUtf8(encoded);
            } catch (Exception e) {
                log.info("An error occurred while serializing UUID id: {}", id, e);
            }
        } else {
            log.info("Unable to serialize id '{}' of type '{}'", id, idClass);
        }

        return serialized;
    }

    @Nullable
    public static Object deserializeId(Class idClass, String base64Id) {
        checkNotNullArgument(idClass, "Unable to deserialize id without its type");
        checkNotEmptyString("Unable to deserialize empty string");

        Object deserialized = null;
        String decoded = URLEncodeUtils.decodeUtf8(base64Id);

        try {
            if (String.class == idClass) {
                deserialized = decoded;

            } else if (Integer.class == idClass) {
                deserialized = Integer.valueOf(decoded);

            } else if (Long.class == idClass) {
                deserialized = Long.valueOf(decoded);

            } else if (UUID.class == idClass) {
                byte[] bytes = Base64.getDecoder().decode(decoded);
                ByteBuffer bb = ByteBuffer.wrap(bytes);
                deserialized = new UUID(bb.getLong(), bb.getLong());

            } else {
                log.info("Unable to deserialize base64 id '{}' of type '{}'", base64Id, idClass);
            }
        } catch (Exception e) {
            log.info("An error occurred while deserializing base64 id: '{}' of type '{}'", base64Id, idClass, e);
        }

        return deserialized;
    }

    public static void pushState(String navigationState) {
        checkNotEmptyString(navigationState, "Unable to push empty navigation state");

        if (headless()) {
            log.debug("Unable to push navigation state in headless mode");
            return;
        }

        Page.getCurrent().setUriFragment(navigationState, false);

        // TODO: get rid of this hack with Crockford Base32 encoding
        // replace state with the same route to ignore redundant URL encoding inside of java.net.URI
        replaceState(navigationState);
    }

    public static void replaceState(String navigationState) {
        checkNotEmptyString(navigationState, "Unable to replace by empty navigation state");

        if (headless()) {
            log.debug("Unable to replace navigation state in headless mode");
            return;
        }

        Page.getCurrent().replaceState("#" + navigationState);
    }

    public static NavigationState parseState(String uriFragment) {
        if (uriFragment == null || uriFragment.isEmpty()) {
            return NavigationState.empty();
        }

        NavigationState navigationState = parseRootRoute(uriFragment);

        if (navigationState == null) {
            navigationState = parseNestedRoute(uriFragment);
        }

        if (navigationState == null) {
            navigationState = parseParamsRoute(uriFragment);
        }

        if (navigationState == null) {
            log.info("Unable to determine navigation state for the given fragment: \"{}\"", uriFragment);
            return NavigationState.empty();
        }

        return navigationState;
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
