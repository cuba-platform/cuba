/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.HttpInvokerClientInterceptor;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;

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

    @Override
    protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws IOException, ClassNotFoundException {
        HttpURLConnection con = null;
        String[] urls = config.getServiceUrl().split("[,;]");
        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            con = openConnection(url);
            try {
                prepareConnection(con, baos.size());
                writeRequestBody(config, con, baos);
                if (i > 0) {
                    changeUrlPriority(config, urls, url);
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
                    if (i < urls.length - 1) {
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

    private void changeUrlPriority(HttpInvokerClientConfiguration config, String[] urls, String successUrl) {
        StringBuilder sb = new StringBuilder(successUrl);
        for (String url : urls) {
            if (!url.equals(successUrl)) {
                sb.append(",").append(url);
            }
        }
        ((UrlBasedRemoteAccessor) config).setServiceUrl(sb.toString());
    }

    protected HttpURLConnection openConnection(String serviceUrl) throws IOException {
        URLConnection con = new URL(serviceUrl).openConnection();
        if (!(con instanceof HttpURLConnection)) {
            throw new IOException("Service URL [" + serviceUrl + "] is not an HTTP URL");
        }
        return (HttpURLConnection) con;
    }
}
