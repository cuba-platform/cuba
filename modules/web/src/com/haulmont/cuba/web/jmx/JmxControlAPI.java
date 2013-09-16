/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanAttribute;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanDomain;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanInfo;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanOperation;

import java.util.List;

/**
 * Interface to provide JMX control functionality for local and remote JMX interfaces
 *
 * @author artamonov
 * @version $Id$
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
     * Loads attributes for managed bean descriptor
     *
     * @param info     managed bean descriptor
     * @return managed bean descriptor with attributes
     */
    ManagedBeanInfo loadAttributes(ManagedBeanInfo info);

    /**
     * Loads attribute value for managed bean attribute
     *
     * @param attribute attribute descriptor
     * @return attribute descriptor with value
     */
    ManagedBeanAttribute loadAttributeValue(ManagedBeanAttribute attribute);

    /**
     * Saves attribute value to JMX node
     *
     * @param attribute attribute descriptor
     */
    void saveAttributeValue(ManagedBeanAttribute attribute);

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