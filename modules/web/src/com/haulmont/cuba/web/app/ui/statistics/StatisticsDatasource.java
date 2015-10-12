/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.data.impl.GroupDatasourceImpl;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.JmxControlException;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanAttribute;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.management.InstanceNotFoundException;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import java.util.*;

/**
 * @author krivenko
 * @version $Id$
 */
public class StatisticsDatasource extends GroupDatasourceImpl<PerformanceParameter, UUID> {

    private Logger log = LoggerFactory.getLogger(StatisticsDatasource.class);

    protected JmxInstance node;

    protected JmxControlAPI jmxControlAPI;

    protected Map<String, ManagedBeanAttribute> name2attr = new HashMap<>();

    protected final ManagedBeanAttribute NOT_FOUND_ATTR = new ManagedBeanAttribute();

    protected int refreshPeriod;

    protected long refreshCount;

    protected DatatypeFormatter datatypeFormatter = AppBeans.get(DatatypeFormatter.NAME);

    protected DateFormatter dateFormatter = new DateFormatter();
    protected DurationFormatter durationFormatter = new DurationFormatter();
    protected KilobyteFormatter kilobyteFormatter = new KilobyteFormatter();
    protected DoubleFormatter doubleFormatter = new DoubleFormatter();
    protected IntegerFormatter integerFormatter = new IntegerFormatter();
    protected PercentFormatter percentFormatter = new PercentFormatter();

    protected String coreAppName;
    protected String webAppName;

    protected StatCounter mwStatCounter;
    protected StatCounter webStatCounter;

    protected Double prevStartedTransactionsCount;
    protected Double prevWebRequestsCount;
    protected Double prevMiddlewareRequestsCount;
    protected Double prevCubaScheduledTasksCount;
    protected Double prevWebSpringScheduledTasksCount;
    protected Double prevMiddlewareSpringScheduledTasksCount;

    public StatisticsDatasource() {
        jmxControlAPI = AppBeans.get(JmxControlAPI.class);
        Configuration configuration = AppBeans.get(Configuration.class);

        coreAppName = getCoreAppName(configuration.getConfig(ClientConfig.class));
        webAppName = getWebAppName(configuration.getConfig(GlobalConfig.class));
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        JmxInstance node = (JmxInstance) params.get("node");
        if (node != this.node) {
            mwStatCounter = null;
            webStatCounter = null;
        }

        this.node = node;
        this.refreshPeriod = (int) params.get("refreshPeriod");

        if (mwStatCounter == null)
            mwStatCounter = new StatCounter(true);

        if (webStatCounter == null)
            webStatCounter = new StatCounter(false);

        if (data.isEmpty()) {
            initParameters();
        }

        loadCurrentValues();
        //attachListeners is called in getParameter.
    }

    @Override
    public void clear() {
        super.clear();
        name2attr.clear();
    }

    @Nullable
    protected ManagedBeanAttribute findAttribute(String beanObjectName, String attrName) {
        ManagedBeanAttribute res = name2attr.get(attrName);
        if (res == NOT_FOUND_ATTR)
            return null;
        if (res == null) {
            ManagedBeanInfo bean = jmxControlAPI.getManagedBean(node, beanObjectName);
            if (bean != null) {
                res = jmxControlAPI.loadAttribute(bean, attrName);
            }
            name2attr.put(attrName, res == null ? NOT_FOUND_ATTR : res);
        }
        if (res != null)
            jmxControlAPI.loadAttributeValue(res);
        return res;
    }

