/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 20.08.2010 11:56:28
 * $Id$
 */

package com.haulmont.cuba.web.ui.jmxcontrol.ds;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.jmxcontrol.app.JmxControlService;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanAttribute;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanInfo;
import com.haulmont.cuba.jmxcontrol.global.JmxControlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.UUID;

public class ManagedBeanAttributeDatasource extends CollectionDatasourceImpl<ManagedBeanAttribute, UUID> {
    private static final long serialVersionUID = 3622116729736938653L;

    private Log log = LogFactory.getLog(getClass());

    public ManagedBeanAttributeDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName, boolean softDeletion) {
        super(context, dataservice, id, metaClass, viewName, softDeletion);
    }

    public ManagedBeanAttributeDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, View view) {
        super(context, dataservice, id, metaClass, view);
    }

    public ManagedBeanAttributeDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        data.clear();

        Datasource mbeanDs = getDsContext().get("mbeanDs");
        ManagedBeanInfo mbean = (ManagedBeanInfo) mbeanDs.getItem();

        if (mbean != null) {
            JmxControlService jcs = ServiceLocator.lookup(JmxControlService.NAME);
            try {
                mbean = jcs.loadAttributes(mbean);
            }
            catch (JmxControlException e) {
                log.error(e);
            }

            if (mbean.getAttributes() != null) {
                for (ManagedBeanAttribute attr: mbean.getAttributes()) {
                    data.put(attr.getId(), attr);
                }
            }
        }
    }
}
