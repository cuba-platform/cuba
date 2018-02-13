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
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.remoting.discovery.ServerSelector;
import com.haulmont.cuba.core.sys.serialization.SerializationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.util.StopWatch;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * HttpInvokerRequestExecutor that executes a request on a server which is selected according to the current cluster
 * topology, provided by {@link ServerSelector}.
 */
public class ClusteredHttpInvokerRequestExecutor extends SimpleHttpInvokerRequestExecutor {

    private ServerSelector serverSelector;

    private static final Logger log = LoggerFactory.getLogger(ClusteredHttpInvokerRequestExecutor.class);

    public ClusteredHttpInvokerRequestExecutor(ServerSelector serverSelector) {
        this.serverSelector = serverSelector;

        String connectTimeoutProp = AppContext.getProperty("cuba.connectionTimeout");
        setConnectTimeout(connectTimeoutProp == null ? -1 : Integer.parseInt(connectTimeoutProp));

        String readTimeoutProp = AppContext.getProperty("cuba.connectionReadTimeout");
        setReadTimeout(readTimeoutProp == null ? -1 : Integer.parseInt(readTimeoutProp));
    }

    @Override
    protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
            throws IOException, ClassNotFoundException {

        RemoteInvocationResult result;

        Object context = serverSelector.initContext();
        String url = currentServiceUrl(serverSelector.getUrl(context), config);
        if (url == null)
            throw new IllegalStateException("Server URL list is empty");

        while (true) {
            HttpURLConnection con = openConnection(url);
            try {
                StopWatch sw = new StopWatch();
                prepareConnection(con, baos.size());
                writeRequestBody(config, con, baos);
                sw.start("waiting time");
                validateResponse(config, con);
                CountingInputStream responseInputStream = new CountingInputStream(readResponseBody(config, con));
                sw.stop();

                serverSelector.success(context);

                sw.start("reading time");
                try (ObjectInputStream ois = createObjectInputStream(decorateInputStream(responseInputStream), config.getCodebaseUrl())) {
                    result = doReadRemoteInvocationResult(ois);
                }
                sw.stop();
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Receiving HTTP invoker response for service at [%s], with size %s, %s", config.getServiceUrl(),
                            responseInputStream.getCount(), printStopWatch(sw)));
                }
                break;
            } catch (IOException e) {
                log.info(String.format("Invocation of %s failed: %s", url, e));

                serverSelector.fail(context);
                url = currentServiceUrl(serverSelector.getUrl(context), config);
                if (url != null) {
                    log.info("Trying to invoke the next available URL: " + url);
                    continue;
                }
                log.info("No more URL available");
                throw e;
            }
        }
        return result;
    }

    @Nullable
    protected String currentServiceUrl(String url, HttpInvokerClientConfiguration config) {
        return url == null ? null :  url + "/" + config.getServiceUrl();
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