    public void initParameters() {
        createParameter("Application", "Start Time", false, dateFormatter);
        createParameter("Application", "Uptime", false, durationFormatter);
        createParameter("Memory", "Heap Memory Usage", true, kilobyteFormatter);
        createParameter("Memory", "Non-Heap Memory Usage", true, kilobyteFormatter);
        createParameter("Memory", "Free Physical Memory Size", true, kilobyteFormatter);
        createParameter("Memory", "Free Swap Space Size", true, kilobyteFormatter);
        createParameter("CPU and Threads", "System CPU Load", true, percentFormatter);
        createParameter("CPU and Threads", "Process CPU Load", true, percentFormatter);
        createParameter("CPU and Threads", "Thread Count", true, integerFormatter);
        createParameter("Database", "Active Connections", true, integerFormatter);
        createParameter("Database", "Idle Connections", true, integerFormatter);
        createParameter("Database", "Active Transactions", true, integerFormatter);
        createParameter("Database", "Transactions per Second", true, doubleFormatter);
        createParameter("Requests", "User Sessions", true, integerFormatter);
        createParameter("Requests", "Web Requests per Second", true, doubleFormatter);
        createParameter("Requests", "Middleware Requests per Second", true, doubleFormatter);
        createParameter("Requests", "CUBA Scheduled Tasks per Second", true, doubleFormatter);
        createParameter("Requests", "Web Spring Scheduled Tasks per Second", true, doubleFormatter);
        createParameter("Requests", "Middleware Spring Scheduled Tasks per Second", true, doubleFormatter);

        prevStartedTransactionsCount = mwStatCounter.getAttributeValue("StartedTransactionsCount");
        prevWebRequestsCount = webStatCounter.getAttributeValue("WebRequestsCount");
        prevMiddlewareRequestsCount = mwStatCounter.getAttributeValue("MiddlewareRequestsCount");
        prevCubaScheduledTasksCount = mwStatCounter.getAttributeValue("CubaScheduledTasksCount");
        prevWebSpringScheduledTasksCount = webStatCounter.getAttributeValue("SpringScheduledTasksCount");
        prevMiddlewareSpringScheduledTasksCount = mwStatCounter.getAttributeValue("SpringScheduledTasksCount");
    }

    protected PerformanceParameter createParameter(String paramGroup, String paramName, boolean showRecent,
                                                   Formatter<Double> formatter) {
        PerformanceParameter param = new PerformanceParameter();
        param.setParameterName(paramName);
        param.setParameterGroup(paramGroup);
        param.setShowRecent(showRecent);
        param.setFormatter(formatter);

        data.put(param.getId(), param);
        attachListener(param);
        return param;
    }

