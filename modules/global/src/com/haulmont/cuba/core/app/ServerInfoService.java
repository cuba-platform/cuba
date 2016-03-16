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
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import java.util.List;
import java.util.TimeZone;

/**
 * Service interface to provide initial information for clients. Can be invoked before login when user session
 * is not yet established.
 *
 */
public interface ServerInfoService {

    String NAME = "cuba_ServerInfoService";

    String getReleaseNumber();

    String getReleaseTimestamp();

    List<View> getViews();

    View getView(Class<? extends Entity> entityClass, String name);

    /**
     * Return time zone used by server application.
     * Useful for remote clients which may run on machines with another default time zone (like desktop client).
     *
     * @return server time zone
     */
    TimeZone getTimeZone();

    /**
     * @return current time on the server in milliseconds
     */
    long getTimeMillis();
}
