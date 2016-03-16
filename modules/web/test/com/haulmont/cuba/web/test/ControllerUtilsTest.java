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

package com.haulmont.cuba.web.test;

import com.haulmont.cuba.web.controllers.ControllerUtils;
import junit.framework.TestCase;

import java.net.URI;

/**
 */
public class ControllerUtilsTest extends TestCase {
    public void testGetLocationWithoutParams() throws Exception {
        URI localUrl = new URI("http://localhost:8080/app?a");
        assertEquals(ControllerUtils.getLocationWithoutParams(localUrl), "http://localhost:8080/app");

        URI externalUrl = new URI("http://ya.ru/app/sample/?param=value");
        assertEquals(ControllerUtils.getLocationWithoutParams(externalUrl), "http://ya.ru/app/sample/");

        URI debugUrl = new URI("http://localhost:8080/app/?debug#!");
        assertEquals(ControllerUtils.getLocationWithoutParams(debugUrl), "http://localhost:8080/app/");
    }
}