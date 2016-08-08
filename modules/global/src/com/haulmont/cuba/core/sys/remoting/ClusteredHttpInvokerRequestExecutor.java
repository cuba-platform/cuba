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

import com.google.common.io.CountingInputStream;
import com.haulmont.cuba.core.sys.serialization.SerializationSupport;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.util.StopWatch;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * HttpInvokerRequestExecutor that executes a request on a server which is selected according to the current cluster
 * topology, provided by {@link ClusterInvocationSupport}.
 */
public class ClusteredHttpInvokerRequestExecutor extends SimpleHttpInvokerRequestExecutor {

    private ClusterInvocationSupport support;

    public ClusteredHttpInvokerRequestExecutor(ClusterInvocationSupport support) {
        this.support = support;
        setConnectTimeout(support.getConnectTimeout());
        setReadTimeout(support.getReadTimeout());
    }

    @Override
    protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
            throws IOException, ClassNotFoundException {
        List<String> urlList = support.getUrlList(config.getServiceUrl());
        if (urlList.isEmpty())
            throw new IllegalStateException("URL list is empty");

        RemoteInvocationResult result = null;
        for (int i = 0; i < urlList.size(); i++) {
            String url = urlList.get(i);
            HttpURLConnection con = openConnection(url);
            try {
                StopWatch sw = new StopWatch();
                prepareConnection(con, baos.size());
                writeRequestBody(config, con, baos);
                sw.start("waiting time");
                validateResponse(config, con);
                CountingInputStream responseInputStream = new CountingInputStream(readResponseBody(config, con));
                sw.stop();
                if (i > 0) {
                    support.updateUrlPriority(url);
                }
                sw.start("reading time");
                try (ObjectInputStream ois = createObjectInputStream(decorateInputStream(responseInputStream), config.getCodebaseUrl())) {
                    result = doReadRemoteInvocationResult(ois);
                }
                sw.stop();
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Receiving HTTP invoker response for service at [%s], with size %s, %s", config.getServiceUrl(),
                            responseInputStream.getCount(), printStopWatch(sw)));
                }
                break;
            } catch (IOException e) {
                logger.info(String.format("Invocation of %s failed: %s", url, e));

                if (i < urlList.size() - 1) {
                    logger.info("Trying to invoke the next available URL: " + urlList.get(i + 1));
                    continue;
                }
                logger.info("No more URL available");
                throw e;
            }
        }
        return result;
    }

    protected HttpURLConnection openConnection(String serviceUrl) throws IOException {
        URLConnection con = new URL(serviceUrl).openConnection();
        if (!(con instanceof HttpURLConnection)) {
            throw new IOException(String.format("Service URL [%s] is not an HTTP URL", serviceUrl));
        }
        return (HttpURLConnection) con;
    }

    @Override
    protected void doWriteRemoteInvocation(RemoteInvocation invocation, ObjectOutputStream oos) throws IOException {
        SerializationSupport.serialize(invocation, oos);
    }

    @Override
    protected RemoteInvocationResult doReadRemoteInvocationResult(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (RemoteInvocationResult) SerializationSupport.deserialize(ois);
    }

    protected String printStopWatch(StopWatch sw) {
        StringBuilder sb = new StringBuilder();
        StopWatch.TaskInfo[] tasks = sw.getTaskInfo();
        for (int i = 0; i < tasks.length; i++) {
            StopWatch.TaskInfo task = tasks[i];
            sb.append(task.getTaskName()).append(" ").append(task.getTimeMillis());
            if (i < tasks.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
