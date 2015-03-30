/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DatatypeFormatter;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.GroupDatasourceImpl;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.JmxControlException;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanAttribute;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanInfo;

import javax.management.InstanceNotFoundException;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import java.util.*;

/**
 * @author krivenko
 * @version $Id$
 */
public class StatisticsDatasource extends GroupDatasourceImpl<PerformanceParameter, UUID> {
    private static final long serialVersionUID = 3919263985912380723L;

    protected JmxInstance node;

    protected JmxControlAPI jmxControlAPI;

    private Map<String, ManagedBeanAttribute> name2attr = new HashMap<>();

    private int averageInterval;

    public StatisticsDatasource() {
        jmxControlAPI = AppBeans.get(JmxControlAPI.class);
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        this.node = (JmxInstance)params.get("node");
        this.averageInterval=(int)params.get("avgInterval");

        if (data.isEmpty()) {
            loadInitialParameters();
        }

        loadCurrentValues();
        //attachListeners is called in getParameter.
    }

    @Override
    public void clear() {
        super.clear();
        name2attr.clear();
    }

    protected ManagedBeanAttribute findAttribute(String beanObjectName, String attrName) {
        ManagedBeanAttribute res=name2attr.get(attrName);
        if (res==null) {
            ManagedBeanInfo bean = jmxControlAPI.getManagedBean(node, beanObjectName);
            res = bean != null ? jmxControlAPI.loadAttribute(bean, attrName) : null;
            name2attr.put(attrName, res);
        }
        if (res!=null) {
            jmxControlAPI.loadAttributeValue(res);
        }
        return res;
    }

    public void loadInitialParameters() {
                createParameter("Memory", "Heap Memory Usage", false, true);
                createParameter("Memory", "Heap Memory Max", false, true);
                createParameter("Memory", "Non-Heap Memory Usage", false, true);
                createParameter("Memory", "Non-Heap Memory Max", false, true);
                createParameter("Memory", "Free Physical Memory Size", false, true);
                createParameter("Memory", "Free Swap Memory Size", false, true);
                createParameter("Memory", "Total Physical Memory Size", false, true);
                createParameter("Memory", "Total Swap Memory Size", false, true);
                createParameter("CPU Load", "CPU Load", false, true);
                createParameter("Database Connections Pool", "Active Connections Number", false, true);
                createParameter("Database Connections Pool", "Idle Connections Number", false, false);
                createParameter("Database Connections Pool", "Maximum Active Connections", false, false);
                createParameter("Database", "Active Transactions Count", false, true);
                createParameter("Database", "Committed Transactions Count", true, false);
                createParameter("Database", "Rolled-Back Transactions Count", true, false);
                createParameter("Threading", "Live Thread Count", false, true);
                createParameter("Threading", "Peak Live Thread Count", false, true);
                createParameter("Application", "Uptime", false, false);
                createParameter("Application", "Start Time", false, false);
                createParameter("Client Requests", "User Sessions Count", false, true);
                createParameter("Client Requests", "Web-Client Requests Count", true, true);
                createParameter("Client Requests", "Middleware Requests Count", true, true);
                createParameter("Client Requests", "Schedulers Calls Count", true, true);

        for (PerformanceParameter param : getItems()) {
            if (param.getShowAverage()) param.setAverageInterval(averageInterval);
        }
    }

    protected PerformanceParameter createParameter(String paramGroup, String paramName, boolean showUptime, boolean showAverage) {
        PerformanceParameter param = new PerformanceParameter();
        param.setParameterName(paramName);
        param.setParameterGroup(paramGroup);
        param.setShowUptime(showUptime);
        param.setShowAverage(showAverage);

        data.put(param.getId(), param);
        attachListener(param);
        return param;
    }

