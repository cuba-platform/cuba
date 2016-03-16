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

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanAttribute;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanDomain;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanInfo;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanOperation;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Interface to provide JMX control functionality for local and remote JMX interfaces
 *
 */
public interface JmxControlAPI {
    String NAME = "cuba_JmxControl";

    /**
     * @return the list with available JMX nodes in cluster
     */
    List<JmxInstance> getInstances();

    /**
     * @return the local JMX node descriptor
     */
    JmxInstance getLocalInstance();

    /**
     * @return the local cluster node name
     */
    String getLocalNodeName();

    /**
     * @return the remote cluster node name
     */
    String getRemoteNodeName(JmxInstance jmxInstance);

    /**
     * Loads the list of managed bean infos
     *
     * @param instance JMX node descriptor
     * @return the list with managed beans
     */
    List<ManagedBeanInfo> getManagedBeans(JmxInstance instance);

    /**
     * Loads the managed bean by its ObjectName
     * @param instance JMX node descriptor
     * @param beanObjectName exact ObjectName of the bean
     * @return found managed bean, null if no bean found
     */
    ManagedBeanInfo getManagedBean(JmxInstance instance, String beanObjectName);

    /**
     * Loads attributes for managed bean descriptor
     *
     * @param info     managed bean descriptor
     */
    void loadAttributes(ManagedBeanInfo info);

    /**
     * Loads attribute by its name. Note that the reference from ManagedBeanInfo
     * to loaded ManagedBeanAttribute is not set.
     *
     * @param info     managed bean descriptor
     * @return loaded attribute, null if no attribute found.
     */
    @Nullable
    ManagedBeanAttribute loadAttribute(ManagedBeanInfo info, String attributeName);

    /**
     * Loads attribute value for managed bean attribute
     *
     * @param attribute attribute descriptor
     */
    void loadAttributeValue(ManagedBeanAttribute attribute);

    /**
     * Saves attribute value to JMX node
     *
     * @param attribute attribute descriptor
     */
    void saveAttributeValue(ManagedBeanAttribute attribute);

    /**
     * Searches for the bean operation by its name and argument types.
     * @param bean  managed bean descriptor
     * @param operationName operation exact name
     * @param argTypes operation argument types
     * @return Found operation descriptor, null if not found
     */
    ManagedBeanOperation getOperation(ManagedBeanInfo bean, String operationName, @Nullable String[] argTypes);

    /**
     * Invokes method of managed bean
     *
     * @param operation       operation descriptor
     * @param parameterValues array with parameter values
     * @return invocation result
     */
    Object invokeOperation(ManagedBeanOperation operation, Object[] parameterValues);

    /**
     * Loads list of managed bean domains
     *
     * @param instance JMX node descriptor
     * @return the list of managed bean domains
     */
    List<ManagedBeanDomain> getDomains(JmxInstance instance);
}