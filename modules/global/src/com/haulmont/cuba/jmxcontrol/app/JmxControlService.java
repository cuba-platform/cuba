/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 17.08.2010 10:40:21
 * $Id$
 */
package com.haulmont.cuba.jmxcontrol.app;

import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanAttribute;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanDomain;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanInfo;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanOperation;

import java.util.List;

public interface JmxControlService {
    String NAME = "cuba_JmxControlService";

    public List<ManagedBeanInfo> getManagedBeans();

    public void loadAttributes(ManagedBeanInfo info);

    public void loadAttributeValue(ManagedBeanAttribute attr);

    public void saveAttributeValue(ManagedBeanAttribute attr);

    public Object invokeOperation(ManagedBeanOperation operation, Object[] parameterValues);

    public List<ManagedBeanDomain> getDomains();
}
