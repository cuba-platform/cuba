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
 * JMX interface for {@link com.haulmont.cuba.security.app.UserSessionsAPI}
 *
 */
public interface UserSessionsMBean {

    /**
     * User session expiration timeout. Not the same as HTTP session timeout, but should have the same value.
     * @return  timeout in seconds
     */
    int getExpirationTimeoutSec();

    /**
     * Set user session expiration timeout for the current server session.
     * @param value timeout in seconds
     */
    void setExpirationTimeoutSec(int value);

    int getCount();

    String printSessions();

    void processEviction();

    /**
     * Kill specified session
     *
     * @param id Session id
     * @return Result status
     */
    String killSession(String id);
}