    protected void loadCurrentValues() {
        refreshCount++;
        for (PerformanceParameter parameter : getItems()) {
            parameter.setRefreshCount(refreshCount);
        }

        try {
            ManagedBeanAttribute uptimeAttr = findAttribute("java.lang:type=Runtime", "Uptime");
            if (uptimeAttr != null) {
                getParameter("Uptime").setCurrentLong((Long) uptimeAttr.getValue());
            }

            ManagedBeanAttribute startTimeAttr = findAttribute("java.lang:type=Runtime", "StartTime");
            if (startTimeAttr != null) {
                getParameter("Start Time").setCurrentLong((Long) startTimeAttr.getValue());
            }

            ManagedBeanAttribute heapUsage = findAttribute("java.lang:type=Memory", "HeapMemoryUsage");
            if (heapUsage != null) {
                CompositeData heapData = (CompositeData) heapUsage.getValue();
                PerformanceParameter parameter = getParameter("Heap Memory Usage");
                parameter.setCurrentLong((Long) heapData.get("used"));
                String max = kilobyteFormatter.format(((Long) heapData.get("max")).doubleValue());
                parameter.setDisplayName(parameter.getParameterName() + " (Max = " + max + ")");
            }

            setAverageParameter("Heap Memory Usage", "AvgHeapMemoryUsage", true);

            ManagedBeanAttribute nonHeapUsage = findAttribute("java.lang:type=Memory", "NonHeapMemoryUsage");
            if (nonHeapUsage != null) {
                CompositeData nonHeapData = (CompositeData) nonHeapUsage.getValue();
                PerformanceParameter parameter = getParameter("Non-Heap Memory Usage");
                parameter.setCurrentLong((Long) nonHeapData.get("used"));
                String max = kilobyteFormatter.format(((Long) nonHeapData.get("max")).doubleValue());
                parameter.setDisplayName(parameter.getParameterName() + " (Max = " + max + ")");
            }

            setAverageParameter("Non-Heap Memory Usage", "AvgNonHeapMemoryUsage", true);

            ManagedBeanAttribute attr = findAttribute("java.lang:type=OperatingSystem", "FreePhysicalMemorySize");
            if (attr != null) {
                PerformanceParameter param = getParameter("Free Physical Memory Size");
                param.setCurrentLong((Long) attr.getValue());

                attr = findAttribute("java.lang:type=OperatingSystem", "TotalPhysicalMemorySize");
                if (attr != null) {
                    String max = kilobyteFormatter.format(((Long) attr.getValue()).doubleValue());
                    param.setDisplayName(param.getParameterName() + " (Total = " + max + ")");
                }
            }

            attr = findAttribute("java.lang:type=OperatingSystem", "FreeSwapSpaceSize");
            if (attr != null) {
                PerformanceParameter param = getParameter("Free Swap Space Size");
                param.setCurrentLong((Long) attr.getValue());

                attr = findAttribute("java.lang:type=OperatingSystem", "TotalSwapSpaceSize");
                if (attr != null) {
                    String max = kilobyteFormatter.format(((Long) attr.getValue()).doubleValue());
                    param.setDisplayName(param.getParameterName() + " (Total = " + max + ")");
                }
            }

            setAverageParameter("Free Physical Memory Size", "AvgFreePhysicalMemorySize", true);
            setAverageParameter("Free Swap Space Size", "AvgFreeSwapSpaceSize", true);

            setParameters("java.lang:type=OperatingSystem",
                    new String[]{"SystemCpuLoad", "ProcessCpuLoad"},
                    new String[]{"System CPU Load", "Process CPU Load"});

            setAverageParameter("System CPU Load", "AvgSystemCpuLoad", true);
            setAverageParameter("Process CPU Load", "AvgProcessCpuLoad", true);

            attr = findAttribute(coreAppName + ".cuba:type=StatisticsCounter", "DbConnectionPoolNumActive");
            if (attr != null) {
                PerformanceParameter param = getParameter("Active Connections");
                param.setCurrent(((Integer) attr.getValue()).doubleValue());

                attr = findAttribute(coreAppName + ".cuba:type=StatisticsCounter", "DbConnectionPoolMaxTotal");
                if (attr != null) {
                    String max = integerFormatter.format(((Integer) attr.getValue()).doubleValue());
                    param.setDisplayName(param.getParameterName() + " (Max = " + max + ")");
                }
            }

            setParameters(coreAppName + ".cuba:type=StatisticsCounter",
                    new String[]{"DbConnectionPoolNumIdle"},
                    new String[]{"Idle Connections"});

            setAverageParameter("Active Connections", "AvgDbConnectionPoolNumActive", true);
            setAverageParameter("Idle Connections", "AvgDbConnectionPoolNumIdle", true);

            setParameters(coreAppName + ".cuba:type=StatisticsCounter",
                    new String[]{"ActiveTransactionsCount"},
                    new String[]{"Active Transactions"});

            setAverageParameter("Active Transactions", "AvgActiveTransactions", true);

            setTxPerSecParameter();
            setAverageParameter("Transactions per Second", "TransactionsPerSecond", true);

            attr = findAttribute("java.lang:type=Threading", "ThreadCount");
            if (attr != null) {
                PerformanceParameter param = getParameter("Thread Count");
                param.setCurrent(((Integer) attr.getValue()).doubleValue());

                attr = findAttribute("java.lang:type=Threading", "PeakThreadCount");
                if (attr != null) {
                    String max = integerFormatter.format(((Integer) attr.getValue()).doubleValue());
                    param.setDisplayName(param.getParameterName() + " (Peak = " + max + ")");
                }
            }

            setAverageParameter("Thread Count", "AvgThreadCount", true);

            setParameters(coreAppName + ".cuba:type=UserSessions",
                    new String[]{"Count"},
                    new String[]{"User Sessions"});

            setAverageParameter("User Sessions", "AvgUserSessions", true);

            setWebRequestsPerSecParameter();
            setAverageParameter("Web Requests per Second", "WebRequestsPerSecond", false);

            setMiddlewareRequestsPerSecParameter();
            setAverageParameter("Middleware Requests per Second", "MiddlewareRequestsPerSecond", true);

            setCubaScheduledTasksPerSecParameter();
            setAverageParameter("CUBA Scheduled Tasks per Second", "CubaScheduledTasksPerSecond", true);

            setMiddlewareSpringScheduledTasksPerSecParameter();
            setAverageParameter("Middleware Spring Scheduled Tasks per Second", "SpringScheduledTasksPerSecond", true);

            setWebSpringScheduledTasksPerSecParameter();
            setAverageParameter("Web Spring Scheduled Tasks per Second", "SpringScheduledTasksPerSecond", false);

        } catch (InstanceNotFoundException | ReflectionException e) {
            throw new JmxControlException(e);
        }
    }

