/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanAttribute;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanInfo;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import javax.management.openmbean.CompositeData;
import java.util.*;

public class ThreadsDatasource extends CollectionDatasourceImpl<ThreadSnapshot, UUID> {

    protected JmxControlAPI jmxControlAPI;

    private long prevUptime = -1;

    private Map<Long, Long> prevThread2CpuTime = new HashMap<>();

    private Map<Long, ThreadSnapshot> id2Thread = new HashMap<>();

    public ThreadsDatasource() {
        jmxControlAPI = AppBeans.get(JmxControlAPI.class);
    }

    protected Object getAttributeValue(JmxInstance node, String beanObjectName, String attrName) {
        ManagedBeanInfo bean = jmxControlAPI.getManagedBean(node, beanObjectName);
        Object res = null;
        if (bean != null) {
            ManagedBeanAttribute attr = jmxControlAPI.loadAttribute(bean, attrName);
            jmxControlAPI.loadAttributeValue(attr);
            res = attr.getValue();
        }
        return res;
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        JmxInstance node = (JmxInstance) params.get("node");
        ManagedBeanInfo threadingBean = jmxControlAPI.getManagedBean(node, "java.lang:type=Threading");
        int nCPUs = (int) getAttributeValue(node, "java.lang:type=OperatingSystem", "AvailableProcessors");

        final long[] allThreadsIds = (long[]) getAttributeValue(node, "java.lang:type=Threading", "AllThreadIds");
        ManagedBeanOperation getThreadInfo = jmxControlAPI.getOperation(threadingBean, "getThreadInfo", new String[]{"[J"});
        CompositeData[] threadsInfo = (CompositeData[]) jmxControlAPI.invokeOperation(getThreadInfo, new Object[]{allThreadsIds});

        final long currentUptime = (long) getAttributeValue(node, "java.lang:type=Runtime", "Uptime");
        ManagedBeanOperation getThreadCpuTime = jmxControlAPI.getOperation(threadingBean, "getThreadCpuTime", new String[]{"[J"});
        long[] threadCpuTime = (long[]) jmxControlAPI.invokeOperation(getThreadCpuTime, new Object[]{allThreadsIds});
        if (prevUptime > 0L && currentUptime > prevUptime) {
            // elapsedTime is in ms
            long elapsedTime = currentUptime - prevUptime;
            for (int i = 0; i < allThreadsIds.length; i++) {
                // elapsedCpu is in ns
                Long threadId = allThreadsIds[i];
                Long prevCpuTimeLong = prevThread2CpuTime.get(threadId); //for new threads returns null.
                long prevCpuTime = prevCpuTimeLong != null ? prevCpuTimeLong : 0L;
                long elapsedCpu = threadCpuTime[i] - prevCpuTime;
                // cpuUsage could go higher than 100% because elapsedTime
                // and elapsedCpu are not fetched simultaneously. Limit to 99%.
                double cpuUsage = Math.min(99d, elapsedCpu / (elapsedTime * 1000000d  /*convert to ns*/
                        / 100 /*percents*/ * nCPUs));
                getThreadSnapshot(threadId).setCpu(cpuUsage);
            }
        }
        prevUptime = currentUptime;
        for (int i = 0; i < allThreadsIds.length; i++) {
            long time = threadCpuTime[i] != -1L ? threadCpuTime[i] : 0L;
            prevThread2CpuTime.put(allThreadsIds[i], time);
        }

        ManagedBeanOperation findDeadlockedThreads = jmxControlAPI.getOperation(threadingBean, "findDeadlockedThreads", null);
        Long[] deadlockedThreads = (Long[]) jmxControlAPI.invokeOperation(findDeadlockedThreads, null);
        Set<Long> deadLockedThreadsSet = new HashSet<>();
        if (deadlockedThreads != null) {
            CollectionUtils.addAll(deadLockedThreadsSet, deadlockedThreads);
        }
        Set<Long> allThreadsSet = new HashSet<>();
        CollectionUtils.addAll(allThreadsSet, ArrayUtils.toObject(allThreadsIds));

        //remove all terminated threads.
        Collection<ThreadSnapshot> toRemove = new LinkedList<>();
        for (ThreadSnapshot snapshot : getItems()) {
            if (!allThreadsSet.contains(snapshot.getThreadId())) {
                toRemove.add(snapshot);
            }
        }
        for (ThreadSnapshot snapshot : toRemove) {
            removeItem(snapshot);
        }

        //update visual data.
        for (int i = 0; i < threadsInfo.length; i++) {
            CompositeData info = threadsInfo[i];
            if (info != null) {
                Long threadId = (Long) info.get("threadId");
                ThreadSnapshot item = getThreadSnapshot(threadId);
                item.setName((String) info.get("threadName"));
                item.setStatus(info.get("threadState").toString());
                item.setDeadLocked(deadLockedThreadsSet.contains(threadId));
            } else {
                removeItem(getThreadSnapshot(allThreadsIds[i])); //no thread info available.
            }
        }

    }

    protected ThreadSnapshot getThreadSnapshot(Long threadId) {
        ThreadSnapshot res = id2Thread.get(threadId);
        if (res == null) {
            res = new ThreadSnapshot();
            res.setThreadId(threadId);
            id2Thread.put(threadId, res);

            data.put(res.getId(), res);
            attachListener(res);
        }
        return res;
    }

    @Override
    public void clear() {
        super.clear();
        id2Thread.clear();
        prevThread2CpuTime.clear();
    }

    @Override
    public void deleted(ThreadSnapshot item) {
        super.deleted(item);
        id2Thread.remove(item.getThreadId());
        prevThread2CpuTime.remove(item.getThreadId());
    }

    protected String getStackTrace(Long threadId) {
        JmxInstance node = (JmxInstance) savedParameters.get("node");
        ManagedBeanInfo threadingBean = jmxControlAPI.getManagedBean(node, "java.lang:type=Threading");
        ManagedBeanOperation getThreadInfo = jmxControlAPI.getOperation(threadingBean, "getThreadInfo", new String[]{"long", "int"});
        CompositeData threadInfo = (CompositeData) jmxControlAPI.invokeOperation(getThreadInfo, new Object[]{threadId, Integer.MAX_VALUE});

        StringBuilder sb = new StringBuilder();
        if (threadInfo != null) {
            CompositeData[] traces = (CompositeData[]) threadInfo.get("stackTrace");
            ThreadSnapshot t = getThreadSnapshot(threadId);
            sb.append(t.getName()).append(" [id=").append(threadId).append("] (").append(t.getStatus()).append(")\n");
            for (CompositeData trace : traces) {
                String className = (String) trace.get("className");
                String methodName = (String) trace.get("methodName");
                int line = (int) trace.get("lineNumber");
                sb.append(className).append(".").append(methodName).append(":").append(line).append("\n");
            }
        }
        return sb.toString();
    }
}