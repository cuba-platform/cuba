/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.ds;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.AbstractTreeDatasource;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanDomain;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanInfo;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.*;

/**
 * @author budarov
 * @version $Id$
 */
public class ManagedBeanInfoDatasource extends AbstractTreeDatasource<ManagedBeanInfo, UUID> {

    private JmxInstance jmxInstance = null;

    private JmxControlAPI jmxControlAPI = AppBeans.get(JmxControlAPI.NAME);

    @Override
    protected Tree<ManagedBeanInfo> loadTree(Map<String, Object> params) {
        String tag = getLoggingTag("TDS");
        StopWatch sw = new Log4JStopWatch(tag, Logger.getLogger(UIPerformanceLogger.class));
        List<Node<ManagedBeanInfo>> nodes = new ArrayList<>();

        if (jmxInstance != null) {
            List<ManagedBeanDomain> domains = jmxControlAPI.getDomains(jmxInstance);

            Map<String, Node<ManagedBeanInfo>> domainMap = new HashMap<>();

            for (ManagedBeanDomain mbd : domains) {
                ManagedBeanInfo dummy = new ManagedBeanInfo();
                dummy.setDomain(mbd.getName());

                Node<ManagedBeanInfo> node = new Node<>(dummy);
                domainMap.put(mbd.getName(), node);
                nodes.add(node);
            }

            List<ManagedBeanInfo> list = loadManagedBeans(params);
            for (ManagedBeanInfo mbi : list) {
                if (mbi != null) {
                    if (domainMap.containsKey(mbi.getDomain())) {
                        domainMap.get(mbi.getDomain()).addChild(new Node<>(mbi));
                    }
                }
            }

            // remove root nodes that might have left without children after filtering
            for (Node<ManagedBeanInfo> rootNode : new ArrayList<>(nodes)) {
                if (rootNode.getChildren().isEmpty()) {
                    nodes.remove(rootNode);
                }
            }
        }

        sw.stop();

        return new Tree<>(nodes);
    }

    private List<ManagedBeanInfo> loadManagedBeans(Map<String, Object> params) {
        List<ManagedBeanInfo> managedBeans = jmxControlAPI.getManagedBeans(jmxInstance);
        List<ManagedBeanInfo> res = new ArrayList<>();

        // filter by object name
        String objectName = (String) params.get("objectName");
        if (objectName != null) {
            objectName = objectName.toLowerCase();
            for (ManagedBeanInfo mbi : managedBeans) {
                if (mbi.getObjectName() != null && mbi.getObjectName().toLowerCase().contains(objectName)) {
                    res.add(mbi);
                }
            }
            return res;
        }

        return managedBeans;
    }

    public JmxInstance getJmxInstance() {
        return jmxInstance;
    }

    public void setJmxInstance(JmxInstance jmxInstance) {
        this.jmxInstance = jmxInstance;
    }
}