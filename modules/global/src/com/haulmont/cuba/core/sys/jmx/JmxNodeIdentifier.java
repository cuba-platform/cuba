/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jmx;

import com.haulmont.cuba.core.global.NodeIdentifier;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 * @author artamonov
 * @version $Id$
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