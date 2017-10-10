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

package com.haulmont.cuba.security.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * JMX interface for {@link com.haulmont.cuba.restapi.ServerTokenStore}
 */
@ManagedResource(description = "Manages user REST API tokens")
public interface ServerTokenStoreMBean {
    String NAME = "cuba_ServerTokenStoreMBean";

    @ManagedOperation(description = "Removes all REST API tokens for the specified user")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "userLogin", description = "user login, for which all tokens will be removed")
    })
    String removeTokensByUserLogin(String userLogin);
}
