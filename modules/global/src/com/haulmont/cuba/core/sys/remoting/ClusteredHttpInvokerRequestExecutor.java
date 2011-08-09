/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.remoting.support.RemoteInvocationResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ClusteredHttpInvokerRequestExecutor extends SimpleHttpInvokerRequestExecutor {

    private ClusterInvocationSupport support;

    public ClusteredHttpInvokerRequestExecutor(ClusterInvocationSupport support) {
        this.support = support;
    }

    @Override
    protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws IOException, ClassNotFoundException {
        HttpURLConnection con = null;

        List<String> urlList = support.getUrlList(config.getServiceUrl());
        for (int i = 0; i < urlList.size(); i++) {
            String url = urlList.get(i);
            con = openConnection(url);
            try {
                prepareConnection(con, baos.size());
                writeRequestBody(config, con, baos);
                if (i > 0) {
                    support.updateUrlPriority(url);
                }
                break;
            } catch (IOException e) {
                List list = ExceptionUtils.getThrowableList(e);
                boolean isConnectException = false;
                for (Object throwable : list) {
                    if (throwable instanceof ConnectException) {
                        logger.info("Invocation of " + url + " failed: " + throwable);
                        isConnectException = true;
                        break;
                    }
                }
                if (isConnectException) {
                    if (i < urlList.size() - 1) {
                        logger.info("Trying to invoke the next available URL");
                        continue;
                    }
                    logger.info("No more URL available");
                }
                throw e;
            }
        }
        validateResponse(config, con);
        InputStream responseBody = readResponseBody(config, con);

        return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
    }

    protected HttpURLConnection openConnection(String serviceUrl) throws IOException {
        URLConnection con = new URL(serviceUrl).openConnection();
        if (!(con instanceof HttpURLConnection)) {
            throw new IOException("Service URL [" + serviceUrl + "] is not an HTTP URL");
        }
        return (HttpURLConnection) con;
    }
}
