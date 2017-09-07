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

package com.haulmont.restapi.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AfterRestInvocationEvent extends ApplicationEvent {

    private ServletRequest request;
    private ServletResponse response;
    private boolean invocationPrevented;

    public AfterRestInvocationEvent(Authentication authentication,
                                    ServletRequest request, ServletResponse response, boolean invocationPrevented) {
        super(authentication);
        this.request = request;
        this.response = response;
        this.invocationPrevented = invocationPrevented;
    }

    @Override
    public Authentication getSource() {
        return (Authentication) super.getSource();
    }

    public Authentication getAuthentication() {
        return (Authentication) super.getSource();
    }

    public ServletRequest getRequest() {
        return request;
    }

    public ServletResponse getResponse() {
        return response;
    }

    public boolean isInvocationPrevented() {
        return invocationPrevented;
    }
}