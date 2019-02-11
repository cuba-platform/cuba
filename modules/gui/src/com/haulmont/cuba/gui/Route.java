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

package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.screen.Screen;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Registers an annotated class as corresponding to some route. It means that that screen should be opened when URL ends
 * with specified route and that URL should be changed according to the route when the screen is opened.
 * <p><br>
 *
 * Example:
 * <br>
 * Screen annotated with {@code @Route("help")} corresponds to "{@code /app/{rootRoute}/help}" route,
 * where {rootRoute} equals to currently opened root screen.
 * <p>
 *
 * This screen will be opened when URL changes from "{@code /app/{rootRoute}}" to "{@code /app/{rootRoute}/help}"
 * and URL will be changed in the same way when the screen is opened.
 * <p><br>
 *
 * Required parent screen that should be opened to form a route can be specified with "parent" property. If this
 * property is set but parent screen isn't opened annotated screen route will not be applied.
 * <p><br>
 *
 * The "parentPrefix" property enables to merge common route parts if a route of previous screen in tab is the same as
 * route configured in this property.
 * <p><br>
 *
 * Example. Let two screens exist:
 * <pre>
 *   &#64;Route("orders")
 *   public class OrderBrowse { ... }
 *
 *   &#64;Route(route = "orders/edit", parentPrefix = "orders")
 *   public class OrderEdit { ... }
 * </pre>
 *
 * When OrderEdit screen is opened after OrderBrowse screen resulting address will be "{@code app/{rootRoute}/users/edit}".
 * <p><br>
 *
 * It allows to specify clear routes for each screen and avoid repeats in URL.
 * Annotated class must be a direct or indirect subclass of {@link Screen}.
 * <p><br>
 *
 * The "root" property enables to specify that the route is for root window (like LoginWindow or MainWindow).
 *
 * @see Screen
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Route {

    String VALUE_ATTRIBUTE = "value";
    String PATH_ATTRIBUTE = "path";
    String PARENT_PREFIX_ATTRIBUTE = "parentPrefix";
    String ROOT_ATTRIBUTE = "root";

    @AliasFor(PATH_ATTRIBUTE)
    String value() default "";

    @AliasFor(VALUE_ATTRIBUTE)
    String path() default "";

    String parentPrefix() default "";

    boolean root() default false;
}
