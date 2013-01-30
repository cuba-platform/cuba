/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.jmx;

import com.haulmont.cuba.core.global.NodeIdentifier;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_JmxNodeIdentifierMBean")
public class JmxNodeIdentifier implements JmxNodeIdentifierMBean {

    @Inject
    private NodeIdentifier nodeIdentifier;

    @Override
    public String getNodeName() {
        return nodeIdentifier.getNodeName();
    }
}