    protected void loadCurrentValues() {
        try {
                    ManagedBeanAttribute heapUsage = findAttribute("java.lang:type=Memory", "HeapMemoryUsage");
                    CompositeData heapData = (CompositeData) heapUsage.getValue();
                    getParameter("Heap Memory Usage").setCurrentLongValue((Long) heapData.get("used"));
                    getParameter("Heap Memory Max").setCurrentLongValue((Long) heapData.get("max"));

                    ManagedBeanAttribute nonHeapUsage = findAttribute("java.lang:type=Memory", "NonHeapMemoryUsage");
                    CompositeData nonHeapData = (CompositeData) nonHeapUsage.getValue();
                    getParameter("Non-Heap Memory Usage").setCurrentLongValue((Long) nonHeapData.get("used"));
                    getParameter("Non-Heap Memory Max").setCurrentLongValue((Long) nonHeapData.get("max"));

                    setParameters("java.lang:type=OperatingSystem",
                            new String[]{"FreePhysicalMemorySize", "FreeSwapSpaceSize", "TotalPhysicalMemorySize", "TotalSwapSpaceSize"},
                            new String[]{"Free Physical Memory Size", "Free Swap Memory Size", "Total Physical Memory Size", "Total Swap Memory Size"});

                    setParameters("java.lang:type=OperatingSystem",
                            new String[]{"SystemCpuLoad"},
                            new String[]{"CPU Load"});

                    setParameters("Catalina:type=DataSource,context=/app-core,host=localhost,class=javax.sql.DataSource,name=\"jdbc/CubaDS\"",
                            new String[]{"numActive", "numIdle", "maxActive"},
                            new String[]{"Active Connections Number", "Idle Connections Number", "Maximum Active Connections"});

                    setParameters("app-core.cuba:type=StatisticsCounter",
                            new String[]{"ActiveTransactionsCount", "CommittedTransactionsCount", "RolledBackTransactionsCount"},
                            new String[]{"Active Transactions Count", "Committed Transactions Count", "Rolled-Back Transactions Count"});

                    setParameters("java.lang:type=Threading",
                            new String[]{"ThreadCount", "PeakThreadCount"},
                            new String[]{"Live Thread Count", "Peak Live Thread Count"});

                    ManagedBeanAttribute uptimeAttr = findAttribute("java.lang:type=Runtime", "Uptime");
                    getParameter("Uptime").setCurrentStringValue(String.format("%d s", (long) uptimeAttr.getValue() / 1000));
                    getParameter("Uptime").setCurrentLongValue((long) uptimeAttr.getValue());

                    DatatypeFormatter formatter = AppBeans.get(DatatypeFormatter.class);
                    ManagedBeanAttribute startTimeAttr = findAttribute("java.lang:type=Runtime", "StartTime");
                    getParameter("Start Time").setCurrentStringValue(formatter.formatDateTime(new Date((long) startTimeAttr.getValue())));

                    setParameters("app-core.cuba:type=UserSessions",
                            new String[]{"Count"},
                            new String[]{"User Sessions Count"});
                    setParameters("app.cuba:type=StatisticsCounter",
                            new String[]{"WebClientRequestsCount"},
                            new String[]{"Web-Client Requests Count"});
                    setParameters("app-core.cuba:type=StatisticsCounter",
                            new String[]{"MiddlewareRequestsCount", "SchedulersCallsCount"},
                            new String[]{"Middleware Requests Count", "Schedulers Calls Count"});
        }
        catch (InstanceNotFoundException | ReflectionException e) {
            throw new JmxControlException(e);
        }
    }

    protected void setParameters(String objectName, String[] beanProps, String[] paramNames) throws ReflectionException, InstanceNotFoundException {
        for (int i = 0; i < beanProps.length; i++) {
            String beanProp = beanProps[i];
            ManagedBeanAttribute attr = findAttribute(objectName, beanProp);
            if (attr!=null) {
                String paramName = paramNames[i];
                PerformanceParameter param = getParameter(paramName);
                Object value = attr.getValue();
                if (value instanceof Long) {
                    param.setCurrentLongValue((Long) value);
                }
                else if (value instanceof Integer) {
                    param.setCurrentLongValue(Long.valueOf((Integer) value));
                }
                else if (value instanceof Double) {
                    param.setCurrentDoubleValue((Double) value);
                }
                else {
                    param.setCurrentStringValue(value.toString());
                }
            }
        }
    }

    protected PerformanceParameter getParameter(String paramName) {
        PerformanceParameter res = null;
        for (PerformanceParameter candidate : (Collection<PerformanceParameter>)data.values()) {
            if (paramName.equals(candidate.getParameterName())) {
                res = candidate;
                break;
            }
        }
        return res;
    }

}
