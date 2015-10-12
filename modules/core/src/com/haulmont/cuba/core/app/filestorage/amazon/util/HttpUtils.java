/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.filestorage.amazon.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author ${USER}
 * @version $Id$
 *
 * Various Http helper routines
 */
public class HttpUtils {
    public static class HttpResponse {
        protected InputStream inputStream;
        protected int status;

        public HttpResponse(InputStream inputStream, int status) {
            this.inputStream = inputStream;
            this.status = status;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public int getStatus() {
            return status;
        }

        public boolean isStatusOk() {
            return status >= 200 && status < 300;
        }

        public boolean isStatusNotFound() {
            return status == 404;
        }
    }


    /**
     * Makes a http request to the specified endpoint
     */
    public static HttpResponse invokeHttpRequest(URL endpointUrl,
                                                 String httpMethod,
                                                 Map<String, String> headers,
                                                 String requestBody) {
        HttpURLConnection connection = createHttpConnection(endpointUrl, httpMethod, headers);
        try {
            if (requestBody != null) {
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(requestBody);
                wr.flush();
                wr.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        }
        return executeHttpRequest(connection);
    }

    public static HttpResponse executeHttpRequest(HttpURLConnection connection) {
        try {
            // Get Response
            InputStream is;
            int status = -1;
            try {
                status = connection.getResponseCode();
                is = connection.getInputStream();
            } catch (IOException e) {
                is = connection.getErrorStream();
            }

            return new HttpResponse(is, status);
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        }
    }

    public static HttpURLConnection createHttpConnection(URL endpointUrl,
                                                         String httpMethod,
                                                         Map<String, String> headers) {
        try {
            HttpURLConnection connection = (HttpURLConnection) endpointUrl.openConnection();
            connection.setRequestMethod(httpMethod);

            if (headers != null) {
                System.out.println("--------- Request headers ---------");
                for (String headerKey : headers.keySet()) {
                    System.out.println(headerKey + ": " + headers.get(headerKey));
                    connection.setRequestProperty(headerKey, headers.get(headerKey));
                }
            }

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create connection. " + e.getMessage(), e);
        }
    }

    public static String urlEncode(String url, boolean keepPathSlash) {
        String encoded;
        try {
            encoded = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding is not supported.", e);
        }
        if (keepPathSlash) {
            encoded = encoded.replace("%2F", "/");
        }
        return encoded;
    }
}
