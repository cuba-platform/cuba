/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DatatypeFormatter;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
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
public class StatisticsDatasource extends CollectionDatasourceImpl<PerformanceParameter, UUID> {
    private static final long serialVersionUID = 3919263985912380723L;

    protected JmxInstance node;

    protected JmxControlAPI jmxControlAPI;

    protected Category category;

    private Map<String, ManagedBeanAttribute> name2attr = new HashMap<>();

    private int averageInterval;

    public enum Category {
        MEMORY,
        CPU,
        DBPOOL,
        DB,
        THREADING,
        REQUESTS,
        APPLICATION
    }

    public StatisticsDatasource() {
        jmxControlAPI = AppBeans.get(JmxControlAPI.class);
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        this.node = (JmxInstance)params.get("node");
        this.averageInterval=(int)params.get("avgInterval");
        this.category=(Category)params.get("category");

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

        switch (category) {
            case MEMORY:
                createParameter("Heap Memory Usage", false, true);
                createParameter("Heap Memory Max", false, true);
                createParameter("Non-Heap Memory Usage", false, true);
                createParameter("Non-Heap Memory Max", false, true);
                createParameter("Free Physical Memory Size", false, true);
                createParameter("Free Swap Memory Size", false, true);
                createParameter("Total Physical Memory Size", false, true);
                createParameter("Total Swap Memory Size", false, true);
                break;

            case CPU:
                createParameter("CPU Load", false, true);
                break;

            case DBPOOL:
                createParameter("Active Connections Number", false, true);
                createParameter("Idle Connections Number", false, false);
                createParameter("Maximum Active Connections", false, false);
                break;

            case DB:
                createParameter("Active Transactions Count", false, true);
                createParameter("Committed Transactions Count", true, false);
                createParameter("Rolled-Back Transactions Count", true, false);

                break;

            case THREADING:
                createParameter("Live Thread Count", false, true);
                createParameter("Peak Live Thread Count", false, true);
                break;

            case APPLICATION:
                createParameter("Uptime", false, false);
                createParameter("Start Time", false, false);
                break;

            case REQUESTS:
                createParameter("User Sessions Count", false, true);
                createParameter("Web-Client Requests Count", true, true);
                createParameter("Middleware Requests Count", true, true);
                createParameter("Schedulers Calls Count", true, true);
                break;
        }
        for (PerformanceParameter param : getItems()) {
            if (param.getShowAverage()) param.setAverageInterval(averageInterval);
        }
    }

    protected PerformanceParameter createParameter(String paramName, boolean showUptime, boolean showAverage) {
        PerformanceParameter param = new PerformanceParameter();
        param.setParameterName(paramName);
        param.setShowUptime(showUptime);
        param.setShowAverage(showAverage);

        data.put(param.getId(), param);
        attachListener(param);
        return param;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    protected void loadCurrentValues() {
        try {
            switch (category) {
                case MEMORY:
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
                    break;

                case CPU:
                    setParameters("java.lang:type=OperatingSystem",
                            new String[]{"SystemCpuLoad"},
                            new String[]{"CPU Load"});
                    break;

                case DBPOOL:
                    setParameters("Catalina:type=DataSource,context=/app-core,host=localhost,class=javax.sql.DataSource,name=\"jdbc/CubaDS\"",
                            new String[]{"numActive", "numIdle", "maxActive"},
                            new String[]{"Active Connections Number", "Idle Connections Number", "Maximum Active Connections"});
                    break;

                case DB:
                    setParameters("app-core.cuba:type=StatisticsCounter",
                            new String[]{"ActiveTransactionsCount", "CommittedTransactionsCount", "RolledBackTransactionsCount"},
                            new String[]{"Active Transactions Count", "Committed Transactions Count", "Rolled-Back Transactions Count"});
                    break;

                case THREADING:
                    setParameters("java.lang:type=Threading",
                            new String[]{"ThreadCount", "PeakThreadCount"},
                            new String[]{"Live Thread Count", "Peak Live Thread Count"});
                    break;

                case APPLICATION:
                    ManagedBeanAttribute uptimeAttr = findAttribute("java.lang:type=Runtime", "Uptime");
                    getParameter("Uptime").setCurrentStringValue(String.format("%d s", (long) uptimeAttr.getValue() / 1000));
                    getParameter("Uptime").setCurrentLongValue((long) uptimeAttr.getValue());

                    DatatypeFormatter formatter = AppBeans.get(DatatypeFormatter.class);
                    ManagedBeanAttribute startTimeAttr = findAttribute("java.lang:type=Runtime", "StartTime");
                    getParameter("Start Time").setCurrentStringValue(formatter.formatDateTime(new Date((long) startTimeAttr.getValue())));
                    break;

                case REQUESTS:
                    setParameters("app-core.cuba:type=UserSessions",
                            new String[]{"Count"},
                            new String[]{"User Sessions Count"});
                    setParameters("app.cuba:type=StatisticsCounter",
                            new String[]{"WebClientRequestsCount"},
                            new String[]{"Web-Client Requests Count"});
                    setParameters("app-core.cuba:type=StatisticsCounter",
                            new String[]{"MiddlewareRequestsCount", "SchedulersCallsCount"},
                            new String[]{"Middleware Requests Count", "Schedulers Calls Count"});
                    break;
            }
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