    protected void setTxPerSecParameter() {
        Double count = mwStatCounter.getAttributeValue("StartedTransactionsCount");
        if (count != null) {
            double perSec = (count - prevStartedTransactionsCount) / (refreshPeriod / 1000);
            getParameter("Transactions per Second").setCurrent(perSec);
        }
        prevStartedTransactionsCount = count;
    }

    protected void setWebRequestsPerSecParameter() {
        Double count = webStatCounter.getAttributeValue("WebRequestsCount");
        if (count != null) {
            double perSec = (count - prevWebRequestsCount) / (refreshPeriod / 1000);
            getParameter("Web Requests per Second").setCurrent(perSec);
        }
        prevWebRequestsCount = count;
    }

    protected void setMiddlewareRequestsPerSecParameter() {
        Double count = mwStatCounter.getAttributeValue("MiddlewareRequestsCount");
        if (count != null) {
            double perSec = (count - prevMiddlewareRequestsCount) / (refreshPeriod / 1000);
            getParameter("Middleware Requests per Second").setCurrent(perSec);
        }
        prevMiddlewareRequestsCount = count;
    }

    protected void setCubaScheduledTasksPerSecParameter() {
        Double count = mwStatCounter.getAttributeValue("CubaScheduledTasksCount");
        if (count != null) {
            double perSec = (count - prevCubaScheduledTasksCount) / (refreshPeriod / 1000);
            getParameter("CUBA Scheduled Tasks per Second").setCurrent(perSec);
        }
        prevCubaScheduledTasksCount = count;
    }

    protected void setWebSpringScheduledTasksPerSecParameter() {
        Double count = webStatCounter.getAttributeValue("SpringScheduledTasksCount");
        if (count != null) {
            double perSec = (count - prevWebSpringScheduledTasksCount) / (refreshPeriod / 1000);
            getParameter("Web Spring Scheduled Tasks per Second").setCurrent(perSec);
        }
        prevWebSpringScheduledTasksCount = count;
    }

    protected void setMiddlewareSpringScheduledTasksPerSecParameter() {
        Double count = mwStatCounter.getAttributeValue("SpringScheduledTasksCount");
        if (count != null) {
            double perSec = (count - prevMiddlewareSpringScheduledTasksCount) / (refreshPeriod / 1000);
            getParameter("Middleware Spring Scheduled Tasks per Second").setCurrent(perSec);
        }
        prevMiddlewareSpringScheduledTasksCount = count;
    }

    protected String getCoreAppName(ClientConfig config) {
        List<String> list = config.getConnectionUrlList();
        if (list.isEmpty())
            return "app-core";
        String[] strings = list.get(0).split("/");
        return strings[strings.length - 1];
    }

    protected String getWebAppName(GlobalConfig config) {
        return config.getWebContextName();
    }

    protected void setAverageParameter(String paramName, String jmxAttrName, boolean fromCore) {
        StatCounter counter = fromCore ? mwStatCounter : webStatCounter;
        Double value = counter.getAttributeValue(jmxAttrName);
        if (value != null)
            getParameter(paramName).setAverage(value);
    }

