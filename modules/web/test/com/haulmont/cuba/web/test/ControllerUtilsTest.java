/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.test;

import com.haulmont.cuba.web.controllers.ControllerUtils;
import junit.framework.TestCase;

import java.net.URI;

/**
 * @author artamonov
 * @version $Id$
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