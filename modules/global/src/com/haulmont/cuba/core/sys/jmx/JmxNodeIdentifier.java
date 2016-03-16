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

package com.haulmont.cuba.core.sys.jmx;

import com.haulmont.cuba.core.global.NodeIdentifier;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 */
@Component("cuba_JmxNodeIdentifierMBean")
public class JmxNodeIdentifier implements JmxNodeIdentifierMBean {

    @Inject
    private NodeIdentifier nodeIdentifier;

    @Override
    public String getNodeName() {
        return nodeIdentifier.getNodeName();
    }
}