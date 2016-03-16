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

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class that holds the information about current cluster topology.
 * <p/>
 * Always inject or lookup this bean by name, not by type, because an application project can define several instances
 * of this type to work with different middleware blocks.
 *
 */
public class ClusterInvocationSupport {

    /**
     * Default name for the bean instance used by the platform code.
     */
    public static final String NAME = "cuba_clusterInvocationSupport";

    public interface Listener {
        void urlListChanged(List<String> newUrlList);
    }

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected List<String> urls;
    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected String baseUrl;
    protected int connectTimeout;
    protected int readTimeout;

    protected boolean randomPriority = Boolean.valueOf(AppContext.getProperty("cuba.randomServerPriority"));

    protected String servletPath = "remoting";

    protected List<Listener> listeners = new ArrayList<>();

    public ClusterInvocationSupport() {
        baseUrl = AppContext.getProperty("cuba.connectionUrlList");

        String connectTimeoutProp = AppContext.getProperty("cuba.connectionTimeout");
        connectTimeout = connectTimeoutProp == null ? -1 : Integer.parseInt(connectTimeoutProp);

        String readTimeoutProp = AppContext.getProperty("cuba.connectionReadTimeout");
        readTimeout = readTimeoutProp == null ? -1 : Integer.parseInt(readTimeoutProp);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void init() {
        urls = new ArrayList<>();
        String[] strings = baseUrl.split("[,;]");
        for (String string : strings) {
            if (!StringUtils.isBlank(string)) {
                urls.add(string + "/" + servletPath);
            }
        }
        if (urls.size() > 1 && randomPriority) {
            Collections.shuffle(urls);
        }
    }

    public List<String> getUrlList() {
        return urls;
    }

    public List<String> getUrlList(String serviceName) {
        lock.readLock().lock();
        try {
            List<String> list = new ArrayList<>(urls.size());
            for (String url : urls) {
                list.add(url + "/" + serviceName);
            }
            return list;
        } finally {
            lock.readLock().unlock();
        }
    }

    public synchronized void updateUrlPriority(String successfulUrl) {
        List<String> newList = new ArrayList<>();
        String url = successfulUrl.substring(0, successfulUrl.lastIndexOf("/"));
        newList.add(url);
        lock.writeLock().lock();
        try {
            for (String u : urls) {
                if (!u.equals(url)) {
                    newList.add(u);
                }
            }
            log.debug("Connection URL priority changed: " + urls + " -> " + newList);
            urls = newList;
            for (Listener listener : listeners) {
                listener.urlListChanged(Collections.unmodifiableList(urls));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addListener(Listener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }
}
