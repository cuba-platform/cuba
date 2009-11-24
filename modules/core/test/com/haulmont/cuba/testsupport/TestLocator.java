/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2009 9:59:57
 *
 * $Id$
 */
package com.haulmont.cuba.testsupport;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.sys.LocatorImpl;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.management.MBeanInfo;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TestLocator extends LocatorImpl {

    public static class MBeanInfo {
        private Object mbean;
        private List<String> depends = new ArrayList<String>();

        public MBeanInfo(Object mbean, List<String> depends) {
            this.mbean = mbean;
            if (depends != null)
                this.depends.addAll(depends);
        }

        public List<String> getDepends() {
            return depends;
        }

        public Object getMbean() {
            return mbean;
        }
    }

    private Map<String, MBeanInfo> mbeans = new HashMap<String, MBeanInfo>();

    protected Context __getJndiContextImpl() {
        return TestContext.getInstance();
    }

    protected <T> T __lookupMBean(Class<T> mbeanClass, String name) {
        MBeanInfo mbeanInfo = mbeans.get(name);
        if (mbeanInfo != null)
            return (T) mbeanInfo.getMbean();
        else
            throw new IllegalStateException("MBean not registered: " + name);
    }

    public void registerMBean(String objectName, MBeanInfo mbean) {
        mbeans.put(objectName, mbean);
    }

    public Map<String, MBeanInfo> getMBeans() {
        return mbeans;
    }
}
