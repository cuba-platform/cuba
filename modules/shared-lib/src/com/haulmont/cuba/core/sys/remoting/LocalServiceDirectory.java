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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * INTERNAL.
 * <p>
 * This class holds a collection of {@link LocalServiceInvoker} instances. It must be loaded to a classloader shared
 * between the client tier and middleware.
 */
public class LocalServiceDirectory {

    private static Map<String, LocalServiceInvoker> invokers = new ConcurrentHashMap<>();

    private static CountDownLatch latch = new CountDownLatch(1);

    private static final Logger log = LoggerFactory.getLogger(LocalServiceDirectory.class);

    public static void registerInvoker(String name, LocalServiceInvoker invoker) {
        log.debug("Registering service {}", name);
        invokers.put(name, invoker);
    }

    public static void unregisterInvoker(String name) {
        log.debug("Unregistering service {}", name);
        invokers.remove(name);
    }

    public static LocalServiceInvoker getInvoker(String name) {
        try {
            latch.await(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted awaiting for context to start");
        }
        return invokers.get(name);
    }

    public static void start() {
        log.debug("Starting local service bridge");

        latch.countDown();
    }
}