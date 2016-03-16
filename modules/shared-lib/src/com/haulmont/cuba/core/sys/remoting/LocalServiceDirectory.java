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

package com.haulmont.cuba.core.sys.remoting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class LocalServiceDirectory {

    private static Map<String, LocalServiceInvoker> invokers = new ConcurrentHashMap<>();

    private static Logger log = LoggerFactory.getLogger(LocalServiceDirectory.class);

    public static void registerInvoker(String name, LocalServiceInvoker invoker) {
        log.debug("Registering service " + name);
        invokers.put(name, invoker);
    }

    public static void unregisterInvoker(String name) {
        log.debug("Unregistering service " + name);
        invokers.remove(name);
    }

    public static LocalServiceInvoker getInvoker(String name) {
        return invokers.get(name);
    }
}