    protected void setParameters(String objectName, String[] beanProps, String[] paramNames) throws ReflectionException, InstanceNotFoundException {
        for (int i = 0; i < beanProps.length; i++) {
            String beanProp = beanProps[i];
            ManagedBeanAttribute attr = findAttribute(objectName, beanProp);
            if (attr != null) {
                String paramName = paramNames[i];
                PerformanceParameter param = getParameter(paramName);
                Object value = attr.getValue();
                if (value instanceof Long) {
                    param.setCurrent(((Long) value).doubleValue());
                } else if (value instanceof Integer) {
                    param.setCurrent(((Integer) value).doubleValue());
                } else if (value instanceof Double) {
                    param.setCurrent((Double) value);
                }
            }
        }
    }

    protected PerformanceParameter getParameter(String paramName) {
        for (PerformanceParameter candidate : getItems()) {
            if (paramName.equals(candidate.getParameterName())) {
                return candidate;
            }
        }
        throw new DevelopmentException("Parameter does not exist: " + paramName);
    }

    protected class DateFormatter implements Formatter<Double> {

        public DateFormatter() {
        }

        @Override
        public String format(Double value) {
            if (value == null)
                return "";
            return datatypeFormatter.formatDateTime(new Date(value.longValue()));
        }
    }

    public static class DurationFormatter implements Formatter<Double> {

        public DurationFormatter() {
        }

        @Override
        public String format(Double value) {
            if (value == null)
                return "";
            int duration = (int) (value / 1000);
            int days = duration / 86400;
            int hours = (duration - days * 86400) / 3600;
            int mins = (duration - days * 86400 - hours * 3600) / 60;
            int secs = duration - days * 86400 - hours * 3600 - mins * 60;
            String res = secs + "s";
            if (mins > 0)
                res = mins + "m " + res;
            if (hours > 0)
                res = hours + "h " + res;
            if (days > 0)
                res = days + "d " + res;
            return res;
        }
    }

    protected class KilobyteFormatter implements Formatter<Double> {

        public KilobyteFormatter() {
        }

        @Override
        public String format(Double value) {
            if (value == null)
                return "";
            return datatypeFormatter.formatLong(value.longValue() / 1024) + " KB";
        }
    }

    protected class DoubleFormatter implements Formatter<Double> {

        public DoubleFormatter() {
        }

        @Override
        public String format(Double value) {
            if (value == null)
                return "";
            return datatypeFormatter.formatDouble(value);
        }
    }

    protected class IntegerFormatter implements Formatter<Double> {

        public IntegerFormatter() {
        }

        @Override
        public String format(Double value) {
            if (value == null)
                return "";
            return datatypeFormatter.formatLong(Math.round(value));
        }
    }

    protected class PercentFormatter implements Formatter<Double> {

        public PercentFormatter() {
        }

        @Override
        public String format(Double value) {
            if (value == null)
                return "";
            return datatypeFormatter.formatInteger(((int) (value * 100))) + "%";
        }
    }

    protected class StatCounter {
        ManagedBeanInfo statCounterMBean;

        public StatCounter(boolean core) {
            String appName = core ? coreAppName : webAppName;
            statCounterMBean = jmxControlAPI.getManagedBean(node, appName + ".cuba:type=StatisticsCounter");
            if (statCounterMBean == null) {
                log.info("MBean " + appName + ".cuba:type=StatisticsCounter not found, " +
                        (core ? "middleware" : "web") + " parameters will not be available");
            }
        }

        @Nullable
        Double getAttributeValue(String attrName) {
            if (statCounterMBean == null)
                return null;

            ManagedBeanAttribute attribute = name2attr.get(attrName);
            if (attribute == NOT_FOUND_ATTR)
                return null;

            attribute = jmxControlAPI.loadAttribute(statCounterMBean, attrName);
            if (attribute == null) {
                log.warn("Attribute " + attrName + " not found");
                name2attr.put(attrName, NOT_FOUND_ATTR);
                return null;
            }

            name2attr.put(attrName, attribute);
            jmxControlAPI.loadAttributeValue(attribute);
            return (Double) attribute.getValue();
        }
    }
}
