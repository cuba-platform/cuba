/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.ds;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.JmxControlException;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanAttribute;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

/**
 * @author budarov
 * @version $Id$
 */
public class ManagedBeanAttributeDatasource extends CollectionDatasourceImpl<ManagedBeanAttribute, UUID> {

    private Logger log = LoggerFactory.getLogger(getClass());

    private JmxControlAPI jmxControlAPI = AppBeans.get(JmxControlAPI.NAME);

    @Override
    protected void loadData(Map<String, Object> params) {
        data.clear();

        Datasource mbeanDs = getDsContext().get("mbeanDs");
        ManagedBeanInfo mbean = (ManagedBeanInfo) mbeanDs.getItem();

        if (mbean != null) {
            try {
                jmxControlAPI.loadAttributes(mbean);
            } catch (JmxControlException e) {
                log.error("Error loading attributes", e);
            }

            if (mbean.getAttributes() != null) {
                for (ManagedBeanAttribute attr : mbean.getAttributes()) {
                    data.put(attr.getId(), attr);
                }
            }
        }
    }
}