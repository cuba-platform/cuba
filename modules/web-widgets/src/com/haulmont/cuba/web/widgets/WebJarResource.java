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

package com.haulmont.cuba.web.widgets;

import com.vaadin.server.ClientConnector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation enables to declare web resource dependencies for the annotated {@link ClientConnector}.
 * <p>
 * To declare such dependency you should use the following template: <code>"webJarName:resource"</code>.
 * <p>
 * Example: <code>"jquery:jquery.min.js"</code>
 * <p>
 * To override web resources, that was previously referenced by this annotation, put new files in the following path:
 * <code>VAADIN/webjars/webJarName/webJarVersion/pathToResource</code>.
 * <p>
 * Example: <code>VAADIN/webjars/jquery-ui/1.12.1/jquery-ui.min.js</code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebJarResource {

    /**
     * Web resources to load before initializing the client-side connector.
     *
     * @return an array of WebJar resource identifiers
     */
    String[] value();

    /**
     * A path that will be used while resolving resource from a file system.
     *
     * @return override path
     */
    String overridePath() default "";
}
