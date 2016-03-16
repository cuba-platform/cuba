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
package com.haulmont.cuba.security.jmx;

/**
 * JMX interface for {@link com.haulmont.cuba.security.app.EntityLogAPI}
 *
 */
public interface EntityLogMBean {
    /**
     * Is logging enabled?
     */
    boolean isEnabled();

    /**
     * Enables or disables logging
     */
    void setEnabled(boolean enabled);

    /**
     * Invalidates configuration cache.
     * The configuration will be recreated from the database on next lifecycle event.
     */
    void invalidateCache();
}
