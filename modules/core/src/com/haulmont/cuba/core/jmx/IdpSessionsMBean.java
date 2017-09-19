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
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.security.idp.IdpSessionStore;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.List;

/**
 * JMX interface for {@link IdpSessionStore}
 */
@ManagedResource(description = "Manages identity provider sessions")
public interface IdpSessionsMBean {
    @ManagedOperation(description = "Print session info")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "sessionId", description = "")})
    String getSessionInfo(String sessionId);

    @ManagedOperation(description = "Activate service provider ticket and print session info")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "serviceProviderTicket", description = "")})
    String activateSessionTicket(String serviceProviderTicket);

    @ManagedOperation(description = "Create service provider ticket for existing session")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "sessionId", description = "")})
    String createServiceProviderTicket(String sessionId);

    @ManagedOperation(description = "Remove existing session")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "sessionId", description = "")})
    String removeSession(String sessionId);

    @ManagedOperation(description = "Print all session ids")
    List<String> getSessions();

    @ManagedOperation(description = "Print all non activated service provider tickets")
    List<String> getTickets();
}