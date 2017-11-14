/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.security;

/**
 * Marker interface for beans that will be wired by {@link com.haulmont.cuba.web.sys.CubaHttpFilter} automatically.
 * You can use it to implement additional authentication, pre- and post-processing of request and response.
 */
public interface HttpRequestFilter extends javax.servlet.Filter {
    /**
     * Defines the highest precedence for {@link org.springframework.core.Ordered} providers of the platform.
     */
    int HIGHEST_PLATFORM_PRECEDENCE = 100;

    /**
     * Defines the lowest precedence for {@link org.springframework.core.Ordered} providers of the platform.
     */
    int LOWEST_PLATFORM_PRECEDENCE = 1000;